package acgn.jessysnow.engine.taskqueue;

import acgn.jessysnow.engine.core.TaskQueue;
import acgn.jessysnow.engine.pojo.CrawlTask;
import acgn.jessysnow.jsoup.pojo.WebSite;
import acgn.jessysnow.jsoup.sample.JDUrlSkus;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.URISyntaxException;

// FIXME
public class RedisTaskQueue implements TaskQueue {
    private final JedisPool pool = new JedisPool("localhost", 6379);
    private final Gson gson = new Gson();

    @Override
    public <T extends CrawlTask> void offer(T task, Class<? extends WebSite> clazz) {
        String keyPrefix = clazz.toString();
        String keyValue = gson.toJson(task);
        try(Jedis jedis = pool.getResource()){
            jedis.lpush(keyPrefix, keyValue);
        }
    }

    @Override
    public CrawlTask poll(Class<? extends WebSite> clazz) {
        String keyPrefix = clazz.toString();
        try(Jedis jedis = pool.getResource()){
            String value = jedis.rpop(keyPrefix);
            return gson.fromJson(value, CrawlTask.class);
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        RedisTaskQueue queue = new RedisTaskQueue();
        queue.offer(new CrawlTask("https://www.google.com"), JDUrlSkus.class);
        CrawlTask task = queue.poll(JDUrlSkus.class);
        System.out.println(task);
    }
}
