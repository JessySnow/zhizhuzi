package acgn.jessysnow.driver;

import acgn.jessysnow.enums.Browsers;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.*;

public class DriverFactoryTest {
    @Test
    public void testDriverGetAndRelease(){
        WebDriver driver = DriverFactory.buildDriver(Browsers.Edge);
        DriverFactory.releaseDriver(driver);
    }
}