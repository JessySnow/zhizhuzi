package acgn.jessysnow.parser;

import acgn.jessysnow.helper.GenericPool;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

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
}
