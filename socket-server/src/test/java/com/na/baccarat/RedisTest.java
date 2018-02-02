package com.na.baccarat;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.na.SocketServerApplication;
import com.na.user.socketserver.config.SocketIoConfig;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes={SocketServerApplication.class,SocketIoConfig.class},loader = AnnotationConfigContextLoader.class)
@TestPropertySource(locations="classpath:application.properties")
public class RedisTest {
	
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void test(){
    	stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }
}
