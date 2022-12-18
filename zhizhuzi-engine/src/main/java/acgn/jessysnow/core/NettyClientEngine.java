package acgn.jessysnow.core;

import acgn.jessysnow.functions.PrintContentHandler;
import acgn.jessysnow.http.HttpChannelInitializer;
import acgn.jessysnow.pojo.CrawlTask;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Netty implement of Http client engine
 */
public class NettyClientEngine implements ClientEngine{
    // Cached bootstrap
    private final Bootstrap bootstrap;
    private final NioEventLoopGroup workGroup;
    // Reactor thread pool size
    private final int poolSize;
    // Global Socket Option
    private final HashMap<ChannelOption<Boolean>, Boolean> optionSwitch;
    // HTTP options
    private final boolean ssl;
    private final boolean compress;


    private NettyClientEngine(int poolSize, boolean ssl, boolean compress,
                              HashMap<ChannelOption<Boolean>, Boolean> optionSwitch){
        this.workGroup = new NioEventLoopGroup(poolSize);
        this.bootstrap = new Bootstrap();
        this.optionSwitch = optionSwitch;
        this.poolSize = poolSize;
        this.ssl = ssl;
        this.compress = compress;
    }

    @Override
    public void boot() {
        try {
            this.bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new HttpChannelInitializer(ssl, compress, new Consumer<ChannelHandlerContext>() {
                        @Override
                        public void accept(ChannelHandlerContext channelHandlerContext) {
                            channelHandlerContext.close();
                        }
                    }));
            if(this.optionSwitch != null) {
                this.optionSwitch.forEach(bootstrap::option);
            }
        } catch (SSLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Customize exception strategy
     * @param strategy exception strategy
     */
    @Override
    public void boot(Consumer<ChannelHandlerContext> strategy) {
        try {
            this.bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new HttpChannelInitializer(ssl, compress, strategy));
            if(this.optionSwitch != null) {
                this.optionSwitch.forEach(bootstrap::option);
            }
        } catch (SSLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Customize channel initializer
     * @param initializer channel initializer
     */
    @Override
    public void boot(ChannelInitializer<SocketChannel> initializer) {
        this.bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(initializer);
        if(this.optionSwitch != null) {
            this.optionSwitch.forEach(bootstrap::option);
        }
    }

    @Override
    public void execute(CrawlTask task) {
        if(this.bootstrap.config().group().isShutdown()){
            throw new RuntimeException("ClientEngine already closed");
        }

        // Flush back the request while tcp-connection is build
        bootstrap.connect(task.getHost(), task.getPort())
                .addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                DefaultFullHttpRequest request = new DefaultFullHttpRequest(task.getHttpVersion(), task.getMethod(),
                        task.getUri().toASCIIString());
                channelFuture.channel().writeAndFlush(request);
            }
        });
    }

    /**
     * Engine builder
     */
    public static class NettyEngineBuilder{
        private static final HashMap<ChannelOption<Boolean>, Boolean> SO_OP_MAP;
        static {
            SO_OP_MAP = new HashMap<>();
            SO_OP_MAP.put(ChannelOption.SO_KEEPALIVE, false);
            SO_OP_MAP.put(ChannelOption.TCP_NODELAY, true);
        }

        public NettyClientEngine buildDefaultEngine(){
            return new NettyClientEngine(Runtime.getRuntime().availableProcessors() * 2
                    , false
                    , true
                    , SO_OP_MAP);
        }

        public NettyClientEngine buildSSLEngine(){
            return new NettyClientEngine(Runtime.getRuntime().availableProcessors()  * 2
                    , true
                    , true
                    , SO_OP_MAP);
        }

        /**
         * only for unit test
         * @return a ClientEngine for junit test
         */
        public NettyClientEngine buildDefaultTestEngine() throws SSLException {
            NettyClientEngine engine = this.buildDefaultEngine();
            engine.boot(new HttpChannelInitializer(engine.ssl, engine.compress, new PrintContentHandler()));
            return engine;
        }
    }
}
