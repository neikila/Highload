package server.threadPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;

/**
 * Created by neikila on 21.10.15.
 */
public class Executor extends Thread {
    private static Logger logger = LogManager.getLogger(Executor.class.getName());

    private final Queue taskQueue;

    Executor(Queue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run() {
        Runnable task;
        while (true) {
            while (taskQueue.isEmpty()) {
                try {
                    taskQueue.wait();
                } catch (InterruptedException e) {
                    logger.debug("Interrupted");
                }
            }
            task = (Runnable) taskQueue.poll();
            try {
                task.run();
            } catch (RuntimeException e) {
                logger.debug("Error while running task.");
            }
        }
    }
}