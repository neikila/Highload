package server.asyncServer.threadPool;

import handler.Method;
import handler.Request;
import handler.Response;
import handler.StatusCode;
import server.asyncServer.RequestHandler;
import server.asyncServer.ResponseWriter;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

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
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        connection.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                attachment.flip();
                byte[] buffer = new byte[attachment.limit()];
                attachment.get(buffer).clear();

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
                connection.write(bufferResponse, bufferResponse,
                        new ResponseWriter(connection));
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });
    }
}
