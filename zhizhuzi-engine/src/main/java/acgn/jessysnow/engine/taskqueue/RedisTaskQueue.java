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
    private final JedisPool pool;
    private final Gson gson;

    private final Class<? extends WebSite> clazz;

    public RedisTaskQueue(Class<? extends WebSite> clazz){
        this.clazz = clazz;
        this.gson = new Gson();
        this.pool = new JedisPool("localhost", 6379);
    }

    public RedisTaskQueue(Class<? extends WebSite> clazz, String host, int port){
        this.clazz = clazz;
        this.gson = new Gson();
        this.pool = new JedisPool(host, port);
    }

    @Override
    public <T extends CrawlTask> void offer(T task) {
        String keyPrefix = "task:" + clazz.toString();
        String keyValue = gson.toJson(task);
        try(Jedis jedis = pool.getResource()){
            jedis.lpush(keyPrefix, keyValue);
        }
    }

    @Override
    public CrawlTask poll() {
        String keyPrefix = "task:" + clazz.toString();
        try(Jedis jedis = pool.getResource()){
            List<String> pair = jedis.brpop(keyPrefix);
            return gson.fromJson(pair.get(1), CrawlTask.class);
        }
    }
}
