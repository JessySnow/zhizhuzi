package acgn.jessysnow.driver;

import acgn.jessysnow.enums.Browsers;

/**
 * TODO Pooling support
 * WebSelenium Driver pool
 */
public class DriverPool{
    private final int SIZE;
    private final Browsers type;

    public DriverPool(int size, Browsers type){
        this.SIZE = size;
        this.type = type;
    }

    private static class Worker{
    }
}
