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
        Response response = new Response();
        Headers headers = new Headers((ByteBuf) msg);

        ByteBuf byteBuf = ctx.alloc().buffer();
        try {
            byte[] file = MyFileReader.getFile(Server.rootDirectory, "index.html");
            byteBuf.writeBytes(response.buildHeader(StatusCode.OK, file.length, ContentType.valueOf("html")).getBytes());
            byteBuf.writeBytes(file);
        } catch (IOException e) {
            logger.error("File not found.");
            byteBuf.writeBytes(response.buildHeader(StatusCode.NOT_FOUND).getBytes());
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
