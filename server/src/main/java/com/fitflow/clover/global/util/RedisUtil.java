package com.fitflow.clover.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;

    public void setDataExpire(String key, String value, long durationMillis) {
        Duration expireDuration = Duration.ofMillis(durationMillis);
        redisTemplate.opsForValue().set(key, value, expireDuration);
    }

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
