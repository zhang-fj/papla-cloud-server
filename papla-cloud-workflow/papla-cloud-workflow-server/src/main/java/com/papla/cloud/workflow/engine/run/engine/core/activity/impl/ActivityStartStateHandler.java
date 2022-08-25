package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.run.engine.core.activity.ActivityStateHandler;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ActivityStartStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 节点启动处理类
 * @date 2021年8月29日 下午12:42:36
 */
@Service("activityStartStateHandler")
public class ActivityStartStateHandler extends ActivityStateHandler {

    @Override
    public void toStart(ActivityBean bean) throws Exception {
    }

    public void toRun(ActivityBean bean) throws Exception {
        // 设置参数
        Date date = CurrentUserUtil.getCurrentDate();
        ActivityStateBean activityStateBean = this.setActivityStateParam(bean, WorkFlowConstants.NODE_RUNNING_STATE, null, null, date);
        // 更新节点状态
        this.getActivityDAO().updateInsActState(activityStateBean);
    }

    public void toCancel(ActivityBean bean) throws Exception {
        // 设置参数
        Date date = CurrentUserUtil.getCurrentDate();
        ActivityStateBean activityStateBean = this.setActivityStateParam(bean, WorkFlowConstants.NODE_CANCELED_STATE, null, date, date);
        // 更新节点状态
        this.getActivityDAO().updateInsActState(activityStateBean);
    }

    public void toTerminate(ActivityBean bean) throws Exception {
        // 设置参数
        Date date = CurrentUserUtil.getCurrentDate();
        ActivityStateBean activityStateBean = this.setActivityStateParam(bean, WorkFlowConstants.NODE_TERMINATE_STATE, null, date, date);
        // 更新节点状态
        this.getActivityDAO().updateInsActState(activityStateBean);
    }

    public void toComplete(ActivityBean bean) throws Exception {
        // 设置参数
        Date date = CurrentUserUtil.getCurrentDate();
        ActivityStateBean activityStateBean = this.setActivityStateParam(bean, WorkFlowConstants.NODE_COMPLETED_STATE, null, date, date);
        // 更新节点状态
        this.getActivityDAO().updateInsActState(activityStateBean);
    }
}
