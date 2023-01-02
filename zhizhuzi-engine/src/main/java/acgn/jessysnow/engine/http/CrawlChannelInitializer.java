package acgn.jessysnow.engine.http;

import acgn.jessysnow.engine.core.CrawlHandler;
import acgn.jessysnow.engine.helper.SslHelper;
import acgn.jessysnow.jsoup.pojo.WebSite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;

import javax.net.ssl.SSLException;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class CrawlChannelInitializer<T extends WebSite> extends HttpChannelInitializer {
    private Consumer<T> consumerLogic;
    private Class<T> clazz;
    private ExecutorService resultPipeline;

    public CrawlChannelInitializer(boolean ssl, boolean compress) throws SSLException {
        super(ssl, compress);
    }

    public CrawlChannelInitializer(boolean ssl, boolean compress, Consumer<ChannelHandlerContext> strategy) throws SSLException {
        super(ssl, compress, strategy);
    }

    public CrawlChannelInitializer(boolean ssl, boolean compress, ChannelInboundHandlerAdapter bizHandler) throws SSLException {
        super(ssl, compress, bizHandler);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        if(ssl){
            ch.pipeline().addFirst("http-ssl", SslHelper.getHandler());
        }
        ch.pipeline().addLast("http-codec", new HttpClientCodec())
                .addLast("http-aggregator", new HttpObjectAggregator(524288));
        if(compress){
            ch.pipeline().addLast("http-decompressor", new HttpContentDecompressor());
        }
    }
}
