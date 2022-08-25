package com.papla.cloud.workflow.engine.run.engine.core.instance.impl;

import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.instance.ProcessInstanceHandler;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessInstanceHandlerImpl.java
 * @Package com.papla.cloud.wf.run.engine.core.instance.impl
 * @Description: 流程实例抽象实现类
 * @date 2021年8月26日 下午8:33:16
 */
public class ProcessInstanceHandlerImpl extends ProcessInstanceHandler {

    public void toStart(ProcessInstanceBean bean) throws Exception {
        try {
            this.getInstanceStateHandler().toStart(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    public void toRun(ProcessInstanceBean bean) throws Exception {
        try {
            this.getInstanceStateHandler().toRun(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    public void toTerminate(ProcessInstanceBean bean) throws Exception {
        try {
            this.getInstanceStateHandler().toTerminate(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    public void toCancel(ProcessInstanceBean bean) throws Exception {
        try {
            this.getInstanceStateHandler().toCancle(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    public void toComplete(ProcessInstanceBean bean) throws Exception {
        try {
            this.getInstanceStateHandler().toComplete(bean);
        } catch (Exception e) {
            throw e;
        }
    }

}
