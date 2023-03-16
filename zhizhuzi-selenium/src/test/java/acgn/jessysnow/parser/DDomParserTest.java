package acgn.jessysnow.parser;

import acgn.jessysnow.jsoup.sample.JDItem;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

public class DDomParserTest {
    private static final WebDriver driver = new EdgeDriver();
    private final DDomParser<JDItem> parser = new DDomParser<>();

    @Test
    public void parse(){
        driver.get("https://item.jd.com/100037299996.html");
        WebElement html = driver.findElements(By.tagName("html")).get(0);
        JDItem item = new JDItem();
        parser.parse(html, item);
        Assert.assertNotNull(item.getName());
        Assert.assertNotNull(item.getPrice());
    }


    @AfterClass
    public static void clear(){
        driver.close();
    }
}