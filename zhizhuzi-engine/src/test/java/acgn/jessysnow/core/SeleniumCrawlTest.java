package acgn.jessysnow.core;

import acgn.jessysnow.common.core.pojo.CrawlTask;
import acgn.jessysnow.engine.core.CrawlEngineBuilder;
import acgn.jessysnow.engine.core.Engine;
import acgn.jessysnow.enums.Browsers;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SeleniumCrawlTest {
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
                            .browserType(Browsers.Edge)
                            .build()){
            new Thread(()->
            engine.execute(new CrawlTask("https://search.jd.com/Search?keyword=GPW2")
                    .attach("https://search.jd.com/Search?keyword=GPW2").dynamic(true)))
                    .start();
            new Thread(() ->
            engine.execute(new CrawlTask("https://search.jd.com/Search?keyword=G304")
                    .attach("https://search.jd.com/Search?keyword=G304").dynamic(true)))
                    .start();
            Thread.sleep(5000);
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
                            .browserType(Browsers.Edge)
                            .build()){

            engine.blockExecute(new CrawlTask("https://search.jd.com/Search?keyword=GPW2")
                    .attach("https://search.jd.com/Search?keyword=GPW2").dynamic(true));
            engine.blockExecute(new CrawlTask("https://search.jd.com/Search?keyword=G304")
                    .attach("https://search.jd.com/Search?keyword=G304").dynamic(true));
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
                            .browserType(Browsers.Edge)
                            .build()){

            JDUrlSkus result = engine.submit(new CrawlTask("https://search.jd.com/Search?keyword=GPW2")
                            .appendCookie("__jdu=16761663931721997646491; o2State={\"webp\":true,\"avif\":false}; shshshfpa=7265f242-1cc1-2a25-6bfe-892a1ef422c5-1676166394; shshshfpb=rFEj3bizn1y2DKoSD2OS7tw; shshshfpx=7265f242-1cc1-2a25-6bfe-892a1ef422c5-1676166394; pinId=LONEqkAtezd1NFzJK7fOtA; pin=jd_iGHlNQUNtvum; unick=砍杀二次元; _tp=aUNtLaiP0Nmgi+jjhZD1Ng==; _pst=jd_iGHlNQUNtvum; __jdv=76161171|direct|-|none|-|1678797150161; areaId=15; ipLoc-djd=15-1213-3038-59931; mt_xid=V2_52007VwMVVFpYVV0dQRtcA2QDGlpbX1ddGkEZbAYwBkFVWFpXRhZLSwgZYgJBBkELVw0WVR9YDWMAFgZZWQIOHHkaXQZlHxNQQVtbSx9IElkHbAMRYl1oUmofSBpUB2cDF1RZXWJYG0gc; PCSYCityID=CN_330000_330100_0; 3AB9D23F7A4B3CSS=jdd03EUHNEIMGNQXREPNNAHSZFJQR6JTCIDBUJVGCQPRYNWHGDXOLFP2KIKOQYXIC63O7EMUA6TKJUXMEWHEI6CYIQ7OOQ4AAAAMHFQ4VCCYAAAAACZ5M4QSX3E6OCMX; shshshfp=458a3398e4bcebe7f68543aaaba4dbbb; TrackID=1xo-Jhm1YKM38JLhS1LwUl2XS-Nnbjrj257xuEeGqgrcYvejL16G2HB0ONVkYlAYw92bpdbi5rZIz_6wCq5qWP9EU7I7Enlshe2BX2UvjYSvJIk3IfJmpzK9OMXs2cWgW; thor=D3833477BDFAB8FAFF4962A616C58F8C0FBC6CD3DA2A29FFA2656ADEA3FBC717DD72D7FBAAF86DDEC0757EBCE3183065FD016FFB3DF62324624350C684E5DDB57338D9B3A3FDDBF705AA9435986A5D0BC53DAB555BC2A228273FC6CE8AA1534B3E13BCB70366F7DCAE51FEAFB669C7737ABAA1F59402ABCF1CD81E92677B6972B9DB81B4D3581103B250E80FA1CF8FD9708565F46C6FA397DDB49382259FE4DE; ceshi3.com=103; __jda=76161171.16761663931721997646491.1676166393.1680073213.1680091190.69; __jdb=76161171.4.16761663931721997646491|69.1680091190; __jdc=76161171; shshshsID=255c16f3e1d7db7594a46a1acb034ffd_2_1680091202674; 3AB9D23F7A4B3C9B=EUHNEIMGNQXREPNNAHSZFJQR6JTCIDBUJVGCQPRYNWHGDXOLFP2KIKOQYXIC63O7EMUA6TKJUXMEWHEI6CYIQ7OOQ4")
                    .attach("https://search.jd.com/Search?keyword=GPW2").dynamic(true));
            Assertions.assertEquals(30, result.getUrlSkus().size());
        }
    }
}
