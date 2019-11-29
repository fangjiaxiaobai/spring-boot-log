package com.fxb.learn.spring_boot_security_jwt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxb.learn.spring_boot_security_jwt.config.Redis;
import com.fxb.learn.spring_boot_security_jwt.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

/**
 * @author fangjiaxiaobai@gmail.com
 * @date 2019-11-29
 * @since 1.0.0
 */
@RestController
public class TestController {

    @GetMapping(value = {"permit/test","test", "/"})
    public Authentication test() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping(value = {"permit/jwt"})
    public String getJwt() {
        String token = generationJwt();
        Redis.put(token);
        return token;
    }

    // 生成token
    private String generationJwt() {
        User user = new User();
        String subject = null;
        try {
            subject = new ObjectMapper().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            System.err.println(" 编码 User 异常 ");
        }
        Date date = new Date(Instant.now().getEpochSecond() * 1000 + 600000);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, "fxb123")
                .compact();
    }

}
