package acgn.jessysnow.core;

import acgn.jessysnow.pojo.CrawlTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.function.Consumer;

public interface ClientEngine {
    // boot the http client
    void boot();

    // boot the http client, with a customize error handling strategy
    default void boot(Consumer<ChannelHandlerContext> strategy){
        throw new UnsupportedOperationException();
    }

    default void boot(ChannelInitializer<SocketChannel> initializer){
        throw new UnsupportedOperationException();
    }

    // execute a specific crawl task
    void execute(CrawlTask task);
}
