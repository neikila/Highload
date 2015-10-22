package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.threadPool.Task;
import server.threadPool.ThreadPool;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by neikila on 21.10.15.
 */
public class AsyncServer {
    final private static Logger logger = LogManager.getLogger(AsyncServer.class.getName());
    private AsynchronousServerSocketChannel listener;
    private int port = 8081;
    private int poolSize = 1;
    private ThreadPool threadPool;
    private String rootDir;

    public AsyncServer(int port, int poolSize, String rootDir) {
        this.port = port;
        this.poolSize = 1;
        this.rootDir = rootDir;
        threadPool = new ThreadPool(poolSize);
    }

    public void start() {
        try {
            listener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
        } catch (Exception e) {
            logger.error("Error while starting server");
        }
        listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void> () {
            @Override
            public void completed(AsynchronousSocketChannel result, Void attachment) {
                threadPool.executeTask(new Task(result, rootDir));
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                logger.error(exc);
            }
        });
    }
}
