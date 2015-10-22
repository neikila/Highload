package server.threadPool;

import handler.Method;
import handler.Request;
import handler.Response;
import handler.StatusCode;
import server.RequestHandler;
import server.ResponseWriter;

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
        // TODO read without limit
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
                        (request.getMethod().equals(Method.GET) ? response.getFile().length : 0);
                ByteBuffer bufferResponse = ByteBuffer.allocateDirect(size);
                bufferResponse.put(response.getHeader().getBytes());
                if (request.getMethod().equals(Method.GET)) {
                    bufferResponse.put(response.getFile());
                }
//                new ResponseWriter(connection).completed(0, bufferResponse);
                connection.write(bufferResponse, bufferResponse,
                        new ResponseWriter(connection));
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });
    }
}
