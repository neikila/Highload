package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import handler.ContentType;
import handler.Response;
import handler.StatusCode;

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

        long length = 200;

        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes(response.buildHeader(StatusCode.OK, length, ContentType.valueOf("html")).getBytes());
        MyFileReader.getFile(Server.rootDirectory, "index.html", byteBuf);
        byteBuf.writeBytes("test".getBytes());
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
        logger.error(cause.getStackTrace());
        cause.printStackTrace();
        ctx.close();
    }
}
