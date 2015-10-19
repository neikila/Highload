package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import response.ContentType;
import response.Response;
import response.StatusCode;

/**
 * Created by neikila on 19.10.15.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

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

        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes((ByteBuf)msg); // (1)
        byteBuf.writeBytes("\n\n".getBytes());
        byteBuf.writeBytes(response.buildHeader(StatusCode.OK, 200, ContentType.valueOf("html")).getBytes());
        channelFuture = ctx.write(byteBuf);
        ctx.flush(); // (2)
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("Finished");
                ctx.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
