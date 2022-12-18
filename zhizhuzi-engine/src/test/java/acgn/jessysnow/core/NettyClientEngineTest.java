package acgn.jessysnow.core;

import acgn.jessysnow.pojo.CrawlTask;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;

class NettyClientEngineTest {

    @Test
    public void testOnDefaultNettyClientEngine(){
        try {
            NettyClientEngine nettyClientEngine = new NettyClientEngine.NettyEngineBuilder().buildDefaultTestEngine();
            nettyClientEngine.execute(new CrawlTask("www.baidu.com", 80, new URI("http://www.baidu.com"),
                    HttpVersion.HTTP_1_1, HttpMethod.GET, null));
            Thread.sleep(1000);
        } catch (URISyntaxException e) {
            System.out.println("Error in creating uri");
        } catch (InterruptedException | SSLException e) {
            throw new RuntimeException(e);
        }
    }
}