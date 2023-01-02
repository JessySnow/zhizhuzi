package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.helper.TestHandler;
import acgn.jessysnow.engine.helper.UAHelper;
import acgn.jessysnow.engine.http.HttpChannelInitializer;
import acgn.jessysnow.engine.pojo.CrawlTask;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import lombok.extern.log4j.Log4j2;

import javax.net.ssl.SSLException;
import java.util.HashMap;

/**
 * Netty implement of Http client engine
 */
@Log4j2
public class NettyClientEngine implements ClientEngine{
    // Cached bootstrap
    private final Bootstrap bootstrap;
    private final NioEventLoopGroup workGroup;
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
        this.ssl = ssl;
        this.compress = compress;
    }

    // Close engine
    @Override
    public void close(){
        this.workGroup.shutdownGracefully();
    }

    @Override
    public void boot(ChannelInitializer<SocketChannel> initializer) {
        this.bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(initializer);
        this.optionSwitch.forEach(bootstrap::option);
    }

    @Override
    public void execute(CrawlTask task) {
        if(this.bootstrap.config().group().isShutdown()){
            throw new IllegalStateException("ClientEngine already closed");
        }

        // Flush back the request while tcp-connection is build
        bootstrap.connect(task.getHost(), task.getPort())
                .addListener((ChannelFutureListener) channelFuture -> {
                    DefaultFullHttpRequest request = new DefaultFullHttpRequest(task.getHttpVersion(), task.getMethod(),
                            task.getUri().toASCIIString());
                    // Host
                    request.headers().set(HttpHeaderNames.HOST, task.getHost());
                    // UA
                    request.headers().set(HttpHeaderNames.USER_AGENT,
                            task.getUserAgent() == null || task.getUserAgent().isBlank()
                                    ? UAHelper.getRandomUserAgent()
                                    : task.getUserAgent());
                    // Cookies
                    if(task.getCookie() != null && !task.getCookie().isBlank()){
                       request.headers().set(HttpHeaderNames.COOKIE, task.getCookie());
                    }

                    channelFuture.channel().writeAndFlush(request);
                });
    }

    /**
     * Engine builder
     */
    public static class NettyEngineBuilder{

        private static final HashMap<ChannelOption<Boolean>, Boolean> SO_OP_MAP = new HashMap<>();
        static {
            SO_OP_MAP.put(ChannelOption.SO_KEEPALIVE, true);
            SO_OP_MAP.put(ChannelOption.TCP_NODELAY, true);
        }

        /**
         * Default engine
         *  - Reactor pool size : 2 * processor's number
         *  - ssl : false, no ssl handler in pipeline
         *  - compress : true
         *  - Socket-KeepAlive : false
         *  - TCP-NoDelay : true
         * @return client engine have not booted
         */
        public NettyClientEngine buildDefaultEngine(){
            return new NettyClientEngine(Runtime.getRuntime().availableProcessors() * 2
                    , false
                    , true
                    , SO_OP_MAP);
        }

        /**
         * SSL engine
         *  - Reactor pool size : 2 * processor's number
         *  - ssl : true, ssl handler in the head of pipeline
         *  - compress : true
         *  - Socket-KeepAlive : false
         *  - TCP-NoDelay : true
         * @return client engine have not booted
         */
        public NettyClientEngine buildSSLEngine(){
            return new NettyClientEngine(Runtime.getRuntime().availableProcessors()  * 2
                    , true
                    , true
                    , SO_OP_MAP);
        }

        /**
         * only for unit test, pre-boot
         *      print http-response's content to console
         * @return a ClientEngine for junit test
         */
        public NettyClientEngine getPreBooteeDefaultTestEngine(boolean ssl) throws SSLException {
            NettyClientEngine engine;
            if(!ssl) {
                engine = this.buildDefaultEngine();
            }else {
                engine = this.buildSSLEngine();
            }
            engine.boot(new HttpChannelInitializer(engine.ssl, engine.compress, new TestHandler()));
            return engine;
        }
    }
}
