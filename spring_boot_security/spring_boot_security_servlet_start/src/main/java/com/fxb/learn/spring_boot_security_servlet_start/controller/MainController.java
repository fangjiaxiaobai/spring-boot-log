package com.fxb.learn.spring_boot_security_servlet_start.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器
 * @author fangjiaxiaobao@gmail.com
 * @date 2019-11-27
 * @since 1.0.0
 */
@RestController
public class MainController {

    @GetMapping("/{name}")
    public String hello(@PathVariable("name") String name){
        return "hello " + name;
    }
}
