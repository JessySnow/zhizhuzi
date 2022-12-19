package acgn.jessysnow.pojo;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import lombok.Data;

import java.net.URI;
import java.util.function.Consumer;

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

    // Defines the logic to consume a full http response
    private Consumer<DefaultFullHttpResponse> logic;

    public CrawlTask(String host,
                     int port,
                     URI uri,
                     HttpVersion httpVersion,
                     HttpMethod method,
                     String cookie,
                     String userAgent,
                     Consumer<DefaultFullHttpResponse> logic) {
        this.host = host;
        this.port = port;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.method = method;
        this.cookie = cookie;
        this.userAgent = userAgent;
        this.logic = logic;
    }

    // Inherit info from a parent craw_task, uri is specified by its own
    public CrawlTask(CrawlTask parent, URI uri){
        this.host = parent.host;
        this.port = parent.port;
        this.httpVersion = parent.httpVersion;
        this.method = parent.method;
        this.cookie = parent.cookie;
        this.userAgent = parent.userAgent;
        this.logic = parent.logic;
        this.uri = parent.uri;
    }
}
