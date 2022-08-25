package com.papla.cloud.logging.client;

import com.papla.cloud.common.logging.dto.LogDTO;
import com.papla.cloud.logging.client.fallback.LoggingFeignFallbackClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "papla-cloud-logging-server", fallback = LoggingFeignFallbackClient.class)
public interface LoggingFeignClient {

    @RequestMapping(value = "/logs/save",method = RequestMethod.POST)
    ResponseEntity<Object> save(@RequestBody LogDTO log);

}
