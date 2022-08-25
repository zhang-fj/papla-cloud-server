package com.papla.cloud.workflow.engine.designer.service;

import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.workflow.engine.modal.ApplicationBean;
import com.papla.cloud.workflow.engine.modal.BizStatusEnumBean;
import com.papla.cloud.workflow.engine.modal.EntityFormBean;

import java.util.List;
import java.util.Map;

/**
 * @author zhangfj
 * @ClassName: ProcessDesignerService
 * @Description: 流程设计器后台数据处理服务接口
 * @date 2021-08-03
 */
public interface ProcessDesignerService {

    /**
     * @param processId 流程ID
     * @param G6Process 代表流程定义信息的G6格式的流程配置信息
     * @Title: saveProcessConfig
     * @Description: 保存流程配置信息
     */
    String saveProcessConfig(String processId, Map<String, Object> G6Process) throws Exception;

    String getProcessJsonById(String processId) throws Exception;

    List<EntityFormBean> getProcessDesignForms(String entityId, String clientType, String formDesc, String formId) throws Exception;

    List<BizStatusEnumBean> getProcessDesignStatus(String statusName, String entityId) throws Exception;

    List<Map<String, Object>> getProcessDesignResourcesByType(String entityId, String type);


    ApplicationBean getApplicationByProcId(String processId);

    TabPage<Map<String,String>> getProcessDesignPosts(Map<String, Object> params) throws Exception;

}
