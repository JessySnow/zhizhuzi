package acgn.jessysnow.engine.taskqueue;

import acgn.jessysnow.engine.core.TaskQueue;
import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.pojo.WebSite;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

// Blocking task queue
public class RedisTaskQueue implements TaskQueue {
    private final JedisPool pool = new JedisPool("localhost", 6379);
    private final Gson gson = new Gson();

    @Override
    public <T extends CrawlTask> void offer(T task, Class<? extends WebSite> clazz) {
        String keyPrefix = "task:" + clazz.toString();
        String keyValue = gson.toJson(task);
        try(Jedis jedis = pool.getResource()){
            jedis.lpush(keyPrefix, keyValue);
        }
    }

    @Override
    public CrawlTask poll(Class<? extends WebSite> clazz) {
        String keyPrefix = "task:" + clazz.toString();
        try(Jedis jedis = pool.getResource()){
            List<String> pair = jedis.brpop(keyPrefix);
            return gson.fromJson(pair.get(1), CrawlTask.class);
        }
    }
}
