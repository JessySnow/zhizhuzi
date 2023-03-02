package acgn.jessysnow.engine.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public interface Engine extends AutoCloseable {
    // boot a request engine with a customized pipeline initializer
    void boot(ChannelInitializer<SocketChannel> initializer);

    // execute a crawl task
    void execute(CrawlTask task);

    // execute task in blocking manner
    void blockExecute(CrawlTask task);
}
