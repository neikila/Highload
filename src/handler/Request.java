package handler;

import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Created by neikila on 19.10.15.
 */
public class Request {
    final private static Logger logger = LogManager.getLogger(Request.class.getName());
    private Method method;
    private String filename;

    public Request(ByteBuf byteBuf) {
        String request = byteBuf.toString(io.netty.util.CharsetUtil.US_ASCII);
        logger.debug(request);
        Scanner input = new Scanner(request);
        if (input.hasNext()) {
            String buf = input.nextLine();
            String[] splitted = buf.split(" ");
            method = Method.valueOf(splitted[0]);
            filename = splitted[1];
            if (filename.equals("/")) {
                filename = "/index.html";
            }
        } else {
            method = null;
        }
    }

    public String getFilename() {
        return filename;
    }

    public Method getMethod() {
        return method;
    }
}
