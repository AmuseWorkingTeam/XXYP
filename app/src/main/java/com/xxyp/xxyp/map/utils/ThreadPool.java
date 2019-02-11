
package com.xxyp.xxyp.map.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPool {

    private static int TASK_COUNT = Runtime.getRuntime().availableProcessors() * 10 + 2;

    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(TASK_COUNT);

    private static int CORE_THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    private static ExecutorService mExecutor;

    private static HandlerThread childThread;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static Handler sIOHandler;

    static {
        childThread = new HandlerThread("ThreadPoolIO");
        childThread.start();
        Looper looper = childThread.getLooper();
        if(looper != null){
            sIOHandler = new Handler(looper);
            //线程池核心线程数基于内核数，最大线程数为5,线程空闲时间为30秒
            mExecutor = new ThreadPoolExecutor(CORE_THREAD_COUNT, Runtime.getRuntime().availableProcessors() + 1, 30, TimeUnit.SECONDS, workQueue, new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
                    sIOHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            execute(r);
                        }
                    }, 500);
                }
            });
        }
    }

    public static void execute(Runnable runnable) {
        mExecutor.execute(runnable);
    }

    public static Handler getIOThreadHandler() {
        return sIOHandler;
    }

    public static void postIOThreadDelayed(Runnable runnable, long delayed) {
        sIOHandler.postDelayed(runnable, delayed);
    }
    public static Future<?> submit(Runnable runnable) {
        return mExecutor.submit(runnable);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        return mExecutor.submit(callable);
    }

    public static Handler getMainHandler() {
        return sHandler;
    }

    public static void postDelayed(Runnable runnable, long delayed) {
        sHandler.postDelayed(runnable, delayed);
    }
}
