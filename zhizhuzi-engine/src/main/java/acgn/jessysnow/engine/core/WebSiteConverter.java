package acgn.jessysnow.engine.core;

import acgn.jessysnow.common.core.pojo.CrawlTask;
import acgn.jessysnow.gson.pojo.Json;
import acgn.jessysnow.jsoup.parser.DomParser;
import acgn.jessysnow.common.core.pojo.WebSite;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Encode HttpContent to a website-pojo
 */
public class WebSiteConverter<T extends WebSite> extends MessageToMessageDecoder<HttpContent> {
    private final Class<T> clazz;
    private final String charSet;
    private final DomParser<T> domParser = new DomParser<>();

    public WebSiteConverter(Class<T> clazz, String charSet){
        this.clazz = clazz;
        this.charSet = charSet;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void decode(ChannelHandlerContext ctx, HttpContent msg, List<Object> out) throws Exception {
        String response = msg.content().toString(Charset.forName(charSet));

        T res = null;
        if (Json.class.isAssignableFrom(clazz)){
            Gson gson = new Gson();
            res = gson.fromJson(response, clazz);
        }else if (WebSite.class.isAssignableFrom(clazz)) {
            Document document = Jsoup.parse(response);
            res = domParser.parse(document, clazz.getConstructor().newInstance());
        }

        Attribute<Object> attr = ctx.channel().attr(AttributeKey.valueOf(ctx.channel().id().asShortText()));
        res.setTask((CrawlTask) attr.get());
        attr.set(res);
        out.add(res);
    }
}
