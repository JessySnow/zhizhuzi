package acgn.jessysnow.driver;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Log4j2
public final class GenericPool<T> implements Lock {
    private final Supplier<T> supplier;
    private final SharedSynchronizer synchronizer;

    // driver pool
    private final ConcurrentLinkedQueue<T> pool = new ConcurrentLinkedQueue<>();

    public T borrowObject() {
        lock();
        T obj = pool.poll();
        return obj == null ? supplier.get() : obj;
    }

    public void returnObject(T obj) {
        pool.offer(obj);
        unlock();
    }

    @Override
    public void lock() {
        synchronizer.acquireShared(1);
    }

    @Override
    public void unlock(){
        synchronizer.releaseShared(1);
    }

    @Override
    public void lockInterruptibly() {
        throw new UnsupportedOperationException("Unsupported so far");
    }

    @Override
    public boolean tryLock() {
        throw new UnsupportedOperationException("Unsupported so far");
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("Unsupported so far");
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Unsupported so far");
    }

    GenericPool(int size, Supplier<T> supplier, Consumer<T> clean) {
        this.supplier = supplier;
        this.synchronizer = new SharedSynchronizer(size);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.forEach(clean)));
    }

    /**
     * resources represents total resource count can borrow from pool
     */
    private static final class SharedSynchronizer extends AbstractQueuedSynchronizer{
        public SharedSynchronizer(int resources){
            super();
            setState(resources);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            for (;;){
                final int state = getState();
                final int newState = state - arg;
                if (newState < 0 || compareAndSetState(state, newState)){
                    return newState;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;){
                final int remain = getState();
                if (compareAndSetState(remain, remain + arg))
                    return true;
            }
        }
    }
}
