package com.papla.cloud.workflow.engine.run.engine.core.instance.impl;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.instance.InstanceStateHandler;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: InstanceCompleteStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.instance.impl
 * @Description: 完成流程状态变迁处理类
 * @date 2021年8月26日 下午8:36:49
 */
@Service("instanceCompleteStateHandler")
public class InstanceCompleteStateHandler extends InstanceStateHandler {

    public void toStart(ProcessInstanceBean bean) throws Exception {
    }

    public void toRun(ProcessInstanceBean bean) throws Exception {
    }

    public void toTerminate(ProcessInstanceBean bean) throws Exception {
    }

    public void toCancle(ProcessInstanceBean bean) throws Exception {
    }

    public void toComplete(ProcessInstanceBean bean) throws Exception {
    }
}
