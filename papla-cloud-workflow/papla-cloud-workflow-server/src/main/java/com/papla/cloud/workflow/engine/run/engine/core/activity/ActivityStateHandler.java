package com.papla.cloud.workflow.engine.run.engine.core.activity;

import java.util.Date;

import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.util.AppContext;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;


/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ActivityStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity
 * @Description:
 * @date 2021年8月29日 下午12:15:02
 */
public abstract class ActivityStateHandler {

    /**
     * @return ActivityDAO
     * @throws Exception
     * @Title getActivityDAO
     * @Description TODO    获取容器中对象
     */
    public ActivityDAO getActivityDAO() throws Exception {
        return (ActivityDAO) AppContext.getApplicationContext().getBean("activityDAO");
    }

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toStart
     * @Description TODO    更新节点状态为启动状态
     */
    public abstract void toStart(ActivityBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toRun
     * @Description TODO    更新节点状态为运行状态
     */
    public abstract void toRun(ActivityBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toCancel
     * @Description TODO    更新节点状态为取消状态
     */
    public abstract void toCancel(ActivityBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toTerminate
     * @Description TODO    更新节点状态为终止状态
     */
    public abstract void toTerminate(ActivityBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toComplete
     * @Description TODO    更新节点状态为完成状态
     */
    public abstract void toComplete(ActivityBean bean) throws Exception;

    /**
     * @param bean
     * @param newActState
     * @param beginDate
     * @param endDate
     * @param date
     * @return ActivityStateBean
     * @throws Exception
     * @Title setActivityStateParam
     * @Description TODO    设置节点状态信息
     */
    public ActivityStateBean setActivityStateParam(ActivityBean bean, String newActState, Date beginDate, Date endDate, Date date) throws Exception {

        ActivityStateBean activityStateBean = new ActivityStateBean();
        activityStateBean.setInstanceId(bean.getInstanceId());
        activityStateBean.setActId(bean.getId());
        activityStateBean.setBeginDate(beginDate);
        activityStateBean.setEndDate(endDate);
        activityStateBean.setActState(newActState);
        activityStateBean.setUpdateBy(CurrentUserUtil.getCurrentUserId());
        activityStateBean.setUpdateDt(date);

        return activityStateBean;
    }

    ;
}
