package handler;

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
    private String params;

    public Request(String request) {
        logger.debug(request);
        try (Scanner input = new Scanner(request)) {
            method = Method.valueOf(input.next());
            filename = input.next();
            String[] temp = filename.split("\\?|#");
            if (temp.length > 1) {
                params = temp[1];
            }
            filename = java.net.URLDecoder.decode(temp[0], "UTF-8");
            if (filename.endsWith("/")) {
                filename += "/index.html";
            }
        } catch (Exception e) {
            logger.debug("Error");
            method = null;
        }
    }
    // TODO isCorrect

    public String getFilename() {
        return filename;
    }

    public Method getMethod() {
        return method;
    }
}
