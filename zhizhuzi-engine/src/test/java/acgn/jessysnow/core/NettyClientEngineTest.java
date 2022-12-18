package acgn.jessysnow.core;

import acgn.jessysnow.pojo.CrawlTask;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.jupiter.api.Test;

import java.net.URI;

class NettyClientEngineTest {

    @Test
    public void testOnDefaultNettyClientEngine(){
        try {
            NettyClientEngine nettyClientEngine = new NettyClientEngine.NettyEngineBuilder().buildDefaultTestEngine();
            for (int i = 0; i < 10; i++) {
                nettyClientEngine.execute(new CrawlTask("www.baidu.com", 80, new URI("http://www.baidu.com"),
                        HttpVersion.HTTP_1_1, HttpMethod.GET, null));
                Thread.sleep(1000);
            }
        }catch (Exception ignored){}
    }
}