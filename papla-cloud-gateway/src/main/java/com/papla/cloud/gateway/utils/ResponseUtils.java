package com.papla.cloud.gateway.utils;

import cn.hutool.json.JSONUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 */
public class ResponseUtils {

    public static Mono<Void> writeErrorInfo(ServerHttpResponse response, HttpStatus status,String message) {
        response.setStatusCode(status);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        String body = JSONUtil.toJsonStr(new HashMap<String,Object>(2){{
            put("message", message);
            put("status",status.value());
        }});
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
    }

}
