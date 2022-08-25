package com.papla.cloud.logging.client.fallback;

import com.papla.cloud.common.logging.dto.LogDTO;
import com.papla.cloud.logging.client.LoggingFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingFeignFallbackClient implements LoggingFeignClient {

    @Override
    public ResponseEntity<Object> save(LogDTO logDTO) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
