package com.papla.cloud.gateway.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.papla.cloud.common.core.constants.AuthConstants;
import com.papla.cloud.common.core.constants.GlobalConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 网关自定义鉴权管理器
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ResourceServerManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final RedisTemplate redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) { // 预检请求放行
            return Mono.just(new AuthorizationDecision(true));
        }
        PathMatcher pathMatcher = new AntPathMatcher();
        String method = request.getMethodValue();
        String path = request.getURI().getPath();
        String restfulPath = method + ":" + path;

        // 如果token以"bearer "为前缀，到此方法里说明JWT有效即已认证，其他前缀的token则拦截
        String token = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_KEY);
        if (StringUtils.isNotBlank(token) && token.startsWith(AuthConstants.AUTHORIZATION_PREFIX)) {
        } else {
            return Mono.just(new AuthorizationDecision(false));
        }

        /**
         * 鉴权开始
         */

        // 缓存中获取匿名资源清单，并判断请求路径是否为匿名资源路径
        Set<Object> anonymousUrls = redisTemplate.opsForSet().members(GlobalConstants.URL_PERM_ANONY_KEY);
        if(anonymousUrls != null){
            for(Object anonymousUrl:anonymousUrls){
                if (pathMatcher.match((String)anonymousUrl, restfulPath)) {
                    // 是匿名资源路径，则放行
                    return Mono.just(new AuthorizationDecision(true));
                }
            }
        }

        /**
         * 缓存取 [URL权限-角色集合] 规则数据
         * urlPermRolesRules = [{'key':'GET:/api/v1/users/*','value':['ADMIN','TEST']},...]
         */
        Map<String, Object> urlPermRolesRules = redisTemplate.opsForHash().entries(GlobalConstants.URL_PERM_ROLES_KEY);

        // 根据请求路径判断有访问权限的角色列表
        List<String> authorizedRoles = new ArrayList<>(); // 拥有访问权限的角色

        for (Map.Entry<String, Object> permRoles : urlPermRolesRules.entrySet()) {
            String perm = permRoles.getKey();
            if (pathMatcher.match(perm, restfulPath)) {
                List<String> roles = Convert.toList(String.class, permRoles.getValue());
                authorizedRoles.addAll(Convert.toList(String.class, roles));
            }
        }

        // 判断JWT中携带的用户角色是否有权限访问
        Mono<AuthorizationDecision> authorizationDecisionMono = mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authority -> {
                    String roleCode = authority.substring(AuthConstants.AUTHORITY_PREFIX.length()); // 用户的角色
                    if (GlobalConstants.ROOT_ROLE_CODE.equals(roleCode)) {
                        return true; // 如果是超级管理员则放行
                    }
                    boolean hasAuthorized = CollectionUtil.isNotEmpty(authorizedRoles) && authorizedRoles.contains(roleCode);
                    return hasAuthorized;
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
        return authorizationDecisionMono;
    }

    public static void main(String[] args) {
        PathMatcher pathMatcher = new AntPathMatcher();
        System.out.println(pathMatcher.match("GET:**/api/v1/users/*","GET:/api/v1/users/save"));
    }
}
