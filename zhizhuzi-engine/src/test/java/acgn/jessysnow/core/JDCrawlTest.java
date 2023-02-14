package acgn.jessysnow.core;

import acgn.jessysnow.engine.core.NettyClientEngine;
import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.gson.sample.JDPriceAndName;
import acgn.jessysnow.jsoup.helper.WebsiteConsumer;
import acgn.jessysnow.jsoup.sample.JDItem;
import acgn.jessysnow.jsoup.sample.JDItemSkus;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

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

    @Test
    public void sslRepeatTest(){
        try (NettyClientEngine itemEngine = new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(
                true, true, null, WebsiteConsumer::toConsole,
                JDItem.class);
            NettyClientEngine skuEngine = new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(
                    true, true, null, WebsiteConsumer::toConsole,
                    JDUrlSkus.class);){

            itemEngine.blockExecute(new CrawlTask("item.jd.com", 443,
                    new URI("https://item.jd.com/100048428267.html"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
            skuEngine.blockExecute(new CrawlTask("search.jd.com", 443,
                    new URI("https://search.jd.com/Search?keyword=分形工艺"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
        }catch (Exception ignored){;}
    }

    @Test
    public void testAllInOne() throws InterruptedException, URISyntaxException {
        //
        Set<String> skuSet = new CopyOnWriteArraySet<>();

        NettyClientEngine itemInfoEngine = new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(true,
                true, null, System.out::println, JDPriceAndName.class);

        NettyClientEngine itemSkusEngine =
                new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(true,
                true, null,  skuPojo-> {
                    skuSet.addAll(skuPojo.getItemSkus());
                },JDItemSkus.class);

        NettyClientEngine entranceEngine = new NettyClientEngine.NettyEngineBuilder().getCrawlEngine(true,
                true, null, skuPojo -> {
                    skuSet.addAll(skuPojo.getUrlSkus());
                },JDUrlSkus.class);


        String itemName = "RTX4090";
        String searchUrl = "https://search.jd.com/Search?keyword=" + itemName;
        // get sku from search page
        entranceEngine.blockExecute(new CrawlTask("search.jd.com", 443,
                new URI(searchUrl),
                HttpVersion.HTTP_1_1, HttpMethod.GET
                ,null, null));

        // get sku in item-detail page
        for (String sku : skuSet){
            String itemUrl = "https://item.jd.com/" + sku + ".html";
            itemSkusEngine.blockExecute(new CrawlTask("item.jd.com", 443,
                    new URI(itemUrl),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
        }

        // get Item info
        for (String sku : skuSet){
            String itemInfoUrl = "https://item-soa.jd.com/getWareBusiness?skuId=" + sku;
            itemInfoEngine.blockExecute(new CrawlTask("item-soa.jd.com", 443,
                    new URI(itemInfoUrl),
                    HttpVersion.HTTP_1_1, HttpMethod.GET
                    ,null, null));
        }
    }
}