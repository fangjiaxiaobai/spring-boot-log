package com.fxb.learn.spring_boot_security_jwt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * url拦截配置
 * @author fangjiaxiaobai@gmail.com
 * @date 2019-11-29
 * @since 1.0.0
 */
@ConfigurationProperties("fxb.url")
@Component
public class UrlConfig {

    /**
     * 都可访问的url
     */
    private List<String> permitAllUrl;

    /**
     * 匿名访问的url
     */
    private List<String> anonymousUrl;

    public void setPermitAllUrl(List<String> permitAllUrl) {
        this.permitAllUrl = permitAllUrl;
    }

    public List<String> getAnonymousUrl() {
        return anonymousUrl;
    }

    public void setAnonymousUrl(List<String> anonymousUrl) {
        this.anonymousUrl = anonymousUrl;
    }

    public List<String> getPermitAllUrl() {
        return permitAllUrl;
    }
}

