package com.fxb.learn.spring_boot_security_jwt.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxb.learn.spring_boot_security_jwt.config.Redis;
import com.fxb.learn.spring_boot_security_jwt.model.User;
import com.fxb.learn.spring_boot_security_jwt.properties.UrlConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JWT 认证拦截器
 *
 * @author fangjiaxiaobai@gmail.com
 * @date 2019-11-29
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private ObjectMapper mapper = new ObjectMapper();

    UserDetailsService userDetailsService;

    /**
     * url的配置: 配置是否拦截的url
     */
    private UrlConfig urlConfig;

    /**
     * 对应 Url 的匹配器
     */
    private OrRequestMatcher orRequestMatcher;

    @PostConstruct
    private void init(){
        // 构建不拦截的url 匹配器
        List<RequestMatcher> matchers =
                urlConfig.getPermitAllUrl().stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        orRequestMatcher = new OrRequestMatcher(matchers);
    }

    /**
     * 判断是否对某个Url进行拦截
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return orRequestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1.获取Header中的token
        String token = request.getHeader("authentication");

        if (Strings.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2.解析jwt,
        User user = getJwtUserFromToken(token);

        // 3.生成username, Token 进行登录。

        if (null != user && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 判断用户的token是否过期
            if (null != Redis.get(token) && Redis.get(token).before(new Date(Instant.now().getEpochSecond() * 1000 + 600000))) {
                // 未失效
                Redis.put(token);
            } else {
                throw new AccountExpiredException("登录信息已经过期或已经退出登录，请重新登录！");
            }

            // 用户未登录.
            // 1. 设置 Authentication
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 解析 jwt 获取用户信息
     *
     * @param token jwt
     * @return 用户信息
     * @throws JsonProcessingException 解析异常
     */
    private User getJwtUserFromToken(String token) throws JsonProcessingException {
        Claims claims = Jwts.parser().setSigningKey("fxb123")
                .parseClaimsJws(token).getBody();
        String subject = claims.getSubject();
        // 这里存储的解析完成之后的token中用户信息.
        Map<String, Object> subjectMap = mapper.readValue(subject, Map.class);
        System.out.println((String) subjectMap.get("username")); // fxb (用户里的username)
        // System.out.println(subjectMap.get(".."));  // 其他信息
        return new User();
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setUrlConfig(UrlConfig urlConfig) {
        this.urlConfig = urlConfig;
    }
}
