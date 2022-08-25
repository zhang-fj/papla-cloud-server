package com.papla.cloud.workflow.engine.run.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ActivityPropertyBean;
import com.papla.cloud.workflow.engine.modal.HolidayRuleBean;
import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TimeOutBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: WorkItemDAO.java
 * @Package com.papla.cloud.wf.run.dao
 * @Description: 待办处理服务接口
 * @date 2021年8月30日 下午6:59:13
 */
public interface WorkItemDAO {

    /**
     * @param workItemBean
     * @return void
     * @throws Exception
     * @Title createWorkItem
     * @Description TODO    创建一条待办任务
     */
    void createWorkItem(WorkItemBean workItemBean) throws Exception;

    /**
     * @param workItemBean
     * @return void
     * @throws Exception
     * @Title modifyWorkItem
     * @Description TODO 更新待办信息
     */
    void modifyWorkItem(WorkItemBean workItemBean) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title modifyWorkItem
     * @Description TODO 批量修改待办信息
     */
    void modifyWorkItem(Map<String, Object> params) throws Exception;

    /**
     * @param instanceId
     * @return void
     * @throws Exception
     * @Title disableTask
     * @Description TODO    流程撤回或流程驳回时, 失效实例下全部待办信息
     */
    void disableTask(String instanceId) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title closeCancelTask
     * @Description TODO 撤回流程时，关闭open状态下的待办信息
     */
    void closeCancelTask(ProcessInstanceBean bean) throws Exception;

    /**
     * @param instanceId
     * @return
     * @throws Exception
     */
    List<WorkItemBean> getRunWorkItemListByInstanceId(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @return
     * @throws Exception
     */
    List<WorkItemBean> getAllRunWorkItemListByInstanceId(String instanceId) throws Exception;


    /**
     * @param instanceId
     * @return
     * @throws Exception
     */
    List<WorkItemBean> getAllArchWorkItemListByInstanceId(String instanceId) throws Exception;


    /**
     * @param params
     * @return
     * @throws Exception
     */
    List<WorkItemBean> getUserTaskList(Map<String, Object> params) throws Exception;

    /**
     * @param taskId
     * @return List<TransitionBean>    返回类型
     * @throws Exception
     * @Title: getMoveOutTransitions
     * @Description: 根据待办Id, 查询节点迁出线信息
     */
    List<TransitionBean> getMoveOutTransitions(String taskId) throws Exception;

    /**
     * @param insId
     * @param taskId
     * @return ActivityBean    返回类型
     * @throws Exception
     * @Title: getBaseActivityBean
     * @Description: 查询节点基础信息
     */
    ActivityBean getBaseActivityBean(String insId, String taskId) throws Exception;

    /**
     * @param taskId
     * @return WorkItemBean    返回类型
     * @throws Exception
     * @Title: getWorkItemBean
     * @Description: 根据待办id , 查询单条待办
     */
    WorkItemBean getWorkItemBean(String taskId) throws Exception;

    /**
     * @param taskId
     * @return String    返回类型
     * @throws Exception
     * @Title: getConsultTaskId
     * @Description: 根据当前待办ID, 取得其对应的征询待办ID
     */
    String getConsultTaskId(String taskId) throws Exception;

    /**
     * @param taskId
     * @return HashMap<String, String>    返回类型
     * @throws Exception
     * @Title: getIdListByTaskId
     * @Description: 根据待办Id, 查询实例Id和节点Id
     */
    HashMap<String, String> getIdListByTaskId(String taskId) throws Exception;

    /**
     * @param executors
     * @param dateStr
     * @return HashMap<String, String>    返回类型
     * @throws Exception
     * @Title: getHolidayRule
     * @Description: 取得假期规则
     */
    HashMap<String, HolidayRuleBean> getHolidayRule(List<ActivityExecutorBean> executors, String dateStr) throws Exception;

    /**
     * @param map
     * @return void    返回类型
     * @throws Exception
     * @Title: createNotice
     * @Description: 创建通知信息
     */
    void createNotice(HashMap<String, Object> map) throws Exception;

    /**
     * @param map
     * @return void    返回类型
     * @throws Exception
     * @Title: createUserNotice
     * @Description: 创建个人与通知关联信息
     */
    void createUserNotice(HashMap<String, Object> map) throws Exception;

    /**
     * @param instanceId
     * @return String    返回类型
     * @throws Exception
     * @Title: getApproveHisTasks
     * @Description:查询审批历史信息
     */
    String getApproveHisTasks(String instanceId) throws Exception;

    /**
     * getHasSendExecutors:(获取已经发送了待办的人员信息)
     *
     * @param insId 实例id
     * @param actId 节点id
     * @return
     * @throws Exception
     */
    HashMap<String, List<String>> getHasSendExecutors(String insId, String actId) throws Exception;

    /**
     * 撤回超过假期规则授权的待办信息
     *
     * @return
     * @throws Exception
     */
    void revokeAcrossAuthPeriodTask() throws Exception;


}
