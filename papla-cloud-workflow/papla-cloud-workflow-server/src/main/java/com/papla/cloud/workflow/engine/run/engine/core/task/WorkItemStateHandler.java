package com.papla.cloud.workflow.engine.run.engine.core.task;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: WorkItemStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.task
 * @Description: 待办任务状态变迁处理抽象类
 * @date 2021年8月26日 下午8:31:08
 */
public abstract class WorkItemStateHandler {

    /**
     * @param workItemBean
     * @return void    返回类型
     * @throws Exception
     * @Title: toOpen
     * @Description: 打开待办信息
     */
    public abstract void toOpen(WorkItemBean workItemBean) throws Exception;

    /**
     * @param workItemBean
     * @return void    返回类型
     * @throws Exception
     * @Title: toClosed
     * @Description: 关闭待办信息
     */
    public abstract void toClosed(WorkItemBean workItemBean) throws Exception;

    /**
     * @param workItemBean
     * @return void    返回类型
     * @throws Exception
     * @Title: toHangUp
     * @Description: 挂起待办信息
     */
    public abstract void toHangUp(WorkItemBean workItemBean) throws Exception;

    /**
     * @param workItemBean
     * @param newTaskState
     * @return WorkItemBean    返回类型
     * @throws Exception
     * @Title: setWorkItemBean
     * @Description: 设置待办更新参数信息
     */
    public WorkItemBean setWorkItemBean(WorkItemBean workItemBean, String newTaskState) throws Exception {
        WorkItemBean bean = new WorkItemBean();
        BeanUtils.copyProperties(workItemBean, bean);
        // 当前用户
        String lastUpdatedBy = CurrentUserUtil.getCurrentUserId();
        // 当前时间
        Date lastUpdateDate = new Date();
        bean.setTaskState(newTaskState);
        bean.setUpdateDt(lastUpdateDate);
        bean.setUpdateBy(lastUpdatedBy);
        return bean;
    }
}
