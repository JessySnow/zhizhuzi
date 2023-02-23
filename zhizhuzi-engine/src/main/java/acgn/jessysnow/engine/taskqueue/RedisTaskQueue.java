package acgn.jessysnow.engine.taskqueue;

import acgn.jessysnow.engine.core.TaskQueue;
import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.pojo.WebSite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.URISyntaxException;

public class RedisTaskQueue implements TaskQueue {
    private final JedisPool pool = new JedisPool("localhost", 6379);

    @Override
    public <T extends CrawlTask> void offer(T task) {}

    @Override
    public <T extends CrawlTask> T poll(Class<? extends WebSite> clazz) {
        return null;
    }

    // TODO blocking queue
    public static void main(String[] args) throws URISyntaxException {
        RedisTaskQueue queue = new RedisTaskQueue();
        CrawlTask task = new CrawlTask("https://www.baidu.com");
        Gson gson = new GsonBuilder().create();
        String objectJson = gson.toJson(task);
        try(Jedis jedis = queue.pool.getResource()){
            jedis.sadd("A", objectJson);
        }
    }
}
