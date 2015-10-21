package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * Created by neikila on 21.10.15.
 */
public class AsyncServer {
    final private static Logger logger = LogManager.getLogger(AsyncServer.class.getName());
    private AsynchronousServerSocketChannel listener;
    private int port = 8081;
    private int poolSize = 1;

    public AsyncServer(int port, int poolSize) {
        this.port = port;
        this.poolSize = 1;
    }

    public void start() {
        try {
            listener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
        } catch (Exception e) {
            logger.error("Error while starting server");
        }
    }
}
