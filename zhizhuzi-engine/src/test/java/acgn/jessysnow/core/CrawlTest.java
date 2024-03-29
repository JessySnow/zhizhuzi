package acgn.jessysnow.core;

import acgn.jessysnow.common.core.pojo.CrawlTask;
import acgn.jessysnow.engine.core.CrawlEngineBuilder;
import acgn.jessysnow.engine.core.Engine;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CrawlTest {
    @Test
    public void executeTest() throws Exception {
        try(Engine<JDUrlSkus> engine =
                    new CrawlEngineBuilder<>(JDUrlSkus.class)
                            .ssl(true)
                            .compress(true)
                            .resConsumer(jdUrlSkus -> {
                                Assertions.assertEquals(jdUrlSkus.getUrlSkus().size(), 30);
                                Assertions.assertEquals(jdUrlSkus.getTask().getExtend(), jdUrlSkus.getTask()
                                        .getUri().toString());
                            })
                            .charSet("UTF-8")
                            .build()){

            engine.execute(new CrawlTask("https://search.jd.com/Search?keyword=GPW2")
                    .attach("https://search.jd.com/Search?keyword=GPW2"));
            engine.execute(new CrawlTask("https://search.jd.com/Search?keyword=G304")
                    .attach("https://search.jd.com/Search?keyword=G304"));
            Thread.sleep(2000);
        }
    }

    @Test
    public void blockTest() throws Exception {
        try(Engine<JDUrlSkus> engine =
                    new CrawlEngineBuilder<>(JDUrlSkus.class)
                            .ssl(true)
                            .compress(true)
                            .resConsumer(jdUrlSkus -> {
                                Assertions.assertEquals(jdUrlSkus.getUrlSkus().size(), 30);
                                Assertions.assertEquals(jdUrlSkus.getTask().getExtend(), jdUrlSkus.getTask()
                                        .getUri().toString());
                            })
                            .charSet("UTF-8")
                            .build()){

            engine.blockExecute(new CrawlTask("https://search.jd.com/Search?keyword=GPW2")
                    .attach("https://search.jd.com/Search?keyword=GPW2"));
            engine.blockExecute(new CrawlTask("https://search.jd.com/Search?keyword=G304")
                    .attach("https://search.jd.com/Search?keyword=G304"));
        }
    }

    @Test
    public void submitTest() throws Exception {
        try(Engine<JDUrlSkus> engine =
                    new CrawlEngineBuilder<>(JDUrlSkus.class)
                            .ssl(true)
                            .compress(true)
                            .resConsumer(WebsiteConsumer::toConsole)
                            .charSet("UTF-8")
                            .build()){

            JDUrlSkus result = engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=GPW2")
                    .attach("https://search.jd.com/Search?keyword=GPW2"));
            Assertions.assertEquals(30, result.getUrlSkus().size());
        }
    }
}