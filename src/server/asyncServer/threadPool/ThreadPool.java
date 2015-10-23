package server.asyncServer.threadPool;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by neikila on 21.10.15.
 */
public class ThreadPool {
    private ArrayList <Executor> executors = new ArrayList<>();
    private Queue <Runnable> queue = new LinkedBlockingQueue<>();

    public ThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; ++i) {
            Executor executor = new Executor(queue);
            executors.add(executor);
            executor.start();
        }
    }

    public void executeTask(Runnable task) {
        // TODO should it be synchronised
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }
}
