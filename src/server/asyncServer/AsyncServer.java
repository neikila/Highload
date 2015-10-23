package server.asyncServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.asyncServer.threadPool.Task;
import server.asyncServer.threadPool.ThreadPool;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

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
        this.poolSize = poolSize;
        this.rootDir = rootDir;
        threadPool = new ThreadPool(poolSize);
    }

    public void start() {
        try {
            listener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
            for (;true;) {
                Future temp = listener.accept();
                try {
                    AsynchronousSocketChannel clientChannel = (AsynchronousSocketChannel)temp.get();
                    threadPool.executeTask(new Task(clientChannel, rootDir));
                } catch (Exception e) {
                    logger.debug("Error");
                }
            }
        } catch (Exception e) {
            logger.error("Error while starting server");
            logger.error(e);
        }
    }
}
