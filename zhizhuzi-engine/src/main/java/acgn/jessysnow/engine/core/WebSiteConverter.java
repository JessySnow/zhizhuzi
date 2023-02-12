package acgn.jessysnow.engine.core;

import acgn.jessysnow.jsoup.parser.DomParser;
import acgn.jessysnow.jsoup.pojo.WebSite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.CharsetUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Encode HttpContent to a website-pojo
 */
public class WebSiteConverter<T extends WebSite> extends MessageToMessageDecoder<HttpContent> {
    private final Class<T> clazz;

    public WebSiteConverter(Class<T> clazz){
        this.clazz = clazz;
    }

    // Fixme For different content types, initialize different decoders and get the field type from clazz
    // Todo Json field support
    @Override
    protected void decode(ChannelHandlerContext ctx, HttpContent msg, List<Object> out) throws Exception {
        String response = msg.content().toString(CharsetUtil.UTF_8);
        Document document = Jsoup.parse(response);
        DomParser<T> parser = new DomParser<>();

        T res = parser.parse(document, clazz.getConstructor().newInstance());
        out.add(res);
    }
}
