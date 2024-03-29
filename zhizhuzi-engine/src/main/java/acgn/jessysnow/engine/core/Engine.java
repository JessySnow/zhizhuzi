package acgn.jessysnow.engine.core;

import acgn.jessysnow.common.core.pojo.CrawlTask;
import acgn.jessysnow.common.core.pojo.WebSite;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public interface Engine<T extends WebSite> extends AutoCloseable {
    // boot a request engine with a customized pipeline initializer
    void boot(ChannelInitializer<SocketChannel> initializer);

    // execute a crawl task
    void execute(CrawlTask task);

    // execute task in blocking manner
    void blockExecute(CrawlTask task);

    // execute and get result as pojo
    T submit(CrawlTask task);
}
