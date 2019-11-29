package com.fxb.learn.spring_boot_security_jwt.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 模拟用户实体
 *
 * @author fangjiaxiaobai@gmail.com
 * @date 2019-11-29
 * @since 1.0.0
 */
public class User implements UserDetails {

    public String password = "$2a$10$ZwuuhofB/AGS.ewmGbeMMO0Vt0V6Zpg2DQmjP8/qcLTMvmr81agFS";

    public String username = "fxb";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 模拟用户的权限
        return Stream.of("admin", "superAdmin", "user")
                .map(item -> (GrantedAuthority) () -> item).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        // 默认密码为123. 这里是手动生成的,模拟数据库中的密码
        return password;
    }

    @Override
    public String getUsername() {
        // 模拟用户的name是 fxb
        return  username;
    }

    /**
     * 账户没有过期.
     *
     * @return true, 表示没有过期, false, 表示已过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户没有锁定
     *
     * @return true, 未被锁定, false 已被锁定。
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 认证未过期
     *
     * @return true: 未过期,false：已过期。
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户时候可用
     *
     * @return true:可用, false:不可用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
