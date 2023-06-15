package git.tsunami047.tsoulbound;

import java.util.concurrent.*;

/**
 *@Author: natsumi
 *@CreateTime: 2023-06-01  18:16
 *@Description: 我的线程池
 */
public class MyThreadPool {

    private final ExecutorService executor;
    private final ExecutorService singleExecutor;
    public static final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    public static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    private volatile static MyThreadPool myThreadPool;

    public static CompletableFuture execute(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, getThreadPool().executor);
    }


    public static MyThreadPool getThreadPool(){
        if (myThreadPool == null) {
            synchronized (MyThreadPool.class) {
                if (myThreadPool == null) {
                    myThreadPool = new MyThreadPool();
                }
            }
        }
        return myThreadPool;
    }

    public static void close(){
        myThreadPool.executor.shutdown();
        myThreadPool.singleExecutor.shutdown();
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public ExecutorService getSingleExecutor() {
        return singleExecutor;
    }

    private MyThreadPool() {
        executor = Executors.newCachedThreadPool();
        singleExecutor = Executors.newSingleThreadExecutor();

    }

    public static void asyncExecute_s(Runnable runnable){
        getThreadPool().getSingleExecutor().execute(runnable);
    }

//    public static Future<?> asyncExecute_s(Callable<?> callable){
//        return getThreadPool().getSingleExecutor().submit(callable);
//    }

    public static void asyncExecute(Runnable runnable){
        getThreadPool().getExecutor().execute(runnable);
    }

    public static Future<?> asyncExecute(Callable<?> callable){
        return getThreadPool().getExecutor().submit(callable);
    }


}
