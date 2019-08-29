package com.example.demo;

import com.netflix.discovery.converters.Auto;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    public void testEncoder() {
//        boolean b = passwordEncoder.matches("123456780", "$2a$10$gpl2VaaCy/f9kdW2ASCrde22w.ah2WBsDK1NYSsIN0qjhfqCJGOTe");
//        System.out.println(b);
//    }

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
