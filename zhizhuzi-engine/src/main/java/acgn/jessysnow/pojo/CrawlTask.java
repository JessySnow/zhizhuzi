package acgn.jessysnow.pojo;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import lombok.Data;

import java.net.URI;
import java.util.function.Consumer;

/**
 * Crawl task pojo, describes the parameters of the download task
 */
@Data
public class CrawlTask {
    private String host;
    private int port;

    // core info to build a request in netty
    private URI uri;
    private HttpVersion httpVersion;
    private HttpMethod method;

    private Consumer<DefaultFullHttpRequest> logic;

    public CrawlTask(String host,
                     int port,
                     URI uri,
                     HttpVersion httpVersion,
                     HttpMethod method,
                     Consumer<DefaultFullHttpRequest> logic) {
        this.host = host;
        this.port = port;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.method = method;
        this.logic = logic;
    }
}
