package com.papla.cloud.workflow.engine.deploy.service;

import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;

/**
 * @author zhangfj
 * @ClassName: ProcessDeployService
 * @Description: 流程部署后台数据处理服务接口
 * @date 2021-08-03
 */
public interface ProcessDeployService {

    /**
     * @param  processId
     * @Title: deployProcess
     * @Description: 部署流行图
     */
     String deployProcess(String processId) throws Exception;

    /**
     * enableProcesee:(启用流程)
     *
     * @param processId 流程id
     */
     Map<String, String> enableProcess(String processId) throws Exception;

    /**
     * disableProcess:(禁用流程)
     *
     * @param processId 流程id
     */
     Map<String, String> disableProcess(String processId) throws Exception;

    /**
     * 根据流程部署Id获取流程信息
     *
     * @param deployId
     * @return
     */
     ProcessDeployBean getProcessDeployById(String deployId) throws Exception;

}
