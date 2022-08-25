package com.papla.cloud.logging.rest;

import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.common.logging.dto.LogDTO;
import com.papla.cloud.common.security.utils.JwtUtils;
import com.papla.cloud.logging.service.LoggingService;
import com.papla.cloud.logging.pojo.dto.LogQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
@Api(tags = "系统：日志管理")
public class LogController {

    private final LoggingService loggingService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        criteria.setLogType("INFO");
        loggingService.download(loggingService.queryAll(criteria), response);
    }

    @ApiOperation("保存日志")
    @PostMapping(value = "/save")
    public ResponseEntity<Object> save(@RequestBody LogDTO log){
        loggingService.save(log);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("导出错误数据")
    @ApiOperation("导出错误数据")
    @GetMapping(value = "/error/download")
    public void downloadErrorLog(HttpServletResponse response, LogQueryCriteria criteria) throws IOException {
        criteria.setLogType("ERROR");
        loggingService.download(loggingService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("日志查询")
    public ResponseEntity<Object> query(LogQueryCriteria criteria, Pageable pageable) {
        criteria.setLogType("INFO");
        return new ResponseEntity<>(loggingService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/user")
    @ApiOperation("用户日志查询")
    public ResponseEntity<Object> queryUserLog(LogQueryCriteria criteria, Pageable pageable) {
        criteria.setLogType("INFO");
        criteria.setBlurry(JwtUtils.getUsername());
        return new ResponseEntity<>(loggingService.queryAllByUser(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/error")
    @ApiOperation("错误日志查询")
    public ResponseEntity<Object> queryErrorLog(LogQueryCriteria criteria, Pageable pageable) {
        criteria.setLogType("ERROR");
        return new ResponseEntity<>(loggingService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    public ResponseEntity<Object> queryErrorLogs(@PathVariable String id) {
        return new ResponseEntity<>(loggingService.findByErrDetail(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/del/error")
    @Log("删除所有ERROR日志")
    @ApiOperation("删除所有ERROR日志")
    public ResponseEntity<Object> delAllErrorLog() {
        loggingService.delAllByError();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/del/info")
    @Log("删除所有INFO日志")
    @ApiOperation("删除所有INFO日志")
    public ResponseEntity<Object> delAllInfoLog() {
        loggingService.delAllByInfo();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
