package com.papla.cloud.workflow.engine.rest;

import java.util.Map;

import javax.annotation.Resource;

import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.workflow.engine.monitor.ProcessMonitorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(tags = "流程监控 ")
@RequestMapping("/monitor/")
public class ProcessMonitorController {

    @Resource
    private ProcessMonitorService processMonitorService;

    @Log("根据流程实例编码获取流程图")
    @ApiOperation("根据流程实例编码获取流程图")
    @RequestMapping(value = "getProcessJsonByInstanceCode", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getProcessJsonByInstanceCode(String instanceCode) throws Exception {
        return new ResponseEntity<>(processMonitorService.getProcessJsonByInstanceCode(instanceCode), HttpStatus.OK);
    }

    @Log("根据任务ID获取流程图")
    @ApiOperation("根据任务ID获取流程图")
    @RequestMapping(value = "getProcessJsonByTaskId", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getProcessJsonByTaskId(String taskId) throws Exception {
        return new ResponseEntity<>(processMonitorService.getProcessJsonByTaskId(taskId), HttpStatus.OK);
    }

}
