package acgn.jessysnow.jsoup.parser;

import acgn.jessysnow.jsoup.pojo.WebSite;
import org.jsoup.nodes.Document;

/**
 * Super class(interface) of all parser
 * @param <T> Target website
 *
 * @see DomParser A parser impl of dom
 */
public interface Parser{
    WebSite parse(Document html, WebSite site);
}
