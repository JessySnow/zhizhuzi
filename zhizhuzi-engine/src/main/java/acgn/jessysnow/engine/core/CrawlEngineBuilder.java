package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.initializer.CrawlChannelInitializer;
import acgn.jessysnow.jsoup.pojo.WebSite;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class CrawlEngineBuilder<T extends WebSite> {
    private final CrawlEngine<T> engine;
    private final Class<T> clazz;

    public CrawlEngineBuilder(Class<T> clazz){
        this.engine = new CrawlEngine<>();
        this.clazz = clazz;
    }

    public CrawlEngine<T> build(){
        CrawlChannelInitializer<T> initializer = new CrawlChannelInitializer<T>(
                engine.isSsl(), engine.isCompress(), engine.getCharSet(),
                engine.getExpConsumer(), engine.getResConsumer(), clazz, engine.getResultPipeline());
        engine.boot(initializer);
        return this.engine;
    }

    public CrawlEngineBuilder<T> ssl(boolean ssl){
        engine.setSsl(ssl);
        return this;
    }

    public CrawlEngineBuilder<T> compress(boolean compress){
        engine.setCompress(compress);
        return this;
    }

    public CrawlEngineBuilder<T> charSet(String charSet){
        engine.setCharSet(charSet);
        return this;
    }

    public CrawlEngineBuilder<T> resultPipeline(ExecutorService resultPipeline){
        engine.setExecutorService(resultPipeline);
        return this;
    }

    public CrawlEngineBuilder<T> refactorPoolSize(int poolSize){
        engine.setRefactorPoolSize(poolSize);
        return this;
    }

    public CrawlEngineBuilder<T> soKeepAlive(){
        engine.soKeepAlive();
        return this;
    }

    public CrawlEngineBuilder<T> tcpNoDelay(){
        engine.TCPNoDelay();
        return this;
    }


    public CrawlEngineBuilder<T> expConsumer(Consumer<ChannelHandlerContext> expConsumer){
        this.engine.setExpConsumer(expConsumer);
        return this;
    }

    public CrawlEngineBuilder<T> resConsumer(Consumer<T> resConsumer){
        this.engine.setResConsumer(resConsumer);
        return this;
    }
}
