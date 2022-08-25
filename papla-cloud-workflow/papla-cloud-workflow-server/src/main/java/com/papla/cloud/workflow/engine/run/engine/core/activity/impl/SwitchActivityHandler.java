package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;

/**
 * @author linpeng
 * @ClassName: SwitchActivityHandler
 * @Description: 判断节点运行方法实现
 * @date 2015年3月17日 上午10:51:40
 */
public class SwitchActivityHandler extends AbstractActivityHandler {

    public void doStart(ProcessInstanceBean bean) throws Exception {
    }

    public void doRun(ProcessInstanceBean bean) throws Exception {
        try {
            // 修改判断节点状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toRun(activityBean);

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
            // 修改判断节点状态
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();
            activityHandler.getActivityStateHandler().toComplete(activityBean);

            // 获取迁出线信息, 对于开始节点只有一条迁出线
            TransitionBean transBean = activityHandler.getActivityBean().getMoveInTransition();
            // 调用函数
            this.processTransition(bean, transBean);

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
