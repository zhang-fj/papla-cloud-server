package com.papla.cloud.workflow.engine.rest;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.designer.service.ProcessDesignerService;
import com.papla.cloud.workflow.engine.modal.ApplicationBean;
import com.papla.cloud.workflow.engine.modal.BizStatusEnumBean;
import com.papla.cloud.workflow.engine.modal.EntityFormBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangfj
 * @Title: ProcessDesignerController.java
 * @Description: TODO   流程设计器后台数据处理服务接口
 * @date 2021-08-03
 */

@RestController
@RequiredArgsConstructor
@Api(tags = "流程设计器")
@RequestMapping("/design/")
public class ProcessDesignerController {

    private final ProcessDesignerService service;

    @Log("根据流程ID获取流程信息")
    @ApiOperation("根据流程ID获取流程信息")
    @GetMapping(value = "getProcessJsonById")
    public ResponseEntity<Map<String, Object>> getProcessJsonById(String processId) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.readValue(service.getProcessJsonById(processId), Map.class), HttpStatus.OK);
    }

    @Log("保存流程图")
    @ApiOperation("保存流程图")
    @PostMapping(value = "saveProcessConfig")
    public ResponseEntity<String> saveProcessConfig(@RequestBody Map<String, Object> param) throws Exception {
        return new ResponseEntity<>(service.saveProcessConfig((String) param.get("id"),param), HttpStatus.OK);
    }

    @Log("绘制流程图：状态值列表")
    @ApiOperation("获取状态值列表")
    @GetMapping(value = "getProcessDesignStatus")
    public ResponseEntity<List<BizStatusEnumBean>> getProcessDesignStatus(String statusName, String entityId) throws Exception {
        return new ResponseEntity<>(service.getProcessDesignStatus(statusName, entityId), HttpStatus.OK);
    }

    @Log("绘制流程图：根据流程Id获取实体属性变量")
    @ApiOperation("绘制流程图：根据流程Id获取实体属性变量")
    @GetMapping(value = "getProcessDesignResources")
    public ResponseEntity<Map<String, Object>> getProcessDesignResources(String entityId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(WorkFlowConstants.INNER_VAR_FORM, service.getProcessDesignResourcesByType(entityId, WorkFlowConstants.INNER_VAR_FORM));
        map.put(WorkFlowConstants.INNER_VAR_EXECUTOR, service.getProcessDesignResourcesByType(entityId, WorkFlowConstants.INNER_VAR_EXECUTOR));
        map.put(WorkFlowConstants.INNER_VAR_TITLE, service.getProcessDesignResourcesByType(entityId, WorkFlowConstants.INNER_VAR_TITLE));
        map.put(WorkFlowConstants.INNER_VAR_CONDITION, service.getProcessDesignResourcesByType(entityId, WorkFlowConstants.INNER_VAR_CONDITION));
        map.put(WorkFlowConstants.INNER_VAR_COMMIT_USER, service.getProcessDesignResourcesByType(entityId, WorkFlowConstants.INNER_VAR_COMMIT_USER));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Log("绘制流程图：获取流程关联表单信息")
    @ApiOperation("绘制流程图：获取流程关联表单信息")
    @GetMapping(value = "getProcessDesignForms")
    public ResponseEntity<List<EntityFormBean>> getProcessDesignForms(String entityId, String clientType, String formDesc, String formId) throws Exception {
        return new ResponseEntity<>(service.getProcessDesignForms(entityId, clientType, formDesc, formId), HttpStatus.OK);
    }

    @Log("绘制流程图：获取流程配置节点岗位")
    @ApiOperation("绘制流程图：获取流程配置节点岗位")
    @PostMapping(value = "getProcessDesignPosts")
    public ResponseEntity<TabPage<Map<String,String>>> getProcessDesignPosts(@RequestBody Map<String,Object> params) throws Exception {
        return new ResponseEntity<>(service.getProcessDesignPosts(params), HttpStatus.OK);
    }

    @Log("绘制流程图：获取流程配置节点用户")
    @ApiOperation("绘制流程图：获取流程配置节点用户")
    @PostMapping(value = "getUsers")
    public ResponseEntity<Map<String, Object>> getUsers(@RequestBody Map<String,Object> params){
        ApplicationBean applicationBean = service.getApplicationByProcId((String)params.get("processId"));
        String postUrl = applicationBean.getWebUrl()+applicationBean.getParamValue(WorkFlowConstants.APP_USER_URL);
        return new ResponseEntity<>(JSONUtil.parseObj(HttpRequest.post(postUrl).body(JSONUtil.toJsonStr(params)).execute().body()), HttpStatus.OK);
    }


}
