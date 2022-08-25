package com.papla.cloud.oauth.security.service;

import com.papla.cloud.oauth.fegin.UserService;
import com.papla.cloud.oauth.security.domain.OAuthUser;
import com.papla.cloud.oauth.security.domain.OAuthUserDetails;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 从数据库获取认证用户信息，用于和前端传过来的用户信息进行密码判读
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {


    private UserService userService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OAuthUser sysUser =userService.loadUserByUsername(username);
        OAuthUserDetails oauthUserDetails = new OAuthUserDetails(sysUser);
        if (oauthUserDetails == null) {
            throw new UsernameNotFoundException("用户不存在");
        } else if (!oauthUserDetails.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!oauthUserDetails.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!oauthUserDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return oauthUserDetails;
    }

}
