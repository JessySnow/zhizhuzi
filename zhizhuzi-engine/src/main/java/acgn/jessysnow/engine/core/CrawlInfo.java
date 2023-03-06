package acgn.jessysnow.engine.core;

import acgn.jessysnow.jsoup.pojo.WebSite;

// embed crawl task and website(result)
public class CrawlInfo<T extends WebSite> {
    private final CrawlTask task;
    private T result;

    protected CrawlTask getTask() {
        return task;
    }

    protected T getResult() {
        return result;
    }

    protected void setResult(T result) {
        this.result = result;
    }

    protected CrawlInfo(CrawlTask task){
        this.task = task;
    }
}
