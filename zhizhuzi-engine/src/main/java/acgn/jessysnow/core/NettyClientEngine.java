package acgn.jessysnow.core;

import acgn.jessysnow.http.HttpChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.net.ssl.SSLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Netty implement of Http client engine
 */
public class NettyClientEngine implements ClientEngine{
    // Cached bootstrap
    private final Bootstrap bootstrap;
    // Reactor thread pool size
    private final int poolSize;
    // Global Socket Option
    private final HashMap<ChannelOption<Boolean>, Boolean> optionSwitch;
    // HTTP options
    private boolean ssl;
    private boolean compress;


    private NettyClientEngine(int poolSize, boolean ssl, boolean compress,
                              HashMap<ChannelOption<Boolean>, Boolean> optionSwitch){
        this.bootstrap = new Bootstrap();
        this.optionSwitch = optionSwitch;
        this.poolSize = poolSize;
        this.ssl = ssl;
        this.compress = compress;

    }

    @Override
    public void boot() {
        try {
            this.bootstrap.group(new NioEventLoopGroup(poolSize))
                    .channel(NioSocketChannel.class)
                    .handler(new HttpChannelInitializer(ssl, compress, null));
            this.optionSwitch.entrySet().forEach(entry -> bootstrap.option(entry.getKey(), entry.getValue()));
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
            this.bootstrap.group(new NioEventLoopGroup(poolSize))
                    .channel(NioSocketChannel.class)
                    .handler(new HttpChannelInitializer(ssl, compress, strategy));
            this.optionSwitch.entrySet().forEach(entry -> bootstrap.option(entry.getKey(), entry.getValue()));
        } catch (SSLException e) {
            e.printStackTrace();
        }
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
            return new NettyClientEngine(Runtime.getRuntime().availableProcessors()
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
    }
}
