package com.papla.cloud.workflow.engine.mapper;

import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessDeployMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 流程部署Mapper： tab - [PAPLA_WF_PROC_DEPLOY]
 * @date 2021年8月24日 下午5:54:06
 */
public interface ProcessDeployMapper {

    /**
     * @param processBean
     * @return void
     * @throws Exception
     * @Title saveProcessDeploy
     * @Description TODO 保存【流程部署】信息
     */
    public void saveProcessDeploy(ProcessDeployBean processBean) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title updateDeployStatus
     * @Description TODO 修改【流程部署】状态
     */
    public void updateProcessDeployStatus(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return Integer
     * @throws Exception
     * @Title getMaxVersionByProcessId
     * @Description TODO 根据【流程设计】ID获取最大版本
     */
    public Integer getMaxVersionByProcessId(String processId) throws Exception;

    /**
     * @param processCode
     * @return String
     * @throws Exception
     * @Title getDeployIdByProcessCode
     * @Description TODO 根据【流程编码】获取【流程部署ID】
     */
    public String getProcessDeployIdByProcessCode(String processCode) throws Exception;

    /**
     * @param deployId
     * @return ProcessDeployBean
     * @throws Exception
     * @Title getProcessDeployById
     * @Description TODO 根据【流程部署ID】获取【流程部署】信息
     */
    public ProcessDeployBean getProcessDeployById(String deployId) throws Exception;

}
