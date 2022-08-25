package com.papla.cloud.workflow.engine.run.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.papla.cloud.workflow.engine.mapper.NoticeMapper;
import com.papla.cloud.workflow.util.PlatformUtil;
import com.papla.cloud.workflow.util.XipUtil;
import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.mapper.ApplicationMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActivityMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActivityPropertyMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsEntityAttrValueMapper;
import com.papla.cloud.workflow.engine.mapper.WorkItemMapper;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.HolidayRuleBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.dao.WorkItemDAO;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: WorkItemDAOImpl.java
 * @Package com.papla.cloud.wf.run.dao.impl
 * @Description: 待办处理服务接口实现类
 * @date 2021年8月30日 下午6:58:21
 */
@Repository("workItemDAO")
public class WorkItemDAOImpl implements WorkItemDAO {

    @Resource
    private WorkItemMapper workItemMapper;

    @Resource
    private ProcessDeployActivityMapper processDeployActivityMapper;

    @Resource
    private NoticeMapper noticeMapper;

    public void createWorkItem(WorkItemBean workItemBean) throws Exception {
        try {
            workItemBean.setDbType(PlatformUtil.getDbType());
            workItemMapper.createWorkItem(workItemBean);
        } catch (Exception e) {
            throw e;
        }
    }

    ;

    public void modifyWorkItem(WorkItemBean workItemBean) throws Exception {
        try {
            workItemMapper.modifyWorkItem(workItemBean);
        } catch (Exception e) {
            throw e;
        }
    }

    public void modifyWorkItem(Map<String, Object> params) throws Exception {
        try {

            if (params.get("updateDt") == null) {
                params.put("updateDt", CurrentUserUtil.getCurrentDate());
            }
            if (params.get("updateBy") == null) {
                params.put("updateBy", CurrentUserUtil.getCurrentUserId());
            }

            workItemMapper.updateWorkItem(params);
        } catch (Exception e) {
            throw e;
        }
    }

