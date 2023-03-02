package acgn.jessysnow.engine.core;

import acgn.jessysnow.jsoup.pojo.WebSite;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * A Netty-Inbound handler work with NettyClientEngine
 *      - Consume the website-pojo get from pipeline
 * @see CrawlEngine
 */
@ChannelHandler.Sharable
public class CrawlHandler<T extends WebSite> extends ChannelInboundHandlerAdapter {

    private final ExecutorService resultPipeline;
    private final Consumer<T> consumeLogic;

    public CrawlHandler(ExecutorService resultPipeline, Consumer<T> consumeLogic){
        this.resultPipeline = resultPipeline;
        this.consumeLogic = consumeLogic;
    }

    /**
     * This handler is placed behind a message2message_encoder, so it will get a Website from pipeline
     *      - Cause consumeLogic will be a time-consuming operation, submit it to an executor-service
     * Notify execute thread to release this channels
     * @param msg A Website-pojo
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        AttributeKey<Object> futureKey = AttributeKey.valueOf("futureKey");
        Attribute<Object> attribute = ctx.channel().attr(futureKey);
        T website = (T) msg;
        attribute.set(website);

        resultPipeline.execute(() -> {
            consumeLogic.accept(website);
            synchronized (ctx.channel()){
                ctx.channel().notifyAll();
            }
        });
    }
}
