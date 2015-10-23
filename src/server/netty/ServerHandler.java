package server.netty;

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
import server.asyncServer.RequestHandler;

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

        String requestAsString = ((ByteBuf) msg).toString(io.netty.util.CharsetUtil.US_ASCII);
        Request request = new Request(requestAsString);
        Response response = new Response();

        StatusCode statusCode = RequestHandler.getResponse(request, response, rootDir);

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
