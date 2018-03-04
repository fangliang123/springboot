package com.boloomo.redistest;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Template;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.boloomo.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)
public class RedisTest {
	
	@Autowired
	private RedisConnectionFactory factory;
	
	@Autowired
	private RedisTemplate<String, Object> template;
	
	/**
	 * 基本测试
	 */
	@Test
	public void redisTest () {
		RedisConnection conn = factory.getConnection();
		conn.set("hello".getBytes(), "world".getBytes());
		System.out.println(new String(conn.get("hello".getBytes())));
	}
	
	/**
	 * Redis Template 
	 */
	@Test
	public void testTemplate() {
		template.opsForList().rightPush("1", "mary");
		template.opsForList().rightPush("1", "jack");
		template.opsForList().rightPush("1", "smith");
		template.opsForList().rightPush("1", "bob");
		template.opsForList().rightPush("1", "lucy");
		
		List<Object> list = template.opsForList().range("1", 0, template.opsForList().size("1")-1);
		
		for (Object object : list) {
			System.out.println(object);
		}
		
		
	}
	
}
