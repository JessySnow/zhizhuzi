package acgn.jessysnow.driver;

import acgn.jessysnow.enums.Browsers;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class DriverFactoryTest {
    @Test
    public void testDriverGetAndRelease() throws InterruptedException {
        Thread[] threads = buildThread(5, new Runnable() {
            @Override
            public void run() {
                WebDriver driver = DriverFactory.buildDriver(Browsers.Edge);
                doSomethingWithDriver(driver);
            }
        });

        for (Thread thread : threads) {
            thread.start();
        }
        Thread.sleep(4096 * 6);
    }

    private void doSomethingWithDriver(WebDriver driver){
        try {
            System.out.println(driver);
            Thread.sleep(4096);
            DriverFactory.releaseDriver(driver);
        } catch (InterruptedException ignored) {}
    }

    private Thread[] buildThread(int size, Runnable runnable){
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++){
            threads[i] = new Thread(runnable);
        }
        return threads;
    }
}