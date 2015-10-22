package server;

import handler.Method;
import handler.Request;
import handler.Response;
import handler.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by neikila on 22.10.15.
 */
public class RequestHandler {
    private static Logger logger = LogManager.getLogger(RequestHandler.class.getName());

    public static StatusCode getResponse(Request request, Response response, String rootDir) {
        StatusCode statusCode;
        Method method = request.getMethod();
        String filename = rootDir + request.getFilename();
        if (method != null) {
            try {
                switch (method) {
                    case GET:
                        logger.debug("filename: {}", filename);
                        Path path = Paths.get(filename);
                        Files.getLastModifiedTime(path);
                        response.readFile(filename);
                    case HEAD:
                        response.countSize(filename);
                        statusCode = StatusCode.OK;
                        break;
//                catch (NoSuchFileException e) {
//                    logger.debug("File not found.");
//                    statusCode = StatusCode.NOT_FOUND;
//                }
//                catch (FileSystemException e) {
//                    logger.debug("Bad filename.");
//                    statusCode = StatusCode.BAD_REQUEST;
//                }
                    default:
                        statusCode = StatusCode.METHOD_NOT_ALLOWED;
                }
            }
            catch (IOException e) {
                logger.debug("File not found.");
                statusCode = StatusCode.NOT_FOUND;
            }
        } else {
            statusCode = StatusCode.NOT_IMPLEMENTED;
        }
        response.buildHeader(statusCode);

        logger.debug("Response\n" + response.getHeader() + new String(response.getFile()));
        return statusCode;
    }
}
