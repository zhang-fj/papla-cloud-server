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
 * @ClassName: ActivityCancelStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 节点取消状态处理类
 * @date 2021年8月29日 下午12:46:18
 */
@Service("activityCancelStateHandler")
public class ActivityCancelStateHandler extends ActivityStateHandler {

    @Override
    public void toStart(ActivityBean bean) throws Exception {
        // 设置参数
        Date date = CurrentUserUtil.getCurrentDate();
        ActivityStateBean activityStateBean = this.setActivityStateParam(bean, WorkFlowConstants.NODE_START_STATE, date, null, date);
        // 更新节点状态
        this.getActivityDAO().updateInsActState(activityStateBean);
    }

    @Override
    public void toRun(ActivityBean bean) throws Exception {
    }

    @Override
    public void toCancel(ActivityBean bean) throws Exception {
    }

    @Override
    public void toTerminate(ActivityBean bean) throws Exception {
    }

    @Override
    public void toComplete(ActivityBean bean) throws Exception {
    }
}
