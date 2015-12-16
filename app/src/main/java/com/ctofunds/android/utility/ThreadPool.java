package com.ctofunds.android.utility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by qianhao.zhou on 12/16/15.
 */
public final class ThreadPool {

    private final ExecutorService backgroundTaskPool;

    private ThreadPool() {
        this.backgroundTaskPool = new ThreadPoolExecutor(0, 3,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    private static class SingletonHolder {
        private static ThreadPool instance = new ThreadPool();
    }

    public static ThreadPool getInstance() {
        return SingletonHolder.instance;
    }

    public void runOnBackground(Runnable runnable) {
        backgroundTaskPool.execute(runnable);
    }

}
