package acgn.jessysnow.core;

import acgn.jessysnow.engine.core.CrawlEngine;
import acgn.jessysnow.engine.core.CrawlEngineBuilder;
import acgn.jessysnow.engine.core.CrawlTask;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import org.junit.jupiter.api.Test;

// Crawl item data-sku from search.jd.com
public class JDCrawlTest {
    @Test
    public void test_urlList(){
        try(CrawlEngine<JDUrlSkus> engine =
                    new CrawlEngineBuilder<>(JDUrlSkus.class)
                            .ssl(true)
                            .compress(true)
                            .resConsumer(WebsiteConsumer::toConsole)
                            .charSet("UTF-8")
                            .build()){
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    JDUrlSkus submit = engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=GPW"));
                    WebsiteConsumer.toConsole(submit);
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    JDUrlSkus submit = engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=GPW2"));
                    WebsiteConsumer.toConsole(submit);
                }
            });

            Thread t3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    JDUrlSkus submit = engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=RTX4090"));
                    WebsiteConsumer.toConsole(submit);
                }
            });

            t1.start();
            t2.start();
            t3.start();
        }catch (Exception ignored){;}
    }
}