package com.papla.cloud.workflow.engine.run.dao;

import java.util.HashMap;
import java.util.List;

import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

/**
 * @author zhangfj
 * @ClassName: ProcessInstanceDAO
 * @Description: 流程实例数据操作接口
 * @date 2021年08月23日  下午20:58:00
 */
public interface ProcessInstanceDAO {

    /**
     * @param processCode
     * @return String    返回类型
     * @throws Exception
     * @Title: getDeployIdByCode
     * @Description: 根据【流程编码】获取流【程部署ID】
     */
    String getDeployIdByProcessCode(String processCode) throws Exception;

    /**
     * @param deployId
     * @return ProcessBean    返回类型
     * @throws Exception
     * @Title: getProcessDeployById
     * @Description: 根据【流程部署】ID获取【部署信息】
     */
    ProcessDeployBean getProcessDeployById(String deployId) throws Exception;

    /**
     * @param bean
     * @return ProcessInstanceBean    返回类型
     * @throws Exception
     * @Title: createProcessInstance
     * @Description: 创建一个流程实例
     */
    ProcessInstanceBean createProcessInstance(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return ProcessInstanceBean    返回类型
     * @throws Exception
     * @Title: initProcessInstance
     * @Description: 初始化流程实例
     */
    ProcessInstanceBean initProcessInstance(ProcessInstanceBean bean) throws Exception;

    /**
     * @param entityAttrValueList
     * @return void    返回类型
     * @throws Exception
     * @Title: saveInsEntityAttrValue
     * @Description: 保存流程实例实体属性值
     */
    void saveInsEntityAttrValue(List<InstanceEntityAttrBean> entityAttrValueList) throws Exception;

    /**
     * @param bean
     * @return void    返回类型
     * @throws Exception
     * @Title: updateInstanceState
     * @Description: 更新流程实例 状态
     */
    void updateInstanceState(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void    返回类型
     * @throws Exception
     * @Title: updateInstance
     * @Description: 更新流程实例信息
     */
    void updateInstanceBusiness(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void    返回类型
     * @throws Exception
     * @Title: updateBizState
     * @Description: 更新流程实例业务状态, 并更新业务系统的状态信息
     */
    void updateBizState(ProcessInstanceBean bean) throws Exception;

    /**
     * @param instanceId
     * @return ProcessInstanceBean    返回类型
     * @throws Exception
     * @Title: getRunInstanceBeanById
     * @Description: 根据【实例ID】获取运行期【流程实例】基本信息
     */
    ProcessInstanceBean getRunBaseInstanceBeanById(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @return ProcessInstanceBean    返回类型
     * @throws Exception
     * @Title: getBaseRunInstanceBeanByCode
     * @Description: 根据【实例CODE】获取运行期【流程实例】
     */
    ProcessInstanceBean getRunBaseInstanceBeanByCode(String instanceCode) throws Exception;

    /**
     * @param instanceCode
     * @return ProcessInstanceBean    返回类型
     * @throws Exception
     * @Title: getBaseProcessInstanceBeanById
     * @Description: 根据【实例CODE】获取运行期或已归档【流程实例】基本信息
     */
    ProcessInstanceBean getBaseInstanceBeanByCode(String instanceCode) throws Exception;

    /**
     * @param taskId
     * @return
     * @throws Exception 设定文件
     * @Title: getBaseProcessInstanceBeanByTaskId
     * @Description: 根据待办ID, 获取运行期【流程实例】
     */
    ProcessInstanceBean getRunBaseInstanceBeanByTaskId(String taskId) throws Exception;

    /**
     * @param taskId
     * @return
     * @throws Exception 设定文件
     * @Title: getBaseProcessInstanceBeanByTaskId
     * @Description: 根据待办ID，获取运行期或已归档【流程实例】基本信息
     */
    ProcessInstanceBean getBaseInstanceBeanByTaskId(String taskId) throws Exception;

    /**
     * @param instanceCode
     * @return ProcessInstanceBean
     * @throws Exception
     * @Title getProcessInstanceByCode
     * @Description TODO    根据【实例CODE】获取实例信息
     */
    ProcessInstanceBean getProcessInstanceByCode(String instanceCode) throws Exception;

    /**
     * @param taskId
     * @return ProcessInstanceBean
     * @throws Exception
     * @Title getProcessInstanceByTaskId
     * @Description TODO    根据【任务ID】获取实例信息
     */
    ProcessInstanceBean getProcessInstanceByTaskId(String taskId) throws Exception;

    /**
     * @param taskId
     * @return
     * @throws Exception 设定文件
     * @Title: getRunInstanceCodeById
     * @Description: 根据实例Id，获取运行期【流程实例】Code
     */
    String getRunInstanceCodeById(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @return ProcessInstanceBean    返回类型
     * @throws Exception
     * @Title: getProcessInstanceById
     * @Description: 根据实例ID, 获取实例对象
     */
    ProcessInstanceBean getProcessInstanceById(String instanceId) throws Exception;

    /**
     * @param instanceCode
     * @return String    返回类型
     * @throws Exception
     * @Title: getInstanceState
     * @Description: 根据流程实例编码获取流程实例状态信息
     */
    String getInstanceState(String instanceCode) throws Exception;

    /**
     * @param instanceId
     * @return HashMap<String, String>    返回类型
     * @throws Exception
     * @Title: getInsEntityAttrsDataType
     * @Description: 查询流程实例实体属性的数据类型
     */
    HashMap<String, String> getInsEntityAttrsDataType(String instanceId) throws Exception;


}
