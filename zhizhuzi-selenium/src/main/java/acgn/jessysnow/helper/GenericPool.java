package acgn.jessysnow.helper;

import java.time.Duration;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

// TODO finish Simple Object pool
public final class GenericPool<T> {
    private final int maxSize, minSize;
    private final Function<Void, T> constructor;
    private final Duration cleanDuration;

    private ConcurrentLinkedQueue<T> pool;

    // TODO
    public T borrowObject(){
        return null;
    }

    // TODO
    public void returnObject(T obj){;}

    public GenericPool(int minSize, int maxSize, Function<Void, T> constructor, Duration cleanDuration){
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.constructor = constructor;
        this.cleanDuration = cleanDuration;
    }

    private void initPool(){
        this.pool = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < minSize; i++) {
            pool.add(constructor.apply(null));
        }
    }
}
