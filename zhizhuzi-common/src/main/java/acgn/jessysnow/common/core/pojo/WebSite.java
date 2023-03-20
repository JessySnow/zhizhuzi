package acgn.jessysnow.common.core.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * Tag pojo of html document
 * All field want injected by parser must be annotated by Nodes or Node
 */
public abstract class WebSite {
    @Getter
    @Setter
    private CrawlTask task;
}