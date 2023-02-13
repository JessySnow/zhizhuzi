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

    // Fixme
    @Override
    protected void decode(ChannelHandlerContext ctx, HttpContent msg, List<Object> out) throws Exception {
        String response = msg.content().toString(CharsetUtil.UTF_8);

        T res;
//        if (clazz.isAssignableFrom(Json.class)){
//            Gson gson = new Gson();
//            res = gson.fromJson(response, clazz);
//        }else if (clazz.isAssignableFrom(WebSite.class)) {
            Document document = Jsoup.parse(response);
            DomParser<T> parser = new DomParser<>();
            res = parser.parse(document, clazz.getConstructor().newInstance());
//        }else {
//            res = null;
//        }

        out.add(res);
    }
}
