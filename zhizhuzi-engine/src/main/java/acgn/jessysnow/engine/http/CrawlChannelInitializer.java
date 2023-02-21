package acgn.jessysnow.engine.http;

import acgn.jessysnow.engine.core.CrawlHandler;
import acgn.jessysnow.engine.core.WebSiteConverter;
import acgn.jessysnow.engine.helper.SslHelper;
import acgn.jessysnow.jsoup.pojo.WebSite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Log4j2
public class CrawlChannelInitializer<T extends WebSite> extends HttpChannelInitializer {
    private final Consumer<T> consumerLogic;
    private final Class<T> clazz;
    private final ExecutorService resultPipeline;
    private final String charSet;

    /**
     * Replace BizHandler --> CrawlHandler
     * @param ssl ssl support
     * @param compress http-compress
     * @param strategy exception handler
     * @param consumerLogic logic to consume result item
     * @param clazz WebSite class
     * @param resultPipeline executor service based result pipeline
     */
    public CrawlChannelInitializer(boolean ssl, boolean compress, String charSet,Consumer<ChannelHandlerContext> strategy,
                                   Consumer<T> consumerLogic, Class<T> clazz, ExecutorService resultPipeline){
        super(ssl, compress, null, strategy);
        this.consumerLogic = consumerLogic;
        this.clazz = clazz;
        this.resultPipeline = resultPipeline;
        this.charSet = charSet;
    }

    /**
     * SslHandler(Optional) --> HttpCodec --> Http-Aggregator --> Http-Decompressor(Optional) --> WebSiteConverter
     *          --> CrawlHandler --> ExceptionHandler
     */
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
        ch.pipeline().addLast(new WebSiteConverter<T>(clazz, charSet))
                .addLast(new CrawlHandler<T>(resultPipeline, consumerLogic))
                .addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        synchronized (ctx.channel()){
                            ctx.channel().notifyAll();
                        }

                        if(strategy == null){
                            log.error(cause);
                        }else {
                            strategy.accept(ctx);
                        }

                        if(ctx.channel().isOpen()){
                            ctx.channel().close();
                        }
                    }
                });
    }
}
