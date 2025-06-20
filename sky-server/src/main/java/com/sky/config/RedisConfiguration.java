package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
       log.info("Creating RedisTemplate");
        RedisTemplate redisTemplate = new RedisTemplate();
//        Set the Redis connection object
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        Set the serializer for Redis keys
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    };
}
