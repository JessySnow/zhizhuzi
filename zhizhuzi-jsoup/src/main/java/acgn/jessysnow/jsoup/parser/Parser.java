package acgn.jessysnow.jsoup.parser;

import acgn.jessysnow.common.core.pojo.WebSite;
import org.jsoup.nodes.Document;

/**
 * Super class(interface) of all parser
 *
 * @see DomParser A parser impl of dom
 */
public interface Parser<T extends WebSite>{
    T parse(Document html, T site);
}
