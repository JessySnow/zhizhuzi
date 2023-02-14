package acgn.jessysnow.core;

import acgn.jessysnow.engine.core.NettyClientEngine;
import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import acgn.jessysnow.jsoup.sample.JDItem;
import acgn.jessysnow.jsoup.sample.JDItemSkus;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.jupiter.api.Test;

import java.net.URI;

public class JDCrawlTest {
    @Test
    public void test_urlList(){
        try(NettyClientEngine nettyClientEngine = new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(
                true, true, null, WebsiteConsumer::toConsole,
                JDUrlSkus.class)){
            nettyClientEngine.execute(new CrawlTask("search.jd.com", 443,
                    new URI("https://search.jd.com/Search?keyword=分形工艺"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
            Thread.sleep(5000);
        }catch (Exception ignored){;}
    }

    @Test
    public void test_itemList(){
        try(NettyClientEngine nettyClientEngine = new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(
                true, true, null, WebsiteConsumer::toConsole,
                JDItemSkus.class)){
            nettyClientEngine.execute(new CrawlTask("item.jd.com", 443,
                    new URI("https://item.jd.com/100048428239.html"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
            Thread.sleep(5000);
        }catch (Exception ignored){;}
    }

    // A blocking crawl request test
    @Test
    public void test_item(){
        try(NettyClientEngine nettyClientEngine = new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(
                true, true, null, WebsiteConsumer::toConsole,
                JDItem.class)){
            nettyClientEngine.blockExecute(new CrawlTask("item.jd.com", 443,
                    new URI("https://item.jd.com/100048428267.html"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
            nettyClientEngine.blockExecute(new CrawlTask("item.jd.com", 443,
                    new URI("https://item.jd.com/100048428267.html"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
        }catch (Exception ignored){;}
    }
}