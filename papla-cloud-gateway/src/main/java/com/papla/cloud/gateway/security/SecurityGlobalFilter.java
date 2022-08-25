package com.papla.cloud.gateway.security;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import com.papla.cloud.common.redis.utils.RedisUtils;
import com.papla.cloud.common.core.constants.AuthConstants;
import com.papla.cloud.gateway.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;


/**
 * 安全拦截全局过滤器
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityGlobalFilter implements GlobalFilter, Ordered {

    private final RedisUtils redisUtils;


    @Value("${spring.profiles.active}")
    private String env;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 非JWT或者JWT为空不作处理
        String token = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_KEY);
        if (StringUtils.isAllBlank(token) || !token.startsWith(AuthConstants.AUTHORIZATION_PREFIX)) {
            return chain.filter(exchange);
        }

        // 解析JWT
        token = token.replace(AuthConstants.AUTHORIZATION_PREFIX, Strings.EMPTY);
        JWSObject jwsObject = JWSObject.parse(token);
        String payload = jwsObject.getPayload().toString();
        JSONObject jsonObject = JSONUtil.parseObj(payload);

        // 以jti为key判断redis的是否存在线用户
        String jti = jsonObject.getStr(AuthConstants.JWT_JTI);
        // 不存在拦截访问
        if(!redisUtils.hasKey(AuthConstants.ONLINE_KEY+jti)){
            // 如果是退出接口则放行
            if(!exchange.getRequest().getURI().getPath().contains("logout")){
                return ResponseUtils.writeErrorInfo(response, HttpStatus.UNAUTHORIZED,"当前登录状态已过期，请重新登录！");
            }
        }else{//存在则续期用户在线时间
            redisUtils.expire(AuthConstants.ONLINE_KEY + jti, AuthConstants.ONLINE_RENEW);
        }

        // request写入JWT的载体信息
        request = exchange.getRequest().mutate().header(AuthConstants.JWT_PAYLOAD_KEY, URLEncoder.encode(payload, "UTF-8")).build();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
