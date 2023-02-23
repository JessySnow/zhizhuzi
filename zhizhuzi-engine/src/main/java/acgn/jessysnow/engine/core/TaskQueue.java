package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.pojo.WebSite;

public interface TaskQueue{
    <T extends CrawlTask> void offer(T task, Class<? extends WebSite> clazz);

    CrawlTask poll(Class<? extends WebSite> clazz);
}
