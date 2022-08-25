package com.papla.cloud.workflow.engine.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.workflow.engine.deploy.service.ProcessDeployService;
import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author zhangfj
 * @Title: ProcessDeployController.java
 * @Description: TODO   流程部署管理
 * @date 2021-08-03
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "流程部署管理")
@RequestMapping("/deploy/")
public class ProcessDeployController {

    private final ProcessDeployService service;

    @Log("部署流程图")
    @ApiOperation("部署流程图")
    @GetMapping(value = "deployProcess")
    public ResponseEntity<String> deployProcess(String processId) throws Exception {
        return new ResponseEntity<>(service.deployProcess(processId), HttpStatus.OK);
    }

    @Log("根据部署ID获取流程信息")
    @ApiOperation("根据部署ID获取流程信息")
    @GetMapping(value = "getProcessJsonById")
    public ResponseEntity<Map<String, Object>> getProcessJsonById(String deployId) throws Exception {
        ProcessDeployBean deploy = service.getProcessDeployById(deployId);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data = objectMapper.readValue(deploy.getProcessJson(), Map.class);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
