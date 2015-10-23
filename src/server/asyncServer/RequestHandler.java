package server.asyncServer;

import handler.Request;
import handler.Response;
import handler.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by neikila on 22.10.15.
 */
public class RequestHandler {
    private static Logger logger = LogManager.getLogger(RequestHandler.class.getName());

    public static StatusCode getResponse(Request request, Response response, String rootDir) {
        StatusCode statusCode;
        if (request.isCorrect()) {
            Path path = Paths.get(rootDir + request.getFilename()).normalize();
            logger.debug("filename: {}", path.toString());
            try {
                if (!path.startsWith(rootDir) || !path.toFile().isFile()) {
                    throw new IOException();
                }
                response.setFile(path.toString());
                switch (request.getMethod()) {
                    case GET:
                        response.readFile();
                    case HEAD:
                        response.countSize();
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
            // If there are any problems with file
//            catch (IllegalArgumentException e) {
//                logger.debug("Wrong media type.");
//                statusCode = StatusCode.UNSUPPORTED_MEDIA_TYPE;
//            }
            catch (IOException e) {
                if (request.getFilename().endsWith("/index.html")) {
                    logger.debug("Index file not Found.");
                    statusCode = StatusCode.FORBIDDEN;
                } else {
                    logger.debug("File not found.");
                    statusCode = StatusCode.NOT_FOUND;
                }
            }
        } else {
            // If request is not correct
            if (request.getMethod() == null) {
                statusCode = StatusCode.NOT_IMPLEMENTED;
            } else {
                statusCode = StatusCode.BAD_REQUEST;
            }
        }
        response.buildHeader(statusCode);
        logger.debug("Response\n" + response.getHeader() + (response.getFile() != null ? new String(response.getFile()) : ""));
        return statusCode;
    }
}
