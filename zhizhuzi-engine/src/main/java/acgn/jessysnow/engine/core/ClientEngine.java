package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.pojo.CrawlTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.function.Consumer;

public interface ClientEngine extends AutoCloseable {
    // boot a request engine with a customized pipeline initializer
    void boot(ChannelInitializer<SocketChannel> initializer);

    // execute a crawl task
    void execute(CrawlTask task);
}
