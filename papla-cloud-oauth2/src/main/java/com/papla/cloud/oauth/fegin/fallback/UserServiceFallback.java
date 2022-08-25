package com.papla.cloud.oauth.fegin.fallback;

import com.papla.cloud.oauth.fegin.UserService;
import com.papla.cloud.oauth.security.domain.OAuthUser;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFallback implements UserService {

    @Override
    public OAuthUser loadUserByUsername(String name) {
        return new OAuthUser();
    }
}
