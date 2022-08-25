package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.util.PlatformConstants;
import com.papla.cloud.workflow.util.PlatformUtil;
import com.papla.cloud.workflow.util.XipUtil;
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
 * @ClassName: CountersignActivityHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 会签节点运行方法实现
 * @date 2021年8月29日 下午1:30:33
 */
public class CountersignActivityHandler extends NotificationActivityHandler {

    public void doStart(ProcessInstanceBean bean) throws Exception {
    }

    public void doRun(ProcessInstanceBean bean) throws Exception {
        try {

            // 修改抄送节点状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toRun(activityBean);

            // 判断节点是否已经被系统规避处理
            if (activityBean.isAvoidFlag()) {  // 已经被系统规避处理

                // 如果节点被规避 , 取节点配置执行人
                List<ActivityExecutorBean> cfgExecutors = activityBean.getActivityConfigExecutorList();
                // 设置当前节点审批人信息
                this.setCurrentNodeApprovers(bean.getAttrList(), cfgExecutors);
                // 节点执行前函数
                this.processActivityFunc(bean, activityBean, WorkFlowConstants.A_BEFORE_EVENT, WorkFlowConstants.A_BEFORE_FUNC);

                // 取得会签规则 、发送关闭待办 ; 系统按"Y"向下执行
                Map<String, String> propertyMap = activityBean.getPropertyMap();

                String rule = (String) propertyMap.get(WorkFlowConstants.A_COUNTERSIGN_RULE);

                // 【一票通过 或抢单】 : 发送待办中只要存在一条待办审批通过, 则节点审批通过
                if (WorkFlowConstants.JOIN_ONE_PASS.equals(rule) || WorkFlowConstants.JOIN_FIRST_PASS.equals(rule)) {
                    // 给配置执行人发送待办
                    this.getWorkItemService().createAvoidWorkItem(bean, cfgExecutors);

                    // 【一票否决 或比例通过】 : 发送待办中所有待办审批通过，则节点审批通过
                } else if (WorkFlowConstants.JOIN_ONE_REFUSE.equals(rule) || WorkFlowConstants.JOIN_PERCENT_PASS.equals(rule)) {
                    // 给配置执行人发送待办
                    this.getWorkItemService().createAvoidWorkItem(bean, cfgExecutors);
                }

                // 设置当前节点状态对象
                activityHandler.setActivityStateHandler(getEngineCommonHandler().getActivityStateHandler(WorkFlowConstants.NODE_RUNNING_STATE));

                // 完成节点处理
                ActionEvent actionEvent = new ActionEvent(bean, "doCompleteActivity");
                this.getEngineEvent().notifyObserver(actionEvent);

            } else {                // 未被系统规避
                // 节点属性
                List<ActivityPropertyBean> actPropList = (List<ActivityPropertyBean>) activityBean.getActPropertyBeanList();

                // 手动选人标志
                String isSelectUser = PropertyHandlerUtil.findPropertyValue(actPropList, WorkFlowConstants.A_IS_ASSGIN_EXECUTOR);
                // 取得手动指定的执行人
                List<ActivityExecutorBean> userIdList = new ArrayList<ActivityExecutorBean>();
                if ("Y".equals(isSelectUser)) {    // 手动选人
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
            // 取得会签节点信息
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();

            // 如果根据节点会签规则判定当前审批人是否为最后一个处理该节点的审批人时, 则关闭会签节点
            if (activityBean.isLastApprover()) {
                activityHandler.getActivityStateHandler().toComplete(activityBean);
            }

            // 判断节点是否已经被系统规避处理
            if (activityBean.isAvoidFlag()) {
                // 节点已经规避处理
                this.processAvoidTask(bean, activityBean);

            } else {
                // 迁出线编码
                List<TransitionBean> moveOutLines = activityBean.getTransitionList();

                // 取得执行线分类属性信息 : Y-同意线 , N-不同意线
                for (TransitionBean transBean : moveOutLines) {
                    if (transBean.getId().equals(bean.getChooseMoveOutLineId())) {
                        activityBean.setMoveInTransition(transBean);
                        break;
                    }
                }

                // 取得会签规则
                Map<String, String> propertyMap = activityBean.getPropertyMap();
                String rule = (String) propertyMap.get(WorkFlowConstants.A_COUNTERSIGN_RULE);

                // 根据会签规则执行节点处理
                if (WorkFlowConstants.JOIN_ONE_PASS.equals(rule) || WorkFlowConstants.JOIN_ONE_REFUSE.equals(rule)) {
                    // 一票通过或一票否决
                    this.completeApprove(bean, activityBean);

                } else if (WorkFlowConstants.JOIN_FIRST_PASS.equals(rule)) {
                    // 抢单
                    this.commonApprove(bean, activityBean);

                } else if (WorkFlowConstants.JOIN_PERCENT_PASS.equals(rule) || WorkFlowConstants.JOIN_PERCENT_REFUSE.equals(rule)) {
                    // 比例通过	或比例驳回
                    this.completeApprove(bean, activityBean);

                } else {
                    HashMap<String, String> tokens = new HashMap<String, String>();
                    tokens.put("actName", activityBean.getName());
                    throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0030", tokens));
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param bean
     * @param actBean
     * @return void    返回类型
     * @throws Exception
     * @Title: processAvoidTask
     * @Description: 节点是否已经被系统规避处理
     */
    private void processAvoidTask(ProcessInstanceBean bean, ActivityBean actBean) throws Exception {
        try {
            // 执行节点后处理事件
            this.processActivityFunc(bean, actBean, WorkFlowConstants.A_AFTER_EVENT, WorkFlowConstants.A_AFTER_FUNC);

            // 处理节点迁出线事件
            List<TransitionBean> transList = actBean.getTransitionList();
            for (TransitionBean transBean : transList) {
                if ("Y".equals(transBean.getCategory())) {
                    this.processTransition(bean, transBean);
                    break;
                }
            }
            // 计算执行路径
            bean = this.setExcePath(bean);

            // 设置当前节点
            bean = this.getEngineCommonHandler().setCurrentActivityHandler(bean);

            // 处理当前节点事件
            ActionEvent event = new ActionEvent(bean, "sigal");
            this.getEngineEvent().notifyObserver(event);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param bean
     * @param actBean
     * @return void    返回类型
     * @throws Exception
     * @Title: schedulingActivityProcess
     * @Description: 调度流程节点向下执行处理
     */
    private void schedulingActivityProcess(ProcessInstanceBean bean, ActivityBean actBean) throws Exception {
        try {
            // 调用节点执行后函数
            this.processActivityFunc(bean, actBean, WorkFlowConstants.A_AFTER_EVENT, WorkFlowConstants.A_AFTER_FUNC);

            // 处理连线函数
            this.processTransition(bean, actBean.getMoveInTransition());

            // 计算执行路径
            bean = this.setExcePath(bean);

            // 设置当前节点
            bean = this.getEngineCommonHandler().setCurrentActivityHandler(bean);

            // 调用下一个节点
            ActionEvent event = new ActionEvent(bean, "sigal");
            this.getEngineEvent().notifyObserver(event);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param bean
     * @param actBean
     * @return void    返回类型
     * @throws Exception
     * @Title: commonProcess
     * @Description: 处理当前信息, 并调度流程节点向下执行处理
     */
    private void commonProcess(ProcessInstanceBean bean, ActivityBean actBean) throws Exception {
        try {
            // 执行关闭处理
            WorkItemBean currentWorkItemBean = this.getNewWorkItemBean(bean, bean.getLineCategory());
            this.getWorkItemService().closeTask(currentWorkItemBean);

            // 关闭节点下其他 open 状态的待办
            String insId = currentWorkItemBean.getInstanceId();
            String actId = currentWorkItemBean.getActId();
            String tskId = currentWorkItemBean.getTaskId();
            this.getWorkItemService().closeCountersignTask(insId, actId, tskId);

            // 调度节点处理
            this.schedulingActivityProcess(bean, actBean);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param bean
     * @param actBean
     * @return void    返回类型
     * @throws Exception
     * @Title: commonApprove
     * @Description: 通用审批处理
     */
    private void commonApprove(ProcessInstanceBean bean, ActivityBean actBean) throws Exception {
        if (actBean.isDoReject()) {
            // 驳回流程
            this.getProcessEngine().rejectProcess(bean);

        } else {
            this.commonProcess(bean, actBean);
        }
    }

    /**
     * @param bean
     * @param actBean
     * @return void    返回类型
     * @throws Exception
     * @Title: completeApprove
     * @Description: 完成审批处理
     */
    private void completeApprove(ProcessInstanceBean bean, ActivityBean actBean) throws Exception {
        // 判断是否为最后一个审批人
        if (actBean.isLastApprover()) {
            this.commonApprove(bean, actBean);
        } else {
            // 关闭当前待办
            this.getWorkItemService().closeTask(this.getNewWorkItemBean(bean, bean.getLineCategory()));
        }
    }
}
