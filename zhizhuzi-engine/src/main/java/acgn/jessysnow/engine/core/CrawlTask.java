package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.helper.UAHelper;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.net.URI;
import java.net.URISyntaxException;

@Data
@Log4j2
public class CrawlTask{
    private String host;
    private int port;

    // Http header properties
    private URI uri;
    private HttpVersion httpVersion;
    private HttpMethod method;
    private String cookie;
    private String userAgent;

    public CrawlTask(String uri) {
        try {
            this.uri = new URI(uri);
            this.host = this.uri.getHost();
            this.port = uri.startsWith("https") ? 443 : 80;
            this.httpVersion = HttpVersion.HTTP_1_1;
            this.method = HttpMethod.GET;
            this.userAgent = UAHelper.getRandomUserAgent();
        }catch (URISyntaxException e){
            log.error(e);
        }
    }

    // Default HTTP_1_1
    public CrawlTask httpVersion(HttpVersion httpVersion){
        this.httpVersion = httpVersion;
        return this;
    }

    public CrawlTask get(){
        this.method = HttpMethod.GET;
        return this;
    }

    // TODO unsupported yet
    public CrawlTask post(){
        throw new UnsupportedOperationException();
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