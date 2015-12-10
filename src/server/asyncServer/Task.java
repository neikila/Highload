package server.asyncServer;

import handler.Method;
import handler.Request;
import handler.Response;
import handler.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.*;

/**
 * Created by neikila on 21.10.15.
 */
public class Task implements Runnable {
    private AsynchronousSocketChannel connection;
    private String rootDir;
    private Logger logger = LogManager.getLogger(Task.class.getName());

    public Task(AsynchronousSocketChannel connection, String rootDir) {
        this.connection = connection;
        this.rootDir = rootDir;
    }

    @Override
    public void run() {
        // TODO read without limit
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        Future<Integer> future = connection.read(byteBuffer);
        try {
            int nread = future.get(1, TimeUnit.SECONDS);
            byteBuffer.flip();
            byte[] buffer = new byte[byteBuffer.limit()];
            byteBuffer.get(buffer).clear();

            Request request = new Request(new String(buffer));
            Response response = new Response();

            StatusCode statusCode = RequestHandler.getResponse(request, response, rootDir);
            int size = response.getHeader().length() +
                    (statusCode.equals(StatusCode.OK) && request.getMethod().equals(Method.GET) ?
                            response.getFile().length : 0);
            ByteBuffer bufferResponse = ByteBuffer.allocateDirect(size);
            bufferResponse.put(response.getHeader().getBytes());
            if (statusCode.equals(StatusCode.OK) && request.getMethod().equals(Method.GET)) {
                bufferResponse.put(response.getFile());
            }
            bufferResponse.flip();
            future = connection.write(bufferResponse);
            future.get();
        } catch (InterruptedException e) {
            logger.error("Error while handling request.");
            logger.error(e);
        } catch (ExecutionException e) {
            logger.error("Error while handling request.");
            logger.error(e);
        } catch (TimeoutException e) {
            logger.error("Error while handling request.");
            logger.error(e);
        }
        try {
            connection.close();
        } catch (IOException e) {
            logger.error("Can't close connection");
        }
    }
}
