package com.papla.cloud.workflow.engine.run.engine.core.instance.impl;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.instance.InstanceStateHandler;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: InstanceRunningStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.instance.impl
 * @Description: 运行实例状态变迁处理类 需要实现  toStart() ,toCancel() , toTerminate() , toComplete()方法
 * @date 2021年8月26日 下午8:38:43
 */
@Service("instanceRunningStateHandler")
public class InstanceRunningStateHandler extends InstanceStateHandler {

    public void toStart(ProcessInstanceBean bean) throws Exception {
        try {
            // 赋值
            bean = this.setInstanceBean(bean, WorkFlowConstants.INS_START_STATE);
            // 更改状态
            bean.setEndDate(null);
            this.getProcessInstanceDAO().updateInstanceState(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    public void toRun(ProcessInstanceBean bean) throws Exception {
    }

    public void toTerminate(ProcessInstanceBean bean) throws Exception {
        try {
            // 赋值
            bean = this.setInstanceBean(bean, WorkFlowConstants.INS_TERMINATE_STATE);
            // 更改状态
            bean.setEndDate(CurrentUserUtil.getCurrentDate());
            this.getProcessInstanceDAO().updateInstanceState(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    public void toCancle(ProcessInstanceBean bean) throws Exception {
        try {
            // 赋值
            bean = this.setInstanceBean(bean, WorkFlowConstants.INS_CANCELED_STATE);
            // 更改状态
            bean.setEndDate(CurrentUserUtil.getCurrentDate());
            this.getProcessInstanceDAO().updateInstanceState(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    public void toComplete(ProcessInstanceBean bean) throws Exception {
        try {
            // 赋值
            bean = this.setInstanceBean(bean, WorkFlowConstants.INS_COMPLETED_STATE);
            // 更改状态
            bean.setEndDate(CurrentUserUtil.getCurrentDate()); // 结束时间
            this.getProcessInstanceDAO().updateInstanceState(bean);
        } catch (Exception e) {
            throw e;
        }
    }
}
