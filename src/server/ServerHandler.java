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

//    @Override
//    public void channelActive(final ChannelHandlerContext ctx) { // (1)
//        final ByteBuf time = ctx.alloc().buffer(30); // (2)
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//
//        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.close();
//            }
//        }); // (4)
//    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        ChannelFuture channelFuture;
        Request request = new Request((ByteBuf) msg);
        StatusCode statusCode;

        Response response = new Response();
        ByteBuf byteBuf = ctx.alloc().buffer();
        if (request.getMethod() != null) {
            switch (request.getMethod()) {
                case GET:
                    try {
                        if (request.getFilename().matches("(/\\w+)+\\.\\w+")) {
                            response.readFile(request.getFilename());
                            statusCode = StatusCode.OK;
                        } else {
                            statusCode = StatusCode.FORBIDDEN;
                        }
                    } catch (IOException e) {
                        logger.error("File not found.");
                        statusCode = StatusCode.NOT_FOUND;
                    }
                    break;
                case HEAD:
                    statusCode = StatusCode.OK;
                    break;
                default:
                    statusCode = StatusCode.METHOD_NOT_ALLOWED;
            }
        } else {
            statusCode = StatusCode.NOT_IMPLEMENTED;
        }

        response.buildHeader(statusCode);
        byteBuf.writeBytes(response.getHeader().getBytes());
        switch (statusCode) {
            case OK:
                byteBuf.writeBytes(response.getFile());
                break;
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
