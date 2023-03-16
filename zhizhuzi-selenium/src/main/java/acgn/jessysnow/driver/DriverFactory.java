package acgn.jessysnow.driver;

import acgn.jessysnow.enums.Browsers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

/**
 * FIXME Pooling support
 * WebSelenium Driver Factory
 */
public class DriverFactory {
    DriverFactory(){}

    public WebDriver buildDriver(Browsers type){
        WebDriver driver;
        switch (type){
            case Edge -> driver = new EdgeDriver();
            case Chrome -> driver = new ChromeDriver();
            case Safari -> driver = new SafariDriver();
            case FireFox -> driver = new FirefoxDriver();
            default -> driver = null;
        }
        return driver;
    }
}
