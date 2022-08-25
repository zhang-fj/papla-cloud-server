package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;
import com.papla.cloud.workflow.util.XipUtil;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: StartActivityHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 开始节点运行方法实现
 * @date 2021年8月29日 下午12:32:24
 */
public class StartActivityHandler extends AbstractActivityHandler {

    public void doStart(ProcessInstanceBean bean) throws Exception {
        try {
            String formId = null;
            // 如果开始节点绑定业务表单生成提交人待办信息，否则不生成提交人待办
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            // 取得开始节点绑定表单信息
            formId = PropertyHandlerUtil.findPropertyValue(activityBean.getPropertyMap(), WorkFlowConstants.A_FORM);
            if (StringUtils.isNotBlank(formId)) {
                // 创建提交人待办信息
                this.getWorkItemService().createSubmitterWorkItem(bean);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void doRun(ProcessInstanceBean bean) throws Exception {
        try {

            // 修改当前阶段状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toRun(activityBean);

            // 执行开始节点函数事件
            this.processActivityFunc(bean, activityBean, WorkFlowConstants.A_FUNC_TYPE, WorkFlowConstants.A_FUNC_NAME);

            // 设置当前节点状态对象
            activityHandler.setActivityStateHandler(getEngineCommonHandler().getActivityStateHandler(WorkFlowConstants.NODE_RUNNING_STATE));

            // 完成节点处理
            ActionEvent actionEvent = new ActionEvent(bean, "doCompleteActivity");
            this.getEngineEvent().notifyObserver(actionEvent);

        } catch (Exception e) {
            throw e;
        }
    }

    public void doTerminate(ProcessInstanceBean bean) throws Exception {
    }

    public void doComplete(ProcessInstanceBean bean) throws Exception {
        try {
            // 修改开始节点状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toComplete(activityBean);

            // 关闭提交人待办信息
            List<WorkItemBean> workItemList = bean.getWorkItemList();
            if (workItemList != null && workItemList.size() > 0) {
                for (WorkItemBean itemBean : workItemList) {
                    if (itemBean.getActId().equals(activityBean.getId()) && itemBean.getTaskState().equals(WorkFlowConstants.TASK_STATE_OPEN)) {

                        // 设置待办信息
                        itemBean = this.setNewWorkItemBean(itemBean, bean);
                        itemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0027", null));

                        // 提交人待办审批意见
                        String comment = PropertyHandlerUtil.findEntityPropertyValue(bean.getAttrList(), WorkFlowConstants.E_SUBMITTER_COMMENT);
                        itemBean.setApproveCommnet(comment);

                        // 关闭待办
                        this.getWorkItemService().closeTask(itemBean);
                        break;
                    }
                }
            }

            // 获取迁出线信息, 对于开始节点只有一条迁出线
            List<TransitionBean> transList = activityHandler.getActivityBean().getTransitionList();
            if (transList != null && transList.size() > 0) {
                TransitionBean transBean = transList.get(0);
                transBean.setInstanceId(bean.getInstanceId());
                transBean.setState(WorkFlowConstants.TRANS_STATE_PASS);
                // 处理连线状态信息
                this.processTransition(bean, transBean);
            }

            // 设置计算路径
            bean = this.setExcePath(bean);

            // 清空当前节点
            bean.setCurrentActivityHandler(null);

            // 处理当前节点事件
            ActionEvent event = new ActionEvent(bean, "sigal");
            this.getEngineEvent().notifyObserver(event);

        } catch (Exception e) {
            throw e;
        }
    }
}
