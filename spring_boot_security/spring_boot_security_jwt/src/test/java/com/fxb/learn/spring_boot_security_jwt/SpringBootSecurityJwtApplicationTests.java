package com.fxb.learn.spring_boot_security_jwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringBootSecurityJwtApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    public void TestGenerateBCEn(){
        System.out.println(new BCryptPasswordEncoder().encode("123"));
    }
}
