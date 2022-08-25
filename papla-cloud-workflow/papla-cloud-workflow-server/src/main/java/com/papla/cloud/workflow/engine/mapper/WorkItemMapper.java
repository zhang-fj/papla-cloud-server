package com.papla.cloud.workflow.engine.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.HolidayRuleBean;
import com.papla.cloud.workflow.engine.modal.TimeOutBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: WorkItemMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 待办任务Mapper : Tab - [PAPLA_WF_RUN_INS_TASK]
 * @date 2021年8月30日 下午7:52:37
 */
public interface WorkItemMapper {

    /**
     * @param workItemBean
     * @return void
     * @throws Exception
     * @Title createWorkItem
     * @Description TODO    创建一条待办信息
     */
    void createWorkItem(WorkItemBean workItemBean) throws Exception;

    /**
     * @param workItemBean
     * @return void
     * @throws Exception
     * @Title modifyWorkItem
     * @Description TODO 修改待办信息
     */
    void modifyWorkItem(WorkItemBean workItemBean) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title updateWorkItem
     * @Description TODO    批量修改待办信息
     */
    void updateWorkItem(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title revokeHolidayTasks
     * @Description TODO    撤回假期待办信息
     */
    void revokeHolidayTasks(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title revokeAcrossTasks
     * @Description TODO    回收逾期未审批的待办审批权限
     */
    void revokeAcrossTasks(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title updateWorkItemUsers
     * @Description TODO    修改待办的所有人或执行人信息
     */
    void updateWorkItemUsers(Map<String, Object> params) throws Exception;

    /**
     * @param userId
     * @return List<WorkItemBean>    返回类型
     * @Title: getWorkItemList
     * @Description:
     */
    List<WorkItemBean> getWorkItemList(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    List<WorkItemBean> getUserTaskList(Map<String, Object> params);

    /**
     * @param taskId
     * @return List<TransitionBean>    返回类型
     * @Title: getMoveOutTransitions
     * @Description: 查询节点迁出线集合
     */
    List<TransitionBean> getMoveOutTransitions(String taskId);

    /**
     * @param taskId
     * @return WorkItemBean    返回类型
     * @Title: getWorkItemTaskId
     * @Description: 查询单条待办信息
     */
    WorkItemBean getWorkItemTaskId(String taskId);

    /**
     * 查询单条归档待办信息
     *
     * @param taskId
     * @return
     */
    WorkItemBean getArchedWorkItemTaskId(String taskId);

    /**
     * @param taskId
     * @return WorkItemBean    返回类型
     * @Title: getWorkItemAndInstance
     * @Description: 查询单条待办信息(含实例信息)
     */
    WorkItemBean getWorkItemAndInstance(String taskId);

    /**
     * @param taskId
     * @return HashMap<String, String>    返回类型
     * @Title: getIdListByTaskId
     * @Description: 根据待办Id, 查询实例Id,流程Id和节点Id
     */
    HashMap<String, String> getIdListByTaskId(String taskId);

    /**
     * @param taskId
     * @return HashMap<String, String>    返回类型
     * @Title: getArchIdListByTaskId
     * @Description: 根据待办Id, 查询已归档实例Id,已归档流程Id和已归档节点Id
     */
    HashMap<String, String> getArchIdListByTaskId(String taskId);

    /**
     * @param formId
     * @return String    返回类型
     * @Title: getFormById
     * @Description: 查询流程关联表单信息
     */
    String getFormById(String formId);

    /**
     * @param taskId
     * @return String    返回类型
     * @Title: getConsultTaskId
     * @Description: 根据当前待办ID, 取得其对应的征询待办ID
     */
    String getConsultTaskId(String taskId);

    /**
     * @param hashmap
     * @return List<HolidayRuleBean>    返回类型
     * @Title: getHolidayRule
     * @Description: 取得假期规则信息
     */
    List<HolidayRuleBean> getHolidayRule(HashMap<String, Object> hashmap);

    /**
     * @param dbType
     * @return List<TimeOutBean>    返回类型
     * @Title: findTimeoutWorkItem
     * @Description: 取得可能超时的待办信息
     */
    List<TimeOutBean> findTimeoutWorkItem();

    /**
     * @param strArray
     * @return List<WorkItemBean>    返回类型
     * @Title: getWorkItemByTaskIds
     * @Description: 根据待办ID列表获取待办列表信息
     */
    List<WorkItemBean> getWorkItemByTaskIds(String[] strArray);

    /**
     * @param map
     * @return List<WorkItemBean>    返回类型
     * @Title: getWorkItemByInsAndAct
     * @Description: 根据实例和节点ID获取待办信息
     */
    List<WorkItemBean> getWorkItemByInsAndAct(HashMap<String, Object> map);

    /**
     * @param list
     * @return List<HolidayRuleBean>    返回类型
     * @Title: getHolidayRulesById
     * @Description: 获取假期规则信息
     */
    List<HolidayRuleBean> getHolidayRulesById(List<String> list);


    /**
     * @return List<HashMap < String, Object>>    返回类型
     * @Title: getTotalNoticeTasks
     * @Description: 查询汇总提醒待办信息
     */
    List<HashMap<String, Object>> getTotalNoticeTasks();

    /**
     * @param instance
     * @return List<String>    返回类型
     * @Title: getHisTasks
     * @Description: 查询历史待办信息
     */
    List<HashMap<String, Object>> getHisTasks(String instanceId);

    /**
     * getHasSendExecutors:(获取已经发送了待办的人员信息)
     *
     * @param insId 实例id
     * @param actId 节点id
     * @return
     * @throws Exception
     */
    List<HashMap<String, String>> getHasSendExecutors(HashMap<String, ?> map) throws Exception;

    /**
     * @param insId
     * @throws Exception 设定文件
     * @Title: deleteAllTaskByInsId
     * @Description: 删除流程实例下所有的待办信息
     */
    void deleteAllTaskByInsId(String insId) throws Exception;

}
