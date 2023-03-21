package acgn.jessysnow.engine.taskqueue.impl;

import acgn.jessysnow.common.core.pojo.CrawlTask;
import acgn.jessysnow.common.core.pojo.WebSite;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedisTaskQueueTest {
    private final RedisTaskQueue redisTaskQueue = new RedisTaskQueue(InnerTestClass.class);

    @Order(0)
    @Test
    public void testOffer(){
        for (int i = 0; i < 3; i++) {
            redisTaskQueue.offer(new CrawlTask(String.valueOf(i)));
        }
    }

    @Order(1)
    @Test
    public void testPoll(){
        for (int i = 0; i < 2; i++) {
            Assertions.assertEquals(String.valueOf(i), redisTaskQueue.poll().getUri().toString());
        }
    }

    @Order(2)
    @Test
    public void testTimedPoll(){
        Assertions.assertEquals(String.valueOf(2), redisTaskQueue.poll(1).getUri().toString());
        Assertions.assertNull(redisTaskQueue.poll(1));
    }

    private static class InnerTestClass extends WebSite{}
}