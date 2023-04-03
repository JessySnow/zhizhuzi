package acgn.jessysnow.driver;

import acgn.jessysnow.helper.UnsafeHelper;
import lombok.extern.log4j.Log4j2;
import sun.misc.Unsafe;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import java.util.function.Supplier;

// FIXME USE AQS
@Log4j2
public final class GenericPool<T> {
    private final int size;
    // updated by CAS
    private int current = 0;
    private long currentByteOffset;
    private final Supplier<T> supplier;
    private final Unsafe unsafe;

    // driver pool
    private final ConcurrentLinkedQueue<T> pool = new ConcurrentLinkedQueue<>();
    // mq
    private final ConcurrentLinkedQueue<Thread> waitQueue = new ConcurrentLinkedQueue<>();

    public T borrowObject() {
        T res;
        while ((res = pool.poll()) == null) {
            final int origin = current;
            if (origin < size) {
                if (unsafe.compareAndSwapInt(this, currentByteOffset, origin, origin + 1)) {
                    res = supplier.get();
                    break;
                }
            } else {
                waitQueue.offer(Thread.currentThread());
                LockSupport.park();
            }
        }
        return res;
    }

    public void returnObject(T obj) {
        this.pool.offer(obj);
        Thread waitThread = waitQueue.poll();
        if (waitThread != null) {
            LockSupport.unpark(waitThread);
        }
    }

    @SuppressWarnings("deprecation")
    GenericPool(int size, Supplier<T> supplier, Consumer<T> clean) {
        this.size = size;
        this.supplier = supplier;
        this.unsafe = UnsafeHelper.getUnsafe();

        try {
            currentByteOffset = unsafe.objectFieldOffset(this.getClass().getDeclaredField("current"));
        } catch (NoSuchFieldException ignored) {}

        Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.forEach(clean)));
    }
}
