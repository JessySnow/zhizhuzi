package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.pojo.CrawlTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.function.Consumer;

public interface ClientEngine extends AutoCloseable {
    // boot the http client
    void boot();

    // boot the http client, with a customized exception handling strategy
    default void boot(Consumer<ChannelHandlerContext> strategy){
        throw new UnsupportedOperationException();
    }

    // boot the http client, with a customized pipeline handler
    default void boot(ChannelInitializer<SocketChannel> initializer){
        throw new UnsupportedOperationException();
    }

    // execute a crawl task
    void execute(CrawlTask task);
}
