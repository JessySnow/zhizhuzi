package acgn.jessysnow.driver;

import acgn.jessysnow.helper.UnsafeHelper;
import lombok.extern.log4j.Log4j2;
import sun.misc.Unsafe;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Function;

@Log4j2
public final class GenericPool<T> {
    private final int size;
    private int current = 0;
    private long currentByteOffset;
    private final Function<Void, T> constructor;
    private final Consumer<T> closeObj;
    private final Unsafe unsafe;

    // driver pool
    private final ConcurrentLinkedQueue<T> pool = new ConcurrentLinkedQueue<>();
    // mq
    private final ConcurrentLinkedQueue<Thread> waitQueue = new ConcurrentLinkedQueue<>();

    public T borrowObject(){
        T res;
        // CAS
        while ((res = pool.poll()) == null){
            final int origin = current;
            if (origin < size){
                if (unsafe.compareAndSwapInt(this, currentByteOffset, origin, origin + 1)){
                    res = constructor.apply(null);
                    break;
                }
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
        while (current >= size){
            int origin = current;
            if (unsafe.compareAndSwapInt(this, currentByteOffset, origin, origin - 1)){
                closeObj.accept(obj);
            }
        }

        this.pool.offer(obj);
        Thread waitThread = waitQueue.poll();
        if (waitThread != null){
            synchronized (waitThread){
                log.info("notify --> {}", waitThread.getName());
                waitThread.notify();
            }
        }
    }

    @SuppressWarnings("deprecation")
    GenericPool(int size, Function<Void, T> constructor, Consumer<T> closeObj){
        this.size = size;
        this.constructor = constructor;
        this.closeObj = closeObj;
        this.unsafe = UnsafeHelper.getUnsafe();
        try {
            currentByteOffset = unsafe.objectFieldOffset(this.getClass().getDeclaredField("current"));
        } catch (NoSuchFieldException ignored) {;}
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.forEach(closeObj)));
    }
}
