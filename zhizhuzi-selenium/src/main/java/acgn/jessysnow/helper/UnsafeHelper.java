package acgn.jessysnow.helper;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeHelper {
    public static Unsafe getUnsafe(){
        Unsafe unsafe;
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return unsafe;
    }
}
