package com.fxb.learn.spring_boot_security_jwt.config;

import com.fxb.learn.spring_boot_security_jwt.filter.JwtAuthenticationFilter;
import com.fxb.learn.spring_boot_security_jwt.properties.UrlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * web 安全配置
 * @author fangjiaxiaobai@gmail.com
 * @date 2019-11-29
 * @since 1.0.0
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UrlConfig urlConfig;

    private UserDetailsService userDetailsService;

    /**
     * JWT 认证拦截器
     */
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 匿名访问的url也类似处理
        String[] permitUrls = urlConfig.getPermitAllUrl().toArray(new String[0]);
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(permitUrls).permitAll() //配置无权可以访问的url
                .anyRequest().authenticated() // 其他的url都必须的鉴权后访问(注意anyRequest在antMatchers之后)
                .and().formLogin().permitAll() //指定登录页面(使用默认的登录页面)
                .and().csrf().disable()// 禁用csrf
                // 在验证用户名密码的时候增加jwt过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); //

    }

    /**
     * 配置 UserDetailsService 和 PasswordEncoder
     * @param auth 快速构建一个 AuthenticationManager
     * @throws Exception 异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Autowired
    public void setUrlConfig(UrlConfig urlConfig) {
        this.urlConfig = urlConfig;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtAuthenticationFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
}
