package threadPool;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by neikila on 21.10.15.
 */
public class Task implements Runnable {
    public AsynchronousSocketChannel connection;

    public Task(AsynchronousSocketChannel connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        // TODO read without limit
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        connection.read(buffer, buffer, null);
    }
}
