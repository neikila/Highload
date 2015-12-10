package server.blockingServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.threadPool.ThreadPool;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by neikila on 21.10.15.
 */
public class ServerBlocking {
    final private static Logger logger = LogManager.getLogger(ServerBlocking.class.getName());
    private int port = 8081;
    private int poolSize = 1;
    private ThreadPool threadPool;
    private String rootDir;

    public ServerBlocking(int port, int poolSize, String rootDir) {
        this.port = port;
        this.poolSize = poolSize;
        this.rootDir = rootDir;
        threadPool = new ThreadPool(poolSize);
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
//            ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize);
            for (;true;) {
                try {
                    Socket socket = serverSocket.accept();
//                    logger.debug("Accept");
                    threadPool.executeTask(new Task(socket, rootDir));
//                    threadPool.execute(new Task(socket, rootDir));
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
