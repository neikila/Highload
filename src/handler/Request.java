package handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Created by neikila on 19.10.15.
 */
public class Request {
    final private static Logger logger = LogManager.getLogger(Request.class.getName());
    private Method method = null;
    private String filename;
    private String params;
    private String version;
    private boolean isCorrect;

    public Request(String request) {
        logger.debug(request);
        isCorrect = true;
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
            temp = input.next().split("\\/");
            version = temp[1];
            if(!version.equals("1.1") && !version.equals("1.0") || !temp[0].equals("HTTP")) {
                isCorrect = false;
            }
        } catch (Exception e) {
            logger.debug("Error");
            isCorrect = false;
        }
    }

    public boolean isCorrect() {
        return isCorrect;
    }
    // TODO isCorrect

    public String getFilename() {
        return filename;
    }

    public Method getMethod() {
        return method;
    }
}
