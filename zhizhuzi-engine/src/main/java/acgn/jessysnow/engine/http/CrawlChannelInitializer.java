package acgn.jessysnow.engine.http;

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
    private final Consumer<T> consumerLogic;
    private final Class<T> clazz;
    private final ExecutorService resultPipeline;

    /**
     * BizHandler --> CrawlHandler
     * @param ssl ssl support
     * @param compress http-compress
     * @param strategy exception handler
     * @param consumerLogic logic to consume result item
     * @param clazz WebSite class
     * @param resultPipeline executor service based result pipeline
     */
    public CrawlChannelInitializer(boolean ssl, boolean compress, Consumer<ChannelHandlerContext> strategy,
                                   Consumer<T> consumerLogic, Class<T> clazz, ExecutorService resultPipeline){
        super(ssl, compress, null, strategy);
        this.consumerLogic = consumerLogic;
        this.clazz = clazz;
        this.resultPipeline = resultPipeline;
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
