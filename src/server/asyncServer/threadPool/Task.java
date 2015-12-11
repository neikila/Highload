package server.asyncServer.threadPool;

import handler.Method;
import handler.Request;
import handler.Response;
import handler.StatusCode;
import server.asyncServer.RequestHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by neikila on 21.10.15.
 */
public class Task implements Runnable {
    private AsynchronousSocketChannel connection;
    private String rootDir;

    public Task(AsynchronousSocketChannel connection, String rootDir) {
        this.connection = connection;
        this.rootDir = rootDir;
    }

    @Override
    public void run() {
        // TODO read without limit
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        Future future = connection.read(buf);
        try {
            future.get(500, TimeUnit.MILLISECONDS);

            buf.flip();
            byte[] buffer = new byte[buf.limit()];
            buf.get(buffer).clear();

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
            Future result = connection.write(bufferResponse);
            result.get();
        } catch (Exception e) {
            System.err.println("To long writting");
        }
        try {
            connection.close();
        } catch (Exception e) {
            System.err.println("Error");
            System.err.println(e);
        }
    }
}
