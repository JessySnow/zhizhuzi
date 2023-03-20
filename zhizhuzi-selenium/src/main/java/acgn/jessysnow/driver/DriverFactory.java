package acgn.jessysnow.driver;

import acgn.jessysnow.enums.Browsers;
import acgn.jessysnow.helper.UnsafeHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import sun.misc.Unsafe;

import java.lang.management.ManagementFactory;

/**
 * WebSelenium Driver Factory
 */
public class DriverFactory {
    private static final Unsafe unsafe;
    private static final int POOL_SIZE;
    private static GenericPool<EdgeDriver> edgeDriverPool;
    private static GenericPool<ChromeDriver> chromeDriverPool;
    private static GenericPool<SafariDriver> safariDriverPool;
    private static GenericPool<FirefoxDriver> fireFoxDriverPool;
    private static long edgeOffset;
    private static long chromeOffset;
    private static long safariOffset;
    private static long fireFoxOffset;

    static {
        unsafe = UnsafeHelper.getUnsafe();
        int MEM_SIZE = _MAX_SIZE_DRIVER_POOL();
        POOL_SIZE = MEM_SIZE > 4 ? (MEM_SIZE - 4) * 2 : 2;
        _INIT_OFFSET();
    }


    public static WebDriver buildDriver(Browsers type){
        WebDriver driver = null;
        switch (type) {
            case Edge -> {
                while (edgeDriverPool == null) {
                    unsafe.compareAndSwapObject(DriverFactory.class, edgeOffset, null,
                            new GenericPool<>(POOL_SIZE, (N) -> new EdgeDriver(), WebDriver::close));
                }
                return edgeDriverPool.borrowObject();
            }
            case Chrome -> {
                while (chromeDriverPool == null) {
                    unsafe.compareAndSwapObject(DriverFactory.class, chromeOffset, null,
                            new GenericPool<>(POOL_SIZE, (N) -> new ChromeDriver(), WebDriver::close));
                }
                return chromeDriverPool.borrowObject();
            }
            case Safari -> {
                while (safariDriverPool == null) {
                    unsafe.compareAndSwapObject(DriverFactory.class, safariOffset, null,
                            new GenericPool<>(POOL_SIZE, (N) -> new SafariDriver(), WebDriver::close));
                }
                return safariDriverPool.borrowObject();
            }
            case FireFox -> {
                while (fireFoxDriverPool == null) {
                    unsafe.compareAndSwapObject(DriverFactory.class, fireFoxOffset, null,
                            new GenericPool<>(POOL_SIZE, (N) -> new FirefoxDriver(), WebDriver::close));
                }
                return fireFoxDriverPool.borrowObject();
            }
        }

        return driver;
    }

    public static void releaseDriver(WebDriver driver){
        Class<? extends WebDriver> driverClass = driver.getClass();
        if (driverClass.equals(ChromeDriver.class)){
            chromeDriverPool.returnObject((ChromeDriver) driver);
        }else if (driverClass.equals(EdgeDriver.class)){
            edgeDriverPool.returnObject((EdgeDriver) driver);
        }else if (driverClass.equals(SafariDriver.class)){
            safariDriverPool.returnObject((SafariDriver) driver);
        }else{
            fireFoxDriverPool.returnObject((FirefoxDriver) driver);
        }
    }

    private static int _MAX_SIZE_DRIVER_POOL(){
        com.sun.management.OperatingSystemMXBean sysBean = (com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
        return (int) (sysBean.getTotalMemorySize() >> 30);
    }

    @SuppressWarnings("deprecation")
    private static void _INIT_OFFSET(){
        try {
            edgeOffset = unsafe.staticFieldOffset(DriverFactory.class.getDeclaredField("edgeDriverPool"));
            chromeOffset = unsafe.staticFieldOffset(DriverFactory.class.getDeclaredField("chromeDriverPool"));
            safariOffset = unsafe.staticFieldOffset(DriverFactory.class.getDeclaredField("safariDriverPool"));
            fireFoxOffset = unsafe.staticFieldOffset(DriverFactory.class.getDeclaredField("fireFoxDriverPool"));
        } catch (NoSuchFieldException ignored) {}
    }
}
