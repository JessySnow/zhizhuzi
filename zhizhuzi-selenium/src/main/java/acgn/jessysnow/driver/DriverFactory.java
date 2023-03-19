package acgn.jessysnow.driver;

import acgn.jessysnow.enums.Browsers;
import acgn.jessysnow.helper.GenericPool;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.lang.management.ManagementFactory;

/**
 * WebSelenium Driver Factory
 */
public class DriverFactory {
    private static final int MEM_SIZE;
    private static GenericPool<EdgeDriver> edgeDriverPool;
    private static GenericPool<ChromeDriver> chromeDriverPool;
    private static GenericPool<SafariDriver> safariDriverPool;
    private static GenericPool<FirefoxDriver> fireFoxDriverPool;

    static {
        MEM_SIZE = _MAX_SIZE_DRIVER_POOL();
    }


    public static WebDriver buildDriver(Browsers type){
        WebDriver driver = null;
        switch (type){
            case Edge: {
                if (edgeDriverPool == null){
                }else {
                    return edgeDriverPool.borrowObject();
                }
                break;
            }
            case Chrome: {
                if (chromeDriverPool == null){

                }else {
                    return chromeDriverPool.borrowObject();
                }
                break;
            }
            case Safari: {
                if (safariDriverPool == null){

                }else {
                    return safariDriverPool.borrowObject();
                }
                break;
            }
            case FireFox: {
                if (fireFoxDriverPool == null){

                }else {
                    return fireFoxDriverPool.borrowObject();
                }
                break;
            }
        }

        return driver;
    }

    public static void releaseDriver(WebDriver driver){}

    private static int _MAX_SIZE_DRIVER_POOL(){
        com.sun.management.OperatingSystemMXBean sysBean = (com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
        return (int) (sysBean.getTotalMemorySize() >> 30);
    }
}
