package com.papla.cloud.workflow.engine.run.engine.core.instance.impl;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.instance.InstanceStateHandler;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: InstanceCancelStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.instance.impl
 * @Description: 撤销流程状态变迁处理类
 * @date 2021年8月26日 下午8:36:05
 */
@Service("instanceCancelStateHandler")
public class InstanceCancelStateHandler extends InstanceStateHandler {

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
    }

    public void toCancle(ProcessInstanceBean bean) throws Exception {
    }

    public void toComplete(ProcessInstanceBean bean) throws Exception {
    }
}
