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
            engine.blockExecute(new CrawlTask("https://search.jd.com/Search?keyword=GPW")
                    .appendCookie("__jdv=76161171|www.bing.com|-|referral|-|1676166393173; __jdu=16761663931721997646491; areaId=15; shshshfpa=7265f242-1cc1-2a25-6bfe-892a1ef422c5-1676166394; shshshfpb=rFEj3bizn1y2DKoSD2OS7tw; ipLoc-djd=15-1262-1267-56327; qrsc=3; PCSYCityID=CN_330000_330100_0; shshshfpx=7265f242-1cc1-2a25-6bfe-892a1ef422c5-1676166394; joyya=1676873441.1676873446.17.0but8nc; shshshfp=929cafbe80a099d0d0830b84f89bc962; rkv=1.0; ip_cityCode=1213; user-key=53246ee6-69ee-4a3d-b2b7-2758f3be61a4; cn=0; pinId=LONEqkAtezd1NFzJK7fOtA; pin=jd_iGHlNQUNtvum; unick=砍杀二次元; _tp=aUNtLaiP0Nmgi+jjhZD1Ng==; _pst=jd_iGHlNQUNtvum; wlfstk_smdl=zarpc1toxutgoqd4bkgkpeb269fte5gd; 3AB9D23F7A4B3C9B=EUHNEIMGNQXREPNNAHSZFJQR6JTCIDBUJVGCQPRYNWHGDXOLFP2KIKOQYXIC63O7EMUA6TKJUXMEWHEI6CYIQ7OOQ4; TrackID=1Y6g4OCgj_dLp94lX_xPbFOeHJbtPmiHDCl_NcQCiEvxEIuBX1tBOdu62ZiCgKql8SCFABNX7uF8QSWdPm4sjZns2qjX4JBXADiDhHjYj4BBC8clhwu4Sw2cKohzZy2r2; thor=D3833477BDFAB8FAFF4962A616C58F8C0FBC6CD3DA2A29FFA2656ADEA3FBC7170225508F3473D4F744A7E37BD2A0A51C9D698D1519A09A8C0EE58373251C45FCB2125A08A56F9700716136A4E47610DCBB21C3B54CE07F5DC915107F139C5C74E91AD067B271C95DD3616FE60022E301A51FB2F70505BF19EE9D66E7EA1FCDA02430F8BBDE6DB4AEFCCAD0748B9DA2522C8B685ADC23A09081DCE07325188035; ceshi3.com=103; __jda=76161171.16761663931721997646491.1676166393.1676903529.1676946604.16; __jdb=76161171.4.16761663931721997646491|16.1676946604; __jdc=76161171"));
        }catch (Exception ignored){;}
    }
}