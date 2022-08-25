package com.papla.cloud.workflow.engine.run.engine.core.common.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;
import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.ActivityStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.ActivityCancelStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.ActivityCompleteStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.ActivityNotCreatedStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.ActivityRunningStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.ActivityStartStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.ActivityTerminateStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.CountersignActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.EndActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.FunctionActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.NotificationActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.ResponseActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.StartActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.SwitchActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.common.EngineCommonHandler;
import com.papla.cloud.workflow.engine.run.engine.core.instance.ProcessInstanceHandler;
import com.papla.cloud.workflow.engine.run.engine.core.instance.impl.InstanceCancelStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.instance.impl.InstanceCompleteStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.instance.impl.InstanceRunningStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.instance.impl.InstanceStartStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.instance.impl.InstanceTerminateStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.instance.impl.ProcessInstanceHandlerImpl;
import com.papla.cloud.workflow.engine.run.engine.core.task.WorkItemStateHandler;
import com.papla.cloud.workflow.util.AppContext;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EngineCommonHandlerImpl.java
 * @Package com.papla.cloud.wf.run.engine.core.common.impl
 * @Description: 引擎调度器通用接口实现类
 * @date 2021年8月29日 上午11:46:02
 */
@Service("engineCommonHandler")
public class EngineCommonHandlerImpl implements EngineCommonHandler {
    @Resource
    private ActivityDAO activityDAO;

    // 流程实例状态处理类
    @Resource
    private InstanceStartStateHandler instanceStartStateHandler;
    @Resource
    private InstanceRunningStateHandler instanceRunningStateHandler;
    @Resource
    private InstanceCancelStateHandler instanceCancelStateHandler;
    @Resource
    private InstanceTerminateStateHandler instanceTerminateStateHandler;
    @Resource
    private InstanceCompleteStateHandler instanceCompleteStateHandler;

    // 流程实例节点状态处理
    @Resource
    private ActivityStartStateHandler activityStartStateHandler;
    @Resource
    private ActivityRunningStateHandler activityRunningStateHandler;
    @Resource
    private ActivityCancelStateHandler activityCancelStateHandler;
    @Resource
    private ActivityCompleteStateHandler activityCompleteStateHandler;
    @Resource
    private ActivityTerminateStateHandler activityTerminateStateHandler;
    @Resource
    private ActivityNotCreatedStateHandler activityNotCreatedStateHandler;

    public ProcessInstanceBean setCurrentActivityHandler(ProcessInstanceBean bean) throws Exception {

        // 执行路径
        List<ActivityBean> activityBeanList = bean.getProcessExecPath();

        if (activityBeanList != null && activityBeanList.size() > 0) {

            ActivityBean activityBean = activityBeanList.get(0);

            // 根据【流程节点类型】获取【当前流程节点处理类】
            AbstractActivityHandler currentActivityHandler = getActivityHandler(activityBean.getActType());

            if (currentActivityHandler != null) {

                // 将【当前节点处理类】与【当前节点绑定】
                currentActivityHandler.setActivityBean(activityBean);

                // 设置【节点状态处理类】
                currentActivityHandler.setActivityStateHandler(this.getActivityStateHandler(activityBean));

                // 设置【流程实例】的【当前处理类】
                bean.setCurrentActivityHandler(currentActivityHandler);

            }

        }
        return bean;
    }

    public ProcessInstanceBean setCurrentActivityHandler(ActivityBean activityBean) throws Exception {

        ProcessInstanceBean bean = new ProcessInstanceBean();

        // 根据【流程节点类型】获取【当前流程节点处理类】
        AbstractActivityHandler currentActivityHandler = getActivityHandler(activityBean.getActType());

        if (currentActivityHandler != null) {

            // 将【当前节点处理类】与【当前节点绑定】
            currentActivityHandler.setActivityBean(activityBean);

            // 设置【流程实例】的【就当处理类】
            bean.setCurrentActivityHandler(currentActivityHandler);
        }

        return bean;
    }

    public AbstractActivityHandler getActivityHandler(String activityType) {

        AbstractActivityHandler activityHandler = null;

        if (activityType.equals(WorkFlowConstants.START_NODE)) {    // 开始节点
            activityHandler = new StartActivityHandler();
        } else if (activityType.equals(WorkFlowConstants.FUNCTION_NODE)) { // 功能节点
            activityHandler = new FunctionActivityHandler();
        } else if (activityType.equals(WorkFlowConstants.SWITCH_NODE)) { // 判断节点
            activityHandler = new SwitchActivityHandler();
        } else if (activityType.equals(WorkFlowConstants.RESPONSE_NODE)) { // 抄送节点
            activityHandler = new ResponseActivityHandler();
        } else if (activityType.equals(WorkFlowConstants.NOTICE_NODE)) { // 通知节点
            activityHandler = new NotificationActivityHandler();
        } else if (activityType.equals(WorkFlowConstants.COUNTERSIGN_NODE)) { // 会签节点
            activityHandler = new CountersignActivityHandler();
        } else if (activityType.equals(WorkFlowConstants.END_NODE)) {// 结束节点
            activityHandler = new EndActivityHandler();
        }
        ;

        return activityHandler;

    }

