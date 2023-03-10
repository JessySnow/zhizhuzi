package acgn.jessysnow.core;

import acgn.jessysnow.common.pojo.CrawlTask;
import acgn.jessysnow.engine.core.CrawlEngine;
import acgn.jessysnow.engine.core.CrawlEngineBuilder;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import org.junit.jupiter.api.Test;

public class AttachObjectTest {
    @Test
    public void testAttachObject() {
        try (CrawlEngine<JDUrlSkus> engine =
                     new CrawlEngineBuilder<>(JDUrlSkus.class)
                             .ssl(true)
                             .compress(true)
                             .resConsumer(WebsiteConsumer::toConsole)
                             .charSet("UTF-8")
                             .build()) {

            JDUrlSkus gpw2 = engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=GPW2").attach("GPW2"));
            JDUrlSkus g304 = engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=G304").attach("G304"));
        }
    }
}