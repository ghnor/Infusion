package com.freer.infusion.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 2172980000774 on 2016/5/17.
 */
public class ThreadManager {

    // 线程安全，延迟加载
    private static class SingleHolder {
        private static final ThreadManager INSTANCE = new ThreadManager();
    }
    public static final ThreadManager getInstance() {
        return SingleHolder.INSTANCE;
    }
    private ThreadManager() {
        initThreadPool();
    }

    // A queue of Runnables
    private BlockingQueue<Runnable> mDecodeWorkQueue;
    // a thread pool manager
    private ThreadPoolExecutor mDecodeThreadPool;

    /*
     * Gets the number of available cores
     * (not always the same as the maximum number of cores)
     */
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    public void initThreadPool() {
        System.out.println("初始化线程池");
        System.out.println("核心线程数"+NUMBER_OF_CORES);
        // Instantiates the queue of Runnables as a LinkedBlockingQueue
        mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>(256);

        // Creates a thread pool manager
        mDecodeThreadPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES + 1,       // Initial pool size
                NUMBER_OF_CORES * 2 + 1,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mDecodeWorkQueue,
                new ThreadPoolExecutor.CallerRunsPolicy());
//        ExecutorService pool = Executors.newFixedThreadPool(NUMBER_OF_CORES);
    }

    public void addTask(Runnable runnable) {
        mDecodeThreadPool.execute(runnable);
    }

    // 关闭线程池
    public void closeThreadPool() {
        if (mDecodeThreadPool != null){
            mDecodeThreadPool.shutdownNow(); // 立刻关闭线程池
        }
    }

    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                // 核心改造点，由blockingqueue的offer改成put阻塞方法
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
