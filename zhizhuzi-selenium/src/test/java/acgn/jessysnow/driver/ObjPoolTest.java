package acgn.jessysnow.driver;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.lang.management.ManagementFactory;

public class ObjPoolTest {
    @Test
    public void testPool() throws InterruptedException {
        GenericPool<WebDriver> webDriverGenericPool = new GenericPool<>(1, (V) -> new EdgeDriver(),
                WebDriver::close);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                WebDriver driver = webDriverGenericPool.borrowObject();
                try {
                    driver.get("https://south-plus.org");
                    Thread.sleep(4096);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    webDriverGenericPool.returnObject(driver);
                }
            });
            thread.start();
            thread.join();
        }
    }

    @Test
    public void SystemInfoTest(){
        com.sun.management.OperatingSystemMXBean mxbean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalMemorySize = mxbean.getTotalMemorySize();
        System.out.println("System max memory size(GB): " + (totalMemorySize >> 30));
    }
}
