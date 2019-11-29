package com.fxb.learn.spring_boot_security_jwt.config;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟 Redis存储 Jwt
 * @author fangjiaxiaobai@gmail.com
 * @date 2019-11-29
 * @since 1.0.0
 */
public class Redis {


    private static Map<String, Date> redis = new ConcurrentHashMap<>();

    public static Date get(String token) {
        return redis.get(token);
    }

    public static Date put(String token) {
        return redis.put(token, new Date(Instant.now().getEpochSecond() * 1000 + 600000));
    }


}
