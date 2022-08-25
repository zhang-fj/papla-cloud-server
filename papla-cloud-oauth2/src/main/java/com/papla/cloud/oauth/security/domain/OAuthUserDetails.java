package com.papla.cloud.oauth.security.domain;

import cn.hutool.core.collection.CollectionUtil;
import com.papla.cloud.oauth.security.enums.PasswordEncoderTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 登录用户信息
 */
@Data
@NoArgsConstructor
public class OAuthUserDetails implements UserDetails {

    private OAuthUser user;

    private Collection<GrantedAuthority> authorities;

    public OAuthUserDetails(OAuthUser user) {
        this.user = user;
        if (CollectionUtil.isNotEmpty(user.getRoles())) {
            authorities = new ArrayList<>();
            user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return PasswordEncoderTypeEnum.BCRYPT.getPrefix() + user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
