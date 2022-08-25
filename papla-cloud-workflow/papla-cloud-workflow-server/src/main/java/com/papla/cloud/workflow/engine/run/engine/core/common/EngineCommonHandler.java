package com.papla.cloud.workflow.engine.run.engine.core.common;

import com.papla.cloud.workflow.engine.run.engine.core.activity.ActivityStateHandler;
import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.StartActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.instance.ProcessInstanceHandler;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EngineCommonHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.common
 * @Description: 引擎调度器通用接口
 * @date 2021年8月29日 上午11:44:27
 */
public interface EngineCommonHandler {

    /**
     * @param activityState
     * @return ActivityStateHandler
     * @throws Exception
     * @Title getActivityStateHandler
     * @Description TODO    根据【节点状态】获取【节点状态处理类】
     */
    public ActivityStateHandler getActivityStateHandler(String activityState) throws Exception;

    /**
     * @param bean
     * @return ProcessInstanceBean
     * @throws Exception
     * @Title setCurrentActivityHandler
     * @Description TODO 设置当前节点
     */
    public ProcessInstanceBean setCurrentActivityHandler(ProcessInstanceBean bean) throws Exception;

    /**
     * @param activityBean
     * @return ProcessInstanceBean
     * @throws Exception
     * @Title setCurrentActivityHandler
     * @Description TODO    设置当前节点
     */
    public ProcessInstanceBean setCurrentActivityHandler(ActivityBean activityBean) throws Exception;

    /**
     * @param bean
     * @return ProcessInstanceHandler
     * @throws Exception
     * @Title getProcessInstanceHandler
     * @Description TODO    获取实例状态处理
     */
    public ProcessInstanceHandler getProcessInstanceHandler(ProcessInstanceBean bean) throws Exception;

    /**
     * @param instanceId
     * @return StartActivityHandler
     * @throws Exception
     * @Title getStartActivityHandler
     * @Description TODO    获取实例的开始处理节点
     */
    public StartActivityHandler getStartActivityHandler(String instanceId) throws Exception;

    /**
     * @param workItemBean
     * @return WorkItemBean
     * @throws Exception
     * @Title setWorkItemStateHandler
     * @Description TODO 设置待办状态处理类
     */
    public WorkItemBean setWorkItemStateHandler(WorkItemBean workItemBean) throws Exception;
}
