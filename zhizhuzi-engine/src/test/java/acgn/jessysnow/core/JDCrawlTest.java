package acgn.jessysnow.core;

import acgn.jessysnow.engine.core.CrawlEngine;
import acgn.jessysnow.engine.core.CrawlEngineBuilder;
import acgn.jessysnow.engine.core.CrawlInfo;
import acgn.jessysnow.common.pojo.CrawlTask;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import org.junit.jupiter.api.Test;

// Crawl item data-sku from search.jd.com
public class JDCrawlTest {
    @Test
    public void test_urlList() throws InterruptedException {
        try(CrawlEngine<JDUrlSkus> engine =
                    new CrawlEngineBuilder<>(JDUrlSkus.class)
                            .ssl(true)
                            .compress(true)
                            .resConsumer(WebsiteConsumer::toConsole)
                            .charSet("UTF-8")
                            .build()){

            Thread t1 = new Thread(() -> engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=GPW2")));
            Thread t2 = new Thread(() -> engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=G304")));
            t1.start();
            t2.start();

            t1.join();
            t2.join();
        }
    }

    @Test
    public void test_attr_key() throws InterruptedException {
        try(CrawlEngine<JDUrlSkus> engine =
                    new CrawlEngineBuilder<>(JDUrlSkus.class)
                            .ssl(true)
                            .compress(true)
                            .resConsumer(WebsiteConsumer::toConsole)
                            .charSet("UTF-8")
                            .build()){

            CrawlInfo<JDUrlSkus> res1 = engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=G304"));
            CrawlInfo<JDUrlSkus> res2 = engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=GPW2"));
            Thread.currentThread().join();
        }
    }
}