package acgn.jessysnow.engine.core;

import acgn.jessysnow.jsoup.parser.DomParser;
import acgn.jessysnow.jsoup.pojo.WebSite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.CharsetUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Encode HttpContent to a website-pojo
 */
public class WebSiteConverter<T extends WebSite> extends MessageToMessageEncoder<HttpContent> {
    private final Class<T> clazz;

    WebSiteConverter(Class<T> clazz){
        this.clazz = clazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpContent msg, List<Object> out) throws Exception {
        String response = msg.content().toString(CharsetUtil.UTF_8);
        Document document = Jsoup.parse(response);
        DomParser<T> parser = new DomParser<>();

        T res = parser.parse(document, clazz.getConstructor().newInstance());
        out.add(res);
    }
}
