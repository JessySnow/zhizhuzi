package acgn.jessysnow.core;

import acgn.jessysnow.engine.core.NettyClientEngine;
import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import acgn.jessysnow.jsoup.sample.RTX3060Ti;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
class NettyClientEngineTest {

    /**
     * Download test, target : www.baidu.com
     */
    private static final CrawlTask baiduTask;
    private static final CrawlTask taobaoTask;
    private static final CrawlTask jdSearchTask;

    static {
        try {
            baiduTask = new CrawlTask("www.baidu.com", 443,
                    new URI("https://www.baidu.com/"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET,
                    null, null);

            taobaoTask =  new CrawlTask("www.taobao.com", 443,
                    new URI("https://www.taobao.com/"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET,
                    null, null);
            jdSearchTask = new CrawlTask("search.jd.com", 443,
                    new URI("https://search.jd.com/Search?keyword=分形工艺"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testOnDefaultNettyClientEngine(){
        try (NettyClientEngine nettyClientEngine = new NettyClientEngine
                    .NettyEngineBuilder()
                    .getDefaultTestEngine(true);){
//            nettyClientEngine.execute(taobaoTask);
//            nettyClientEngine.execute(baiduTask);
            nettyClientEngine.execute(jdSearchTask);
            Thread.sleep(5000);
        }catch (Exception ignored){}
    }

    @Test
    public void testOnCrawlTask(){
        try(NettyClientEngine nettyClientEngine = new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(
                true, true, null, WebsiteConsumer::toConsole,
                RTX3060Ti.class)){
            nettyClientEngine.execute(jdSearchTask);
            Thread.sleep(5000);
        }catch (Exception ignored){;}
    }

    @Test
    public void testOnJsonCrawlTask(){

    }
}