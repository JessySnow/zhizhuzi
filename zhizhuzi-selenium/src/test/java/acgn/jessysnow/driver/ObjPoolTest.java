package acgn.jessysnow.driver;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.lang.management.ManagementFactory;

public class ObjPoolTest {
    @Test
    public void testPool() throws InterruptedException {
        GenericPool<WebDriver> webDriverGenericPool = new GenericPool<>(3, EdgeDriver::new, WebDriver::close);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                WebDriver driver = webDriverGenericPool.borrowObject();
                try {
                    driver.get("https://south-plus.org");
                    Thread.sleep(4096);
                    System.out.println("Here");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    webDriverGenericPool.returnObject(driver);
                }
            });
            thread.start();
        }
        Thread.sleep(4096 * 11);
    }

    // Show host's physical memory size
    @Test
    public void SystemInfoTest(){
        long totalMemorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean())
                .getTotalMemorySize() >> 30;

        System.out.println(totalMemorySize);
        Assert.assertTrue(totalMemorySize > 0 && totalMemorySize < 100);
    }
}
