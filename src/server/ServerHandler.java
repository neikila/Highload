package server;

import handler.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by neikila on 19.10.15.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LogManager.getLogger(ServerHandler.class.getName());
    private String rootDir;

    public ServerHandler(String rootDir) {
        super();
        this.rootDir = rootDir;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        ChannelFuture channelFuture;
        Request request = new Request((ByteBuf) msg);
        StatusCode statusCode;

        Response response = new Response();
        ByteBuf byteBuf = ctx.alloc().buffer();
        Method method = request.getMethod();
        String filename = rootDir + request.getFilename();
        if (method != null) {
            if (method.equals(Method.GET) || method.equals(Method.HEAD)) {
                try {
                    if (Validator.validateFilename(filename)) {
                        response.readFile(filename);
                        statusCode = StatusCode.OK;
                    } else {
                        statusCode = StatusCode.BAD_REQUEST;
                    }
                } catch (IOException e) {
                    logger.error("File not found.");
                    statusCode = StatusCode.NOT_FOUND;
                }
            } else {
                statusCode = StatusCode.METHOD_NOT_ALLOWED;
            }
        } else {
            statusCode = StatusCode.NOT_IMPLEMENTED;
        }

        response.buildHeader(statusCode);
        byteBuf.writeBytes(response.getHeader().getBytes());
        if (statusCode.equals(StatusCode.OK)) {
            byteBuf.writeBytes(response.getFile());
        }

        channelFuture = ctx.write(byteBuf);
        ctx.flush();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("Finished");
                ctx.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        logger.error(cause.getStackTrace().toString());
        cause.printStackTrace();
        ctx.close();
    }
}
