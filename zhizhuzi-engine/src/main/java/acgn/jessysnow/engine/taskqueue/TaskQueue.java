package acgn.jessysnow.engine.taskqueue;

import acgn.jessysnow.common.core.pojo.CrawlTask;

public interface TaskQueue{
    <T extends CrawlTask> void offer(T task);

    CrawlTask poll();

    CrawlTask poll(int timeout);

    long size();
}
