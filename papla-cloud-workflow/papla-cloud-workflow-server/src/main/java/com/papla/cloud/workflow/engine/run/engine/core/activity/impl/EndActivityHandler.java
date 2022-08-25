package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EndActivityHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 结束节点运行方法实现
 * @date 2021年8月29日 下午1:30:02
 */
public class EndActivityHandler extends AbstractActivityHandler {

    public void doStart(ProcessInstanceBean bean) throws Exception {
    }

    public void doRun(ProcessInstanceBean bean) throws Exception {
        try {
            // 修改结束节点状态
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
            // 修改结束节点状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toComplete(activityBean);

            // 清空当前节点
            bean.setCurrentActivityHandler(null);

            // 完成实例处理
            ActionEvent actionEvent = new ActionEvent(bean, "doCompleteProcess");
            this.getEngineEvent().notifyObserver(actionEvent);

        } catch (Exception e) {
            throw e;
        }
    }
}
