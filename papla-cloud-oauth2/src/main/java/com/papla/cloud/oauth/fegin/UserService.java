package com.papla.cloud.oauth.fegin;


import com.papla.cloud.oauth.fegin.fallback.UserServiceFallback;
import com.papla.cloud.oauth.security.domain.OAuthUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "papla-cloud-admin-server", fallback = UserServiceFallback.class)
public interface UserService {

    @RequestMapping(value = "/auth/user/getUserByName", method = RequestMethod.GET)
    OAuthUser loadUserByUsername(@RequestParam("username") String username);

}
