package acgn.jessysnow.helper;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;

// FIXME A bad patch
@Log4j2
public final class GenericPool<T> {
    private final int minSize;
    private final Function<Void, T> constructor;
    private ConcurrentLinkedQueue<T> pool = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Thread> waitQueue = new ConcurrentLinkedQueue<>();

    public T borrowObject(){
        T res;
        // CAS failed
        while ((res = pool.poll()) == null){
            waitQueue.offer(Thread.currentThread());
            synchronized (Thread.currentThread()) {
                try {
                    log.info("{} --> wait", Thread.currentThread().getName());
                    Thread.currentThread().wait();
                } catch (InterruptedException ignored) {}
            }
        }
        return res;
    }

    // wake up waiting thread
    public void returnObject(T obj){
        this.pool.offer(obj);
        Thread waitThread = waitQueue.poll();
        if (waitThread != null){
            synchronized (waitThread){
                log.info("notify --> {}", waitThread.getName());
                waitThread.notify();
            }
        }
    }

    public GenericPool(int minSize, Function<Void, T> constructor, Consumer<T> closeObj){
        this.minSize = minSize;
        this.constructor = constructor;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (T t : pool){
                closeObj.accept(t);
            }
        }));
        initPool();
    }

    private void initPool(){
        this.pool = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < minSize; i++) {
            pool.offer(constructor.apply(null));
        }
    }
}
