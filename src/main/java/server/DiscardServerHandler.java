package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by neikila on 19.10.15.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        ByteBuf in = ((ByteBuf) msg).duplicate();
        try {
            while (in.isReadable()) { // (1)
                char ch = (char) in.readByte();
                System.out.print(ch);
                System.out.flush();
            }
        } finally {
            ctx.write(msg);
            ctx.flush(); // (2)

//            ReferenceCountUtil.release(in); // (2)
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
