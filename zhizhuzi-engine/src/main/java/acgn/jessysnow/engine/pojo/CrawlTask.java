package acgn.jessysnow.engine.pojo;

import acgn.jessysnow.engine.helper.UAHelper;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import lombok.Data;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Crawl task pojo, describes the parameters of the download task
 */
@Data
public class CrawlTask{
    private String host;
    private int port;

    // Http header properties
    private URI uri;
    private HttpVersion httpVersion;
    private HttpMethod method;
    private String cookie;
    private String userAgent;

    public CrawlTask(String uri) throws URISyntaxException {
        this.uri = new URI(uri);
        this.host = this.uri.getHost();
        this.port = uri.startsWith("https") ? 443 : 80;
        this.httpVersion = HttpVersion.HTTP_1_1;
        this.method = HttpMethod.GET;
        this.userAgent = UAHelper.getRandomUserAgent();
    }

    // Default HTTP_1_1
    public CrawlTask setHttpVersion(HttpVersion httpVersion){
        this.httpVersion = httpVersion;
        return this;
    }

    public CrawlTask get(){
        this.method = HttpMethod.GET;
        return this;
    }

    public CrawlTask post(){
        this.method = HttpMethod.POST;
        return this;
    }

    public CrawlTask appendCookie(String cookie){
        this.cookie = cookie;
        return this;
    }

    public CrawlTask useAgent(String userAgent){
        this.userAgent = userAgent;
        return this;
    }

    public CrawlTask port(int port){
        this.port = port;
        return this;
    }
}
