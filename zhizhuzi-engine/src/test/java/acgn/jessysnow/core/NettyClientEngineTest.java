package acgn.jessysnow.core;

import acgn.jessysnow.engine.core.NettyClientEngine;
import acgn.jessysnow.engine.pojo.CrawlTask;
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
     *                repeat : 2 times
     */
    private static final CrawlTask baiduTask;
    private static final CrawlTask taobaoTask;

    static {
        try {
            baiduTask = new CrawlTask("www.baidu.com", 80,
                    new URI("http://www.baidu.com/"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET,
                    null, null, null);

            taobaoTask =  new CrawlTask("www.taobao.com", 80,
                    new URI("http://www.taobao.com/"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET,
                    null, null, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    // FIXME SSL Engine 无法输出, Default Engine 正常输出
    @Test
    public void testOnDefaultNettyClientEngine(){
        try {
            NettyClientEngine nettyClientEngine = new NettyClientEngine
                    .NettyEngineBuilder()
                    .bootDefaultTestEngine(true);
            nettyClientEngine.execute(baiduTask);

            while (true){}
        }catch (Exception ignored){}
    }
}