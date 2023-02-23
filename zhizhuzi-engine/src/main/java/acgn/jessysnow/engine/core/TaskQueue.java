package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.pojo.WebSite;

public interface TaskQueue{
    <T extends CrawlTask> void offer(T task);

    <T extends CrawlTask> T poll(Class<? extends WebSite> clazz);
}
