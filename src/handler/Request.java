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
        try {
            method = Method.valueOf(input.next());
            filename = input.next();
            filename = java.net.URLDecoder.decode(filename, "UTF-8");
            if (filename.equals("/")) {
                filename = "/index.html";
            }
            filename = filename.split("\\?|#")[0];
        } catch (Exception e) {
            logger.debug("Error");
            method = null;
        } finally {
            input.close();
        }
    }

    public String getFilename() {
        return filename;
    }

    public Method getMethod() {
        return method;
    }
}
