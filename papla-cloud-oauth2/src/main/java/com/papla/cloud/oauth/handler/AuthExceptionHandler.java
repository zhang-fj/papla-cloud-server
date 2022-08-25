package com.papla.cloud.oauth.handler;

import com.papla.cloud.common.web.handler.ApiError;
import com.papla.cloud.common.web.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhangfj
 * 授权异常统一处理
 * @date 2018-11-23
 */
@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler extends GlobalExceptionHandler {

     /**
     * BadCredentialsException
     */
    @ExceptionHandler(InvalidGrantException.class)
    public ResponseEntity<ApiError> badCredentialsException(InvalidGrantException e) {
        // 打印堆栈信息
        String message = "Bad credentials".equals(e.getMessage()) ? "用户名或密码不正确" : e.getMessage();
        log.error(message);
        return buildResponseEntity(ApiError.error(message));
    }

}
