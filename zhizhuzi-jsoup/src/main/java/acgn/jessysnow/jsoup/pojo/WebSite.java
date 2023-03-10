package acgn.jessysnow.jsoup.pojo;

import acgn.jessysnow.common.pojo.CrawlTask;
import acgn.jessysnow.jsoup.sample.SimpleHtml;
import lombok.Getter;
import lombok.Setter;

/**
 * Tag pojo of html document
 * All field want injected by parser must be annotated by Nodes or Node
 * @see SimpleHtml demo
 * <br>
 * Accaptable field type:
 *      String
 *      List<String>
 */
public abstract class WebSite {
    @Getter
    @Setter
    private CrawlTask task;
}