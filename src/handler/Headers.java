package handler;

import io.netty.buffer.ByteBuf;

/**
 * Created by neikila on 19.10.15.
 */
public class Headers {
    public Headers(ByteBuf byteBuf) {
        System.out.println(byteBuf.toString());
    }
}