    public StartActivityHandler getStartActivityHandler(String instanceId) throws Exception {
        // 获取开始处理节点
        StartActivityHandler startActivityHandler = null;
        try {
            startActivityHandler = activityDAO.getStartActivityHandler(instanceId);

            ActivityBean activityBean = startActivityHandler.getActivityBean();

            // 设置开始节点状态处理类
            ActivityStateHandler activityStateHandler = this.getActivityStateHandler(activityBean);
            startActivityHandler.setActivityStateHandler(activityStateHandler);

        } catch (Exception e) {
            throw e;
        }
        return startActivityHandler;
    }

    public ProcessInstanceHandler getProcessInstanceHandler(ProcessInstanceBean bean) throws Exception {
        ProcessInstanceHandler processInstanceHandler = new ProcessInstanceHandlerImpl();

        // 实例状态
        String insState = bean.getInstanceState();
        if (insState == null) return processInstanceHandler;

        if (insState.equals(WorkFlowConstants.INS_START_STATE)) {    // 启动状态
            processInstanceHandler.setInstanceStateHandler(instanceStartStateHandler);

        } else if (insState.equals(WorkFlowConstants.INS_RUNNING_STATE)) {        // 运行状态
            processInstanceHandler.setInstanceStateHandler(instanceRunningStateHandler);

        } else if (insState.equals(WorkFlowConstants.INS_CANCELED_STATE)) {    // 取消状态
            processInstanceHandler.setInstanceStateHandler(instanceCancelStateHandler);

        } else if (insState.equals(WorkFlowConstants.INS_COMPLETED_STATE)) {    // 完成状态
            processInstanceHandler.setInstanceStateHandler(instanceCompleteStateHandler);

        } else if (insState.equals(WorkFlowConstants.INS_TERMINATE_STATE)) {    // 终止状态
            processInstanceHandler.setInstanceStateHandler(instanceTerminateStateHandler);
        }

        return processInstanceHandler;
    }

    public ActivityStateHandler getActivityStateHandler(ActivityBean activityBean) throws Exception {

        ActivityStateHandler activityStateHandler = null;

        try {
            // 计算节点最新状态
            ActivityStateBean activityStateBean = activityDAO.getLastActivityState(activityBean.getInstanceId(), activityBean.getId());

            String actState = null;

            if (activityStateBean != null) {
                actState = activityStateBean.getActState();
                activityBean.setLastActivityStateBean(activityStateBean);
            }

            activityStateHandler = getActivityStateHandler(actState);

        } catch (Exception e) {
            throw e;
        }
        return activityStateHandler;
    }

    public ActivityStateHandler getActivityStateHandler(String activityState) throws Exception {

        ActivityStateHandler activityStateHandler = null;

        switch (activityState == null ? "other" : activityState) {
            case WorkFlowConstants.NODE_START_STATE:        // 启动状态
                activityStateHandler = activityStartStateHandler;
                break;
            case WorkFlowConstants.NODE_RUNNING_STATE:        // 运行状态
                activityStateHandler = activityRunningStateHandler;
                break;
            case WorkFlowConstants.NODE_CANCELED_STATE:    // 取消状态
                activityStateHandler = activityCancelStateHandler;
                break;
            case WorkFlowConstants.NODE_COMPLETED_STATE:    // 完成状态
                activityStateHandler = activityCompleteStateHandler;
                break;
            case WorkFlowConstants.NODE_TERMINATE_STATE:    // 终止状态
                activityStateHandler = activityTerminateStateHandler;
                break;
            default:
                activityStateHandler = activityNotCreatedStateHandler;
                break;
        }

        return activityStateHandler;
    }

    public WorkItemBean setWorkItemStateHandler(WorkItemBean workItemBean) throws Exception {
        WorkItemStateHandler workItemStateHandler = null;

        String taskState = workItemBean.getTaskState(); // 待办状态

        // 设置待办状态处理类
        if (taskState.equals(WorkFlowConstants.TASK_STATE_OPEN)) {
            workItemStateHandler = (WorkItemStateHandler) AppContext.getApplicationContext().getBean("workItemOpenStateHandler");

        } else if (taskState.equals(WorkFlowConstants.TASK_STATE_HUNGUP)) {
            workItemStateHandler = (WorkItemStateHandler) AppContext.getApplicationContext().getBean("workItemHangUpStateHandler");

        } else if (taskState.equals(WorkFlowConstants.TASK_STATE_CLOSED)) {
            workItemStateHandler = (WorkItemStateHandler) AppContext.getApplicationContext().getBean("workItemCloseStateHandler");
        } else {
            // 扩展处理
        }
        workItemBean.setWorkItemStateHandler(workItemStateHandler);

        return workItemBean;
    }
}
