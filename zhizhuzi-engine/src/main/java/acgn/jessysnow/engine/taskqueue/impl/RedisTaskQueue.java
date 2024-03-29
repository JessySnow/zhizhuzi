package acgn.jessysnow.engine.taskqueue.impl;

import acgn.jessysnow.common.core.pojo.CrawlTask;
import acgn.jessysnow.common.core.pojo.WebSite;
import acgn.jessysnow.engine.taskqueue.TaskQueue;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Optional;

// Blocking task queue
public class RedisTaskQueue implements TaskQueue {
    private final JedisPool pool;
    private final Gson gson;

    private final String keyPrefix;

    public RedisTaskQueue(Class<? extends WebSite> clazz){
        this.gson = new Gson();
        this.pool = new JedisPool("localhost", 6379);
        this.keyPrefix = "task:"+clazz.toString();
    }

    public RedisTaskQueue(Class<? extends WebSite> clazz, String host, int port){
        this.gson = new Gson();
        this.pool = new JedisPool(host, port);
        this.keyPrefix = "task:"+clazz.toString();
    }

    // offer a task to redis list
    @Override
    public <T extends CrawlTask> void offer(T task) {
        String keyValue = gson.toJson(task);
        try(Jedis jedis = pool.getResource()){
            jedis.lpush(keyPrefix, keyValue);
        }
    }

    // blocking poll
    @Override
    public CrawlTask poll() {
        try(Jedis jedis = pool.getResource()){
            List<String> pair = jedis.brpop(0, keyPrefix);
            return gson.fromJson(pair.get(1), CrawlTask.class);
        }
    }

    // blocking poll with timeout
    @Override
    public CrawlTask poll(int timeout) {
        try(Jedis jedis = pool.getResource()){
            List<String> pair = jedis.brpop(timeout, keyPrefix);
            return Optional.ofNullable(pair)
                    .map((t) -> gson.fromJson(t.get(1), CrawlTask.class))
                    .orElse(null);
        }
    }

    @Override
    public long size() {
        try(Jedis jedis = pool.getResource()){
            return jedis.llen(keyPrefix);
        }
    }
}
