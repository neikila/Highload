package server.asyncServer;

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
//        Path rootPath = Paths.get(rootDir);
        StatusCode statusCode;
        Method method = request.getMethod();
        String filename = rootDir + request.getFilename();
        Path path = Paths.get(filename).normalize();
        logger.debug("filename: {}", path.toString());
        try {
            if (!path.startsWith(rootDir)) {
                throw new IOException();
            }
            if (method != null) {
                response.setFile(path.toString());
                response.updateContentType();
                switch (method) {
                    case GET:
                        Files.getLastModifiedTime(path);
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
            } else {
                statusCode = StatusCode.NOT_IMPLEMENTED;
            }
        }
        catch (IOException e) {
            if (request.getFilename().endsWith("/index.html")) {
                logger.debug("Index file not Found.");
                statusCode = StatusCode.FORBIDDEN;
            } else {
                logger.debug("File not found.");
                statusCode = StatusCode.NOT_FOUND;
            }
        }
        response.buildHeader(statusCode);

        logger.debug("Response\n" + response.getHeader() + (statusCode.equals(StatusCode.OK) && method.equals(Method.GET)? new String(response.getFile()) : ""));
        return statusCode;
    }
}
