package acgn.jessysnow.core;

import acgn.jessysnow.engine.core.CrawlEngine;
import acgn.jessysnow.engine.core.CrawlEngineBuilder;
import acgn.jessysnow.engine.pojo.CrawlTask;
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
            engine.blockExecute(new CrawlTask("https://search.jd.com/Search?keyword=GPW"));
        }catch (Exception ignored){;}
    }
}