    public void disableTask(String instanceId) throws Exception {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("currentInstanceId", instanceId);

            params.put("taskEffective", WorkFlowConstants.TASK_EFFECTIVE_N);
            workItemMapper.updateWorkItem(params);

        } catch (Exception e) {
            throw e;
        }
    }

    public List<TransitionBean> getMoveOutTransitions(String taskId) throws Exception {
        return workItemMapper.getMoveOutTransitions(taskId);
    }

    public ActivityBean getBaseActivityBean(String insId, String taskId) throws Exception {
        HashMap<String, Object> hashmap = new HashMap<String, Object>();
        hashmap.put("insId", insId);
        hashmap.put("taskId", taskId);
        return processDeployActivityMapper.getBaseActivityBean(hashmap);
    }

    @Override
    public List<WorkItemBean> getRunWorkItemListByInstanceId(String instanceId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", instanceId);
        params.put("createType", "notcopyto");
        return workItemMapper.getWorkItemList(params);
    }


    @Override
    public List<WorkItemBean> getAllRunWorkItemListByInstanceId(String instanceId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", instanceId);
        return workItemMapper.getWorkItemList(params);
    }

    @Override
    public List<WorkItemBean> getAllArchWorkItemListByInstanceId(String instanceId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", instanceId);
        params.put("arch", "Y");
        return workItemMapper.getWorkItemList(params);
    }

    @Override
    public List<WorkItemBean> getUserTaskList(Map<String, Object> params) throws Exception {
        return workItemMapper.getUserTaskList(params);
    }

    public WorkItemBean getWorkItemBean(String taskId) throws Exception {
        // 查询运行期待办信息
        WorkItemBean bean = workItemMapper.getWorkItemTaskId(taskId);
        // 查询归档期待办信息
        if (bean == null) {
            bean = workItemMapper.getArchedWorkItemTaskId(taskId);
        }
        return bean;
    }

    public String getConsultTaskId(String taskId) throws Exception {
        return workItemMapper.getConsultTaskId(taskId);
    }

    public HashMap<String, String> getIdListByTaskId(String taskId) throws Exception {
        return workItemMapper.getIdListByTaskId(taskId);
    }

    public void closeCancelTask(ProcessInstanceBean bean) throws Exception {

        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("currentInstanceId", bean.getInstanceId());

            params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
            params.put("endDate", CurrentUserUtil.getCurrentDate());
            params.put("closeCause", WorkFlowConstants.TASK_CLOSE_RECALL);
            params.put("updateDt", CurrentUserUtil.getCurrentDate());
            params.put("updateBy", CurrentUserUtil.getCurrentUserId());

            // 关闭提交人待办
            params.put("createType", WorkFlowConstants.TASK_CREATE_START);
            params.put("result", XipUtil.getMessage("XIP_WF_DAO_0008", null));
            workItemMapper.updateWorkItem(params);

            // 关闭抄送待办
            params.put("createType", WorkFlowConstants.TASK_CREATE_COPYTO);
            params.remove("result");
            workItemMapper.updateWorkItem(params);

            // 关闭正常待办
            params.put("createType", WorkFlowConstants.TASK_CREATE_NORMAL);
            params.put("result", XipUtil.getMessage("XIP_WF_DAO_0008", null));
            workItemMapper.updateWorkItem(params);

            // 关闭征询待办
            params.put("createType", WorkFlowConstants.TASK_CREATE_CONSULT);
            workItemMapper.updateWorkItem(params);

            // 失效所有待办信息
            params.clear();
            params.put("currentInstanceId", bean.getInstanceId());
            params.put("taskEffective", WorkFlowConstants.TASK_EFFECTIVE_N);
            params.put("updateDt", CurrentUserUtil.getCurrentDate());
            params.put("updateBy", CurrentUserUtil.getCurrentUserId());
            workItemMapper.updateWorkItem(params);

        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: getHolidayRule</p>
     * <p>Description: 取得假期规则 </p>
     * @param executors
     * @param dateStr
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.WorkItemDAO#getHolidayRule(java.util.List, java.lang.String)
     */
    public HashMap<String, HolidayRuleBean> getHolidayRule(List<ActivityExecutorBean> executors, String dateStr) throws Exception {
        HashMap<String, HolidayRuleBean> hashmap = null;
        try {
            // 查询数据库类型
            String dbType = PlatformUtil.getDbType();

            // 封装参数
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("executors", executors);
            map.put("dateStr", dateStr);
            map.put("dbType", dbType);

            // 执行查询
            List<HolidayRuleBean> list = workItemMapper.getHolidayRule(map);

            // 执行转换
            if (list != null && list.size() > 0) {
                hashmap = new HashMap<String, HolidayRuleBean>();
                for (HolidayRuleBean rule : list) {
                    hashmap.put(rule.getMasterUserId(), rule);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return hashmap;
    }

    public void createNotice(HashMap<String, Object> map) throws Exception {
        String dbType = PlatformUtil.getDbType();
        map.put("dbType", dbType);
        // 保存通知信息
        noticeMapper.storeNotice(map);
    }

    ;

    public void createUserNotice(HashMap<String, Object> map) throws Exception {
        noticeMapper.storeUserNotice(map);
    }

    ;

    public String getApproveHisTasks(String instanceId) throws Exception {
        String content = "<p><b>" + XipUtil.getMessage("XIP_WF_DAO_0009", null) + ":</b></p><table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\"><tr><th>"
                + XipUtil.getMessage("XIP_WF_DAO_0010", null) + "</th><th>" +
                XipUtil.getMessage("XIP_WF_DAO_0011", null) + "</th><th>" +
                XipUtil.getMessage("XIP_WF_DAO_0012", null) + "</th><th>" +
                XipUtil.getMessage("XIP_WF_DAO_0013", null) + "</th></tr>";
        List<HashMap<String, Object>> tasks = workItemMapper.getHisTasks(instanceId);
        if (tasks != null && tasks.size() > 0) {
            for (HashMap<String, Object> task : tasks) {
                content = content.concat("<tr><td>").concat(String.valueOf(task.get("ACT_NAME"))).concat("</td><td>").concat(String.valueOf(task.get("EXECUTEUSERNAME"))).concat("</td><td>").concat(String.valueOf(task.get("RESULT"))).concat("</td><td>").concat(String.valueOf(task.get("APPROVE_COMMENT"))).concat("</td></tr>");
            }

        }
        content = content.concat("</table>");
        return content;
    }

    /**
     * getHasSendExecutors:(获取已经发送了待办的人员信息)
     *
     * @param insId 实例id
     * @param actId 节点id
     * @return
     * @throws Exception
     */
    @Override
    public HashMap<String, List<String>> getHasSendExecutors(String insId, String actId) throws Exception {
        HashMap<String, List<String>> retMap = new HashMap<String, List<String>>();

        // 查询待办执行人
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("insId", insId);
        map.put("actId", actId);
        List<HashMap<String, String>> list = workItemMapper.getHasSendExecutors(map);

        // 处理执行人信息
        List<String> assignUsers = new ArrayList<String>();
        List<String> executeUsers = new ArrayList<String>();
        for (HashMap<String, String> hashmap : list) {
            String au = hashmap.get("ASSIGN_USER");
            String eu = hashmap.get("EXECUTE_USER");
            if (!assignUsers.contains(au)) {
                assignUsers.add(au);
            }
            if (!au.equals(eu) && !executeUsers.contains(eu)) {
                executeUsers.add(eu);
            }
        }

        // 返回值
        retMap.put("assign", assignUsers);
        retMap.put("execute", executeUsers);

        return retMap;
    }

    @Override
    public void revokeAcrossAuthPeriodTask() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dbType", PlatformUtil.getDbType());
        workItemMapper.revokeAcrossTasks(map);
    }

}
