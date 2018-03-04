package com.boloomo.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis连接配置
 * @author 13607
 */
@Configuration
public class RedisConfig {
	
	@Bean
	RedisConnectionFactory redisCF () {
		JedisConnectionFactory jcf = new JedisConnectionFactory();
		jcf.setPort(6379);
		jcf.setHostName("localhost");
		return jcf;
	}
	
	@Bean
	public RedisTemplate<String,Object> template (RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(factory);
		return redisTemplate;
	}
}
