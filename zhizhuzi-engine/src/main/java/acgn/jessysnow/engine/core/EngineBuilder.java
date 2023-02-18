package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.http.CrawlChannelInitializer;
import acgn.jessysnow.jsoup.pojo.WebSite;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class EngineBuilder<T extends WebSite> {
    private final NettyClientEngine<T> engine;
    private final Class<T> clazz;

    public EngineBuilder(Class<T> clazz){
        this.engine = new NettyClientEngine<>();
        this.clazz = clazz;
    }

    public NettyClientEngine<T> build(){
        CrawlChannelInitializer<T> initializer = new CrawlChannelInitializer<T>(
                engine.isSsl(), engine.isCompress(),
                engine.getExpConsumer(), engine.getResConsumer(), clazz, engine.getResultPipeline());
        engine.boot(initializer);
        return this.engine;
    }

    public EngineBuilder<T> ssl(boolean ssl){
        engine.setSsl(ssl);
        return this;
    }

    public EngineBuilder<T> compress(boolean compress){
        engine.setCompress(compress);
        return this;
    }

    public EngineBuilder<T> resultPipeline(ExecutorService resultPipeline){
        engine.setExecutorService(resultPipeline);
        return this;
    }

    public EngineBuilder<T> refactorPoolSize(int poolSize){
        engine.setRefactorPoolSize(poolSize);
        return this;
    }

    public EngineBuilder<T> soKeepAlive(){
        engine.soKeepAlive();
        return this;
    }

    public EngineBuilder<T> tcpNoDelay(){
        engine.TCPNoDelay();
        return this;
    }


    public EngineBuilder<T> expConsumer(Consumer<ChannelHandlerContext> expConsumer){
        this.engine.setExpConsumer(expConsumer);
        return this;
    }

    public EngineBuilder<T> resConsumer(Consumer<T> resConsumer){
        this.engine.setResConsumer(resConsumer);
        return this;
    }
}
