package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import java.util.List;

import com.papla.cloud.workflow.util.PlatformConstants;
import com.papla.cloud.workflow.util.PlatformUtil;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TaskNoticeBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ResponseActivityHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 抄送节点运行方法实现
 * @date 2021年8月29日 下午1:22:38
 */
public class ResponseActivityHandler extends AbstractActivityHandler {

    public void doStart(ProcessInstanceBean bean) throws Exception {
    }

    public void doRun(ProcessInstanceBean bean) throws Exception {
        try {
            // 修改抄送节点状态为完成状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toComplete(activityBean);

            // 取得节点执行人
            ActivityBean actBean = activityHandler.getActivityBean();
            List<ActivityExecutorBean> excutors = actBean.getActivityConfigExecutorList();

            // 发送抄送待办信息
            if (excutors != null && excutors.size() > 0) {
                List<TaskNoticeBean> notices = this.getWorkItemService().createCopyToWorkItem(bean, excutors);

                // 执行提醒处理
                String isNotice = PropertyHandlerUtil.findPropertyValue(actBean.getPropertyMap(), WorkFlowConstants.A_IS_NOTICE); // 是否提醒
                String noticeMode = PlatformUtil.getSystemParamVal(PlatformConstants.NOTICE_MODE);     // 提醒模式

                if ("Y".equals(isNotice) && WorkFlowConstants.NOTICE_MODE_DETAIL.equals(noticeMode)) {
                    handlerTaskNotice(notices);
                }

            }

            // 获取迁出线信息, 对于抄送节点只有一条迁出线
            List<TransitionBean> transList = activityHandler.getActivityBean().getTransitionList();
            if (transList != null && transList.size() > 0) {
                TransitionBean transBean = transList.get(0);
                // 调用函数
                this.processTransition(bean, transBean);
            }

            // 计算执行路径
            bean = this.setExcePath(bean);

            // 设置当前节点
            bean = this.getEngineCommonHandler().setCurrentActivityHandler(bean);

            // 完成节点处理
            ActionEvent event = new ActionEvent(bean, "sigal");
            this.getEngineEvent().notifyObserver(event);

        } catch (Exception e) {
            throw e;
        }
    }

    public void doTerminate(ProcessInstanceBean bean) throws Exception {
    }

    public void doComplete(ProcessInstanceBean bean) throws Exception {
    }
}
