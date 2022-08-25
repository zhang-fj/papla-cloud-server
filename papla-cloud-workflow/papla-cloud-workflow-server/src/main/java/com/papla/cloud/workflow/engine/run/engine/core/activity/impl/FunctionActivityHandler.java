package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import java.util.List;

import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: FunctionActivityHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 功能节点运行方法实现
 * @date 2021年8月29日 下午1:29:27
 */
public class FunctionActivityHandler extends AbstractActivityHandler {

    public void doStart(ProcessInstanceBean bean) throws Exception {
    }

    public void doRun(ProcessInstanceBean bean) throws Exception {
        try {
            // 修改开始节点状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toRun(activityBean);

            // 执行功能节点函数事件
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
            // 修改功能节点状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toComplete(activityBean);

            // 获取迁出线信息, 对于功能节点只有一条迁出线
            List<TransitionBean> transList = activityHandler.getActivityBean().getTransitionList();
            if (transList != null && transList.size() > 0) {
                TransitionBean transBean = transList.get(0);
                // 函数类型
                this.processTransition(bean, transBean);
            }
            // 计算执行路径
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
