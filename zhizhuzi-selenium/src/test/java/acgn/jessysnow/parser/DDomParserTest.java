package acgn.jessysnow.parser;

import acgn.jessysnow.jsoup.sample.JDItem;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

public class DDomParserTest {
    private static final WebDriver driver = new EdgeDriver();

    @Test
    public void parseSingle(){
        driver.get("https://item.jd.com/100037299996.html");
        WebElement html = driver.findElements(By.tagName("html")).get(0);
        JDItem item = new JDItem();
        DDomParser<JDItem> parser = new DDomParser<>();
        parser.parse(html, item);
        Assert.assertNotNull(item.getName());
        Assert.assertNotNull(item.getPrice());
    }

    @Test
    public void parseMulti(){
        driver.get("https://search.jd.com/Search?keyword=GPW2");
        WebElement html = driver.findElements(By.tagName("html")).get(0);
        JDUrlSkus skus = new JDUrlSkus();
        new DDomParser<JDUrlSkus>().parse(html, skus);
        Assert.assertNotNull(skus.getUrlSkus());
    }


    @AfterClass
    public static void clear(){
        driver.close();
    }
}