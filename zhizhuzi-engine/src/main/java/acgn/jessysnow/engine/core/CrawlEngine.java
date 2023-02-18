package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.helper.UAHelper;
import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.pojo.WebSite;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Netty implement of Http client engine
 */
@Log4j2
public class CrawlEngine<T extends WebSite> implements ClientEngine{

    // Cached bootstrap
    private final Bootstrap bootstrap = new Bootstrap();
    private NioEventLoopGroup workGroup;
    // Global Socket Option
    private final HashMap<ChannelOption<Boolean>, Boolean> optionSwitch = new HashMap<>();
    // HTTP options
    @Getter
    private boolean ssl;
    @Getter
    private boolean compress;
    // Executor service based pipeline
    @Getter
    private ExecutorService resultPipeline;
    @Getter
    private Consumer<T> resConsumer;
    @Getter
    private Consumer<ChannelHandlerContext> expConsumer;

    /**
     * @see CrawlEngineBuilder
     */
    protected CrawlEngine(){
        this.workGroup = new NioEventLoopGroup();
        this.ssl = false;
        this.compress = false;
        this.resultPipeline = Executors.newCachedThreadPool();
    }

    protected void setSsl(boolean ssl){
        this.ssl = ssl;
    }

    protected void setCompress(boolean compress){
        this.compress = compress;
    }

    protected void setExecutorService(ExecutorService resultPipeline){
        this.resultPipeline = resultPipeline;
    }

    protected void setRefactorPoolSize(int poolSize){
        this.workGroup = new NioEventLoopGroup(poolSize);
    }

    protected void soKeepAlive(){
        optionSwitch.put(ChannelOption.SO_KEEPALIVE, true);
    }

    protected void TCPNoDelay(){
        optionSwitch.put(ChannelOption.TCP_NODELAY, true);
    }

    protected void setResConsumer(Consumer<T> resConsumer){
        this.resConsumer = resConsumer;
    }

    protected void setExpConsumer(Consumer<ChannelHandlerContext> expConsumer){
        this.expConsumer = expConsumer;
    }

    // Close engine
    @Override
    public void close(){
        this.workGroup.shutdownGracefully()
                .addListener(channelFuture -> log.info("Netty client engine auto-closed"));
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
                    request.headers().set(HttpHeaderNames.USER_AGENT, task.getUserAgent());
                    // Cookies
                    if(task.getCookie() != null && !task.getCookie().isBlank()){
                       request.headers().set(HttpHeaderNames.COOKIE, task.getCookie());
                    }

                    channelFuture.channel().writeAndFlush(request);
                });
    }

    @Override
    public void blockExecute(CrawlTask task) {
        if(this.bootstrap.config().group().isShutdown()){
            throw new IllegalStateException("ClientEngine already closed");
        }

        ChannelFuture future = bootstrap.connect(task.getHost(), task.getPort());
        try {
            future.sync(); // wait until tcp-connection is build
            // exception handler's job
        } catch (InterruptedException ignored) {}

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

        // write and flush request to this channel
        future.channel().writeAndFlush(request);
        // synchronize on this channel
        try {
            synchronized (future.channel()){
                future.channel().wait();
            }
        } catch (InterruptedException ignored) {}
    }
}