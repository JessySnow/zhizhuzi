package acgn.jessysnow.engine.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpContent;

import java.util.List;

/**
 * Encode HttpContent to a website-pojo
 */
public class WebSiteConverter extends MessageToMessageEncoder<HttpContent> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpContent msg, List<Object> out) throws Exception {
    }
}
