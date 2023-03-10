package acgn.jessysnow.engine.core;

import acgn.jessysnow.common.pojo.CrawlTask;
import acgn.jessysnow.common.helper.SysHelper;
import acgn.jessysnow.jsoup.pojo.WebSite;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Log4j2
public class CrawlEngine<T extends WebSite> implements Engine<T> {
    private final Bootstrap bootstrap = new Bootstrap();
    private EventLoopGroup workGroup;
    private final HashMap<ChannelOption<Boolean>, Boolean> optionSwitch = new HashMap<>();
    @Getter
    private boolean ssl;
    @Getter
    private boolean compress;
    @Getter
    private String charSet = "UTF-8";
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
        this.workGroup = getWorkGroup(null);
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
        this.workGroup = getWorkGroup(poolSize);
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
    protected void setCharSet(String charSet){
        this.charSet = charSet;
    }


    @Override
    public void close(){
        this.workGroup.shutdownGracefully()
                .addListener(channelFuture -> log.info("Netty client engine auto-closed"));
    }

    @Override
    public void boot(ChannelInitializer<SocketChannel> initializer) {
        // Sys native channel config
        this.bootstrap.group(workGroup)
                .channel(this.workGroup instanceof NioEventLoopGroup ? NioSocketChannel.class :
                        workGroup instanceof KQueueEventLoopGroup ? KQueueSocketChannel.class :
                        EpollSocketChannel.class)
                .handler(initializer);
        this.optionSwitch.forEach(bootstrap::option);
    }

    @Override
    public void execute(CrawlTask task) {
        validateStatus(task);
        ChannelFuture future = bootstrap.connect(task.getHost(), task.getPort())
                .addListener((ChannelFutureListener) _future -> _future.channel().writeAndFlush(configRequest(task)));
        attachTask(future.channel(), task);
    }

    @Override
    public void blockExecute(CrawlTask task) {
        validateStatus(task);
        ChannelFuture future = bootstrap.connect(task.getHost(), task.getPort());
        future.addListener((ChannelFutureListener) channelFuture ->
                channelFuture.channel().writeAndFlush(configRequest(task)));
        attachTask(future.channel(), task);
        // While TCP connection is build, sync this channel(socket),
        // until crawl handler's or exception handler's notification
        try {
            synchronized (future.channel()){
                future.channel().wait();
            }
        } catch (InterruptedException ignored) {}
    }

    @Override
    public T submit(CrawlTask task) {
        validateStatus(task);
        ChannelFuture future = bootstrap.connect(task.getHost(), task.getPort());
        future.addListener((ChannelFutureListener) _future -> _future.channel().writeAndFlush(configRequest(task)));
        Attribute<Object> attribute = attachTask(future.channel(), task);

        // While TCP connection is build, sync this channel(socket),
        // until crawl handler's or exception handler's notification
        try {
            synchronized (future.channel()){
                future.channel().wait();
            }
        } catch (InterruptedException ignored) {}

        return (T) attribute.get();
    }

    private EventLoopGroup getWorkGroup(Integer poolSize){
        SysHelper.SysType sysType = SysHelper.getSysType();
        poolSize = Optional.ofNullable(poolSize).orElse(Runtime.getRuntime().availableProcessors());

        EventLoopGroup res;
        if (sysType == null || sysType == SysHelper.SysType.Windows){
            res = new NioEventLoopGroup(poolSize);
        }else if (sysType == SysHelper.SysType.MAC_OS_X || sysType == SysHelper.SysType.FreeBSD){
            res = new KQueueEventLoopGroup(poolSize);
        }else { // Linux
            res = new EpollEventLoopGroup(poolSize);
        }
        return res;
    }

    private HttpRequest configRequest(CrawlTask task){
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(task.getHttpVersion(), task.getMethod(),
                task.getUri().toASCIIString());
        request.headers().set(HttpHeaderNames.HOST, task.getHost());
        request.headers().set(HttpHeaderNames.USER_AGENT, task.getUserAgent());
        if(task.getCookie() != null && !task.getCookie().isBlank()){
            request.headers().set(HttpHeaderNames.COOKIE, task.getCookie());
        }
        return request;
    }

    private void validateStatus(CrawlTask task){
        if (task == null || task.getHost() == null){
            log.error(task);
            throw new IllegalStateException("Crawl task is broken");
        }
        if(this.bootstrap.config().group().isShutdown()){
            throw new IllegalStateException("ClientEngine already closed");
        }
    }

    private Attribute<Object> attachTask(Channel channel, CrawlTask task){
        Attribute<Object> attr = channel.attr(AttributeKey.valueOf(channel.id().asShortText()));
        attr.set(task);
        return attr;
    }
}
