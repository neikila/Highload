package server;

import handler.Method;
import handler.Request;
import handler.Response;
import handler.StatusCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;

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
        ByteBuf byteBuf = ctx.alloc().buffer();

        Request request = new Request((ByteBuf) msg);
        StatusCode statusCode;

        Response response = new Response();
        Method method = request.getMethod();
        String filename = rootDir + request.getFilename();
        if (method != null) {
            if (method.equals(Method.GET) || method.equals(Method.HEAD)) {
                try {
                    logger.debug("filename: {}", filename);
                    Path path = Paths.get(filename);
                    Files.getLastModifiedTime(path);
                    response.readFile(filename);
                    statusCode = StatusCode.OK;
                }
                catch (NoSuchFileException e) {
                    logger.debug("File not found.");
                    statusCode = StatusCode.NOT_FOUND;
                }
                catch (FileSystemException e) {
                    logger.debug("Bad filename.");
                    statusCode = StatusCode.BAD_REQUEST;
                }
                catch (IOException e) {
                    logger.debug("File not found.");
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
