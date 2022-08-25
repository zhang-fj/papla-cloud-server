package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import java.util.ArrayList;
import java.util.List;

import com.papla.cloud.workflow.util.PlatformConstants;
import com.papla.cloud.workflow.util.PlatformUtil;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ActivityPropertyBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TaskNoticeBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: NotificationActivityHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 通知节点运行方法实现
 * @date 2021年8月29日 下午1:23:36
 */
public class NotificationActivityHandler extends ResponseActivityHandler {

    public void doStart(ProcessInstanceBean bean) throws Exception {
    }

    public void doRun(ProcessInstanceBean bean) throws Exception {
        try {

            // 修改抄送节点状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityBean.setInstanceCode(bean.getInstanceCode());
            activityHandler.getActivityStateHandler().toRun(activityBean);

            // 修改动态审批列表状态
            if (activityBean.getQueueBean() != null) {
                this.modifyDynaNodeStatus(activityBean.getQueueBean(), WorkFlowConstants.TASK_STATE_RUNNING);
            }

            // 判断节点是否已经被系统规避处理
            if (activityBean.isAvoidFlag()) {  // 已经被系统规避处理

                // 节点配置执行人
                List<ActivityExecutorBean> cfgExecutors = activityBean.getActivityConfigExecutorList();

                // 设置当前节点审批人信息
                this.setCurrentNodeApprovers(bean.getAttrList(), cfgExecutors);

                // 节点执行前函数
                this.processActivityFunc(bean, activityBean, WorkFlowConstants.A_BEFORE_EVENT, WorkFlowConstants.A_BEFORE_FUNC);

                // 发送规避待办  即待办状态为 closed  modified by qp at 16/03/31
                this.getWorkItemService().createAvoidWorkItem(bean, cfgExecutors);

                // 设置当前节点状态对象
                activityHandler.setActivityStateHandler(getEngineCommonHandler().getActivityStateHandler(WorkFlowConstants.NODE_RUNNING_STATE));

                // 完成节点处理
                ActionEvent actionEvent = new ActionEvent(bean, "doCompleteActivity");
                this.getEngineEvent().notifyObserver(actionEvent);

            } else {    // 未被系统规避

                // 节点属性
                List<ActivityPropertyBean> actPropList = (List<ActivityPropertyBean>) activityBean.getActPropertyBeanList();

                // 手动选人标志
                String isSelectUser = PropertyHandlerUtil.findPropertyValue(actPropList, WorkFlowConstants.A_IS_ASSGIN_EXECUTOR);

                // 取得手动指定的执行人
                List<ActivityExecutorBean> userIdList = new ArrayList<ActivityExecutorBean>();

                if ("Y".equals(isSelectUser)) {    // 手动选人
                    userIdList = bean.getChoosedUserList();
                } else if ("N".equals(isSelectUser) && activityBean.getActivityExecutorList().size() > 1) {    // 非手动选人, 但最终执行人为多个
                    userIdList = bean.getChoosedUserList();
                } else {    // 非手动选人
                    userIdList = activityBean.getActivityExecutorList();
                }

                // 设置当前节点审批人信息
                this.setCurrentNodeApprovers(bean.getAttrList(), userIdList);

                // 节点执行前函数
                this.processActivityFunc(bean, activityBean, WorkFlowConstants.A_BEFORE_EVENT, WorkFlowConstants.A_BEFORE_FUNC);

                // 发送待办
                List<TaskNoticeBean> notices = this.getWorkItemService().createNormalWorkItem(bean, userIdList);

                // 执行提醒处理
                String isNotice = PropertyHandlerUtil.findPropertyValue(actPropList, WorkFlowConstants.A_IS_NOTICE); // 是否提醒
                String noticeMode = PlatformUtil.getSystemParamVal(PlatformConstants.NOTICE_MODE);     // 提醒模式

                if ("Y".equals(isNotice) && WorkFlowConstants.NOTICE_MODE_DETAIL.equals(noticeMode)) {
                    handlerTaskNotice(notices);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void doTerminate(ProcessInstanceBean bean) throws Exception {
    }

    public void doComplete(ProcessInstanceBean bean) throws Exception {
        try {
            // 修改抄送节点状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toComplete(activityBean);

            // 修改动态审批列表状态
            if (activityBean.getQueueBean() != null) {
                this.modifyDynaNodeStatus(activityBean.getQueueBean(), WorkFlowConstants.TASK_STATE_CLOSED);
            }

            // 判断节点是否已经被系统规避处理
            if (activityBean.isAvoidFlag()) {  // 已经规避处理

                // 执行节点后处理事件
                this.processActivityFunc(bean, activityBean, WorkFlowConstants.A_AFTER_EVENT, WorkFlowConstants.A_AFTER_FUNC);

                // 获取迁出线信息
                TransitionBean transBean = activityHandler.getActivityBean().getMoveInTransition();

                // 调用函数
                if (transBean != null) {
                    this.processTransition(bean, transBean);
                }

                // 计算执行路径
                bean = this.setExcePath(bean);

                // 设置当前节点
                bean = this.getEngineCommonHandler().setCurrentActivityHandler(bean);

                // 处理当前节点事件
                ActionEvent event = new ActionEvent(bean, "sigal");
                this.getEngineEvent().notifyObserver(event);

            } else {

                // 执行关闭处理
                if ("Y".equals(bean.getIsTimeout())) {
                    // 处理超时待办
                    this.getWorkItemService().closeTimeoutTask(bean, WorkFlowConstants.NOTICE_NODE);
                } else {
                    // 处理正常待办
                    //this.getWorkItemService().closeTask(this.getNewWorkItemBean(bean,null));
                    if (activityBean.isDoReject()) {
                        // 驳回流程
                        this.getProcessEngine().rejectProcess(bean);
                    } else {
                        // 执行关闭处理
                        WorkItemBean currentWorkItemBean = this.getNewWorkItemBean(bean, bean.getLineCategory());
                        this.getWorkItemService().closeTask(currentWorkItemBean);

                        // 关闭节点下其他 open 状态的待办
                        String insId = currentWorkItemBean.getInstanceId();
                        String actId = currentWorkItemBean.getActId();
                        String tskId = currentWorkItemBean.getTaskId();
                        this.getWorkItemService().closeCountersignTask(insId, actId, tskId);
                    }

                }

                // 执行节点后处理事件
                this.processActivityFunc(bean, activityBean, WorkFlowConstants.A_AFTER_EVENT, WorkFlowConstants.A_AFTER_FUNC);

                // 处理连线状态信息默认需要处理, 如果当前节点为动态通知节点且不跨节点则不需要处理连线状态
                if (!WorkFlowConstants.TASK_BTN_LOOP_AGREE.equals(activityBean.getChooseLineCode())) {
                    // 获取迁出线信息
                    String moveOutTransId = bean.getChooseMoveOutLineId();
                    List<TransitionBean> transList = activityBean.getTransitionList();
                    for (TransitionBean transBean : transList) {
                        if (moveOutTransId.equals(transBean.getId())) {
                            this.processTransition(bean, transBean);
                            break;
                        }
                    }
                }

                // 计算执行路径
                bean = this.setExcePath(bean);

                // 设置当前节点
                bean = this.getEngineCommonHandler().setCurrentActivityHandler(bean);

                // 调度节点处理
                ActionEvent event = new ActionEvent(bean, "sigal");
                this.getEngineEvent().notifyObserver(event);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
