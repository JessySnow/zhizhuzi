package acgn.jessysnow.pojo;

import lombok.Data;

import java.util.function.Consumer;

/**
 * Crawl task pojo, describes the parameters of the download task
 */
@Data
public class CrawlTask {
    private String host;
    private int port;
}
