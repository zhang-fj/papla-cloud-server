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
 * @ClassName: ActivityNotCreatedStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 节点状态未创建
 * @date 2021年8月29日 下午12:45:04
 */
@Service("activityNotCreatedStateHandler")
public class ActivityNotCreatedStateHandler extends ActivityStateHandler {

    @Override
    public void toStart(ActivityBean bean) throws Exception {
        Date date = CurrentUserUtil.getCurrentDate();
        // 创建节点执行状态
        ActivityStateBean activityStateBean = setActivityStateParam(bean, WorkFlowConstants.NODE_START_STATE, date, null, date);
        // 执行新增操作
        this.getActivityDAO().insertInsActState(activityStateBean);
    }

    @Override
    public void toRun(ActivityBean bean) throws Exception {
        Date date = CurrentUserUtil.getCurrentDate();
        // 创建节点执行状态
        ActivityStateBean activityStateBean = setActivityStateParam(bean, WorkFlowConstants.NODE_RUNNING_STATE, date, null, date);
        // 执行新增操作
        this.getActivityDAO().insertInsActState(activityStateBean);
    }

    @Override
    public void toCancel(ActivityBean bean) throws Exception {
    }

    @Override
    public void toTerminate(ActivityBean bean) throws Exception {
    }

    @Override
    public void toComplete(ActivityBean bean) throws Exception {
        Date date = CurrentUserUtil.getCurrentDate();
        // 创建节点执行状态
        ActivityStateBean activityStateBean = setActivityStateParam(bean, WorkFlowConstants.NODE_CANCELED_STATE, date, null, date);
        // 执行新增操作
        this.getActivityDAO().insertInsActState(activityStateBean);
    }

}
