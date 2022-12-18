package acgn.jessysnow.core;

import acgn.jessysnow.pojo.CrawlTask;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.URI;

@Log4j2
class NettyClientEngineTest {

    /**
     * Download test, target : www.baidu.com
     *                repeat : 2 times
     */
    @Test
    public void testOnDefaultNettyClientEngine(){
        try {
            NettyClientEngine nettyClientEngine = new NettyClientEngine.NettyEngineBuilder().buildDefaultTestEngine();
            for (int i = 0; i < 2; i++) {
                nettyClientEngine.execute(new CrawlTask("www.baidu.com", 80, new URI("http://www.baidu.com"),
                        HttpVersion.HTTP_1_1, HttpMethod.GET, null));
                Thread.sleep(1000);
            }
        }catch (Exception ignored){}
    }
}