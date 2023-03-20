package acgn.jessysnow.driver;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;

@Log4j2
public final class GenericPool<T> {
    private final int size;
    private volatile int current = 0;
    private final Function<Void, T> constructor;

    // driver pool
    private final ConcurrentLinkedQueue<T> pool = new ConcurrentLinkedQueue<>();
    // mq
    private final ConcurrentLinkedQueue<Thread> waitQueue = new ConcurrentLinkedQueue<>();

    public T borrowObject(){
        T res;
        while ((res = pool.poll()) == null){
            if (current < size){

            }

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

    // FIXME use smaller range of supresswarning annonation
    @SuppressWarnings("all")
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

    GenericPool(int size, Function<Void, T> constructor, Consumer<T> closeObj){
        this.size = size;
        this.constructor = constructor;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.forEach(closeObj)));
    }
}
