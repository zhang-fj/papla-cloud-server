package com.papla.cloud.workflow.engine.run.engine.core.instance.impl;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.instance.InstanceStateHandler;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: InstanceStartStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.instance.impl
 * @Description: 启动实例状态变迁处理类 需要实现toRun() , toCancel() 方法
 * @date 2021年8月26日 下午8:39:39
 */
@Service("instanceStartStateHandler")
public class InstanceStartStateHandler extends InstanceStateHandler {

    public void toStart(ProcessInstanceBean bean) throws Exception {
    }

    public void toRun(ProcessInstanceBean bean) throws Exception {
        try {
            // 赋值
            bean = this.setInstanceBean(bean, WorkFlowConstants.INS_RUNNING_STATE);
            // 更改状态
            bean.setEndDate(null);
            this.getProcessInstanceDAO().updateInstanceState(bean);
        } catch (Exception e) {
            throw e;
        }
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
            bean.setEndDate(CurrentUserUtil.getCurrentDate());
            this.getProcessInstanceDAO().updateInstanceState(bean);
        } catch (Exception e) {
            throw e;
        }
    }
}
