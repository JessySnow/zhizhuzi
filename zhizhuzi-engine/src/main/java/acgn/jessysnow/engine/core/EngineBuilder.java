package acgn.jessysnow.engine.core;

import java.util.concurrent.ExecutorService;

public class EngineBuilder {
    private final NettyClientEngine engine;

    public EngineBuilder(){
        this.engine = new NettyClientEngine();
    }

    public NettyClientEngine build(){
        return this.engine;
    }

    public EngineBuilder ssl(boolean ssl){
        engine.setSsl(ssl);
        return this;
    }

    public EngineBuilder compress(boolean compress){
        engine.setCompress(compress);
        return this;
    }

    public EngineBuilder resultPipeline(ExecutorService resultPipeline){
        engine.setExecutorService(resultPipeline);
        return this;
    }

    public EngineBuilder refactorPoolSize(int poolSize){
        engine.setRefactorPoolSize(poolSize);
        return this;
    }

    public EngineBuilder soKeepAlive(){
        engine.soKeepAlive();
        return this;
    }

    public EngineBuilder tcpNoDelay(){
        engine.TCPNoDelay();
        return this;
    }
}
