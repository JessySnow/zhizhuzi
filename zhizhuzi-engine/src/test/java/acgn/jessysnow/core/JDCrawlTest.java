package acgn.jessysnow.core;

import acgn.jessysnow.engine.core.EngineBuilder;
import acgn.jessysnow.engine.core.NettyClientEngine;
import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import org.junit.jupiter.api.Test;

// Crawl item data-sku from search.jd.com
public class JDCrawlTest {
    @Test
    public void test_urlList(){
        try(NettyClientEngine<JDUrlSkus> engine = new EngineBuilder<JDUrlSkus>(JDUrlSkus.class).ssl(true)
                    .compress(true).resConsumer(WebsiteConsumer::toConsole).build();
        ){
            engine.blockExecute(new CrawlTask("https://search.jd.com/Search?keyword=分形工艺"));
        }catch (Exception ignored){;}
    }
}