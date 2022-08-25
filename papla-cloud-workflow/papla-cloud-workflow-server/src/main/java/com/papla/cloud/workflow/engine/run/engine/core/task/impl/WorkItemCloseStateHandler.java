package com.papla.cloud.workflow.engine.run.engine.core.task.impl;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.engine.core.task.WorkItemStateHandler;


/**
 * @author linpeng
 * @ClassName: WorkItemCloseStateHandler
 * @Description: 关闭状态变迁处理类
 * @date 2015年4月29日 下午12:58:11
 */
@Service("workItemCloseStateHandler")
public class WorkItemCloseStateHandler extends WorkItemStateHandler {

    public void toOpen(WorkItemBean workItemBean) throws Exception {
    }

    public void toClosed(WorkItemBean workItemBean) throws Exception {
    }

    public void toHangUp(WorkItemBean workItemBean) throws Exception {
    }

}
