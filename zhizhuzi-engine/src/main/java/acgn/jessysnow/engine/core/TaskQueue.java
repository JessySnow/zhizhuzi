package acgn.jessysnow.engine.core;

import acgn.jessysnow.engine.pojo.CrawlTask;

public interface TaskQueue{
    <T extends CrawlTask> void offer(T task);

    CrawlTask poll();

    CrawlTask poll(int timeout);

    long size();
}
