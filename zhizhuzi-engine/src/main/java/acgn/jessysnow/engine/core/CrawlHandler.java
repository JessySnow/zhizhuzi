package acgn.jessysnow.engine.core;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * A Netty-Inbound handler work with NettyClientEngine
 *      - Consume the website-pojo get from pipeline
 * @see NettyClientEngine
 */
@ChannelHandler.Sharable
public class CrawlHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
