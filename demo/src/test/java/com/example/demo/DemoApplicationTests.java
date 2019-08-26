package com.example.demo;

import com.example.demo.util.EmailTool;
import com.example.demo.util.SnowFlake;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

//    @Test
//    public void contextLoads() {
//    }
//
//    @Test
//    public void generateSnowFlakeId() {
//        SnowFlake snowFlake = new SnowFlake(3, 3);
//
//        for (int i = 0; i < 1 << 8; i++) {
//            System.out.println(snowFlake.nextId());
//        }
//    }
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Test
//    public void testRedis() {
//        stringRedisTemplate.opsForValue().set("aaa", "111", Duration.ofSeconds(10));
//        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
//    }
//
//    @Autowired
//    private EmailTool emailTool;
//
//    @Test
//    public void testEmail() {
//        emailTool.sendSimpleMail("subject", "context");
//    }
}
