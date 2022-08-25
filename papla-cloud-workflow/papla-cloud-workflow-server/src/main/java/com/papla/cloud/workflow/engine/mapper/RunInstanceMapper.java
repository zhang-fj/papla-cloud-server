package com.papla.cloud.workflow.engine.mapper;

import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: RunInstanceMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description:    【流程实例】Mapper : Tab - [PAPLA_WF_RUN_INSTANCE]
 * @date 2021年8月24日 下午2:38:58
 */
public interface RunInstanceMapper {

    /**
     * @param processInstanceBean
     * @return void
     * @throws Exception
     * @Title saveInstance
     * @Description TODO 创建【流程实例】
     */
    public void saveInstance(ProcessInstanceBean processInstanceBean) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title updateInstanceState
     * @Description TODO 修改【流程实例】状态
     */
    public void updateInstanceState(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title updateInstanceBusiness
     * @Description TODO 更新【流程实例】对应的【业务信息】
     */
    public void updateInstanceBusiness(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title updateBizState
     * @Description TODO 更新【流程实例】对应的【业务状态】
     */
    public void updateBizState(Map<String, Object> params) throws Exception;

    /**
     * @param processInstanceBean
     * @return void
     * @throws Exception
     * @Title modifyInsBaseInfo
     * @Description TODO 更新【流程实例】基本信息
     */
    public void modifyInsBaseInfo(ProcessInstanceBean processInstanceBean) throws Exception;

    /**
     * @param params
     * @return ProcessInstanceBean
     * @throws Exception
     * @Title getBaseInstanceBean
     * @Description TODO 获取【流程实例】基本信息
     */
    public ProcessInstanceBean getBaseInstanceBean(Map<String, Object> params) throws Exception;

    /**
     * @param instanceId
     * @return String
     * @throws Exception
     * @Title getRunInstanceCodeById
     * @Description TODO 根据【流程实例ID】查询【流程实例CODE】
     */
    public String getRunInstanceCodeById(String instanceId) throws Exception;

    /**
     * @param taskId
     * @return String
     * @throws Exception
     * @Title getRunInstanceIdByTaskId
     * @Description TODO 根据【流程任务ID】查询【流程实例ID】
     */
    public String getRunInstanceIdByTaskId(String taskId) throws Exception;

    /**
     * @param instanceCode
     * @return String
     * @throws Exception
     * @Title getInstanceState
     * @Description TODO 获取【流程实例】状态信息
     */
    public String getInstanceState(String instanceCode) throws Exception;

}
