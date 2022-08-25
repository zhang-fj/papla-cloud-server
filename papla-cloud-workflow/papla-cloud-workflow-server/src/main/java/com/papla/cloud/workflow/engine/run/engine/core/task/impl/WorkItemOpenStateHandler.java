package com.papla.cloud.workflow.engine.run.engine.core.task.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.dao.WorkItemDAO;
import com.papla.cloud.workflow.engine.run.engine.core.task.WorkItemStateHandler;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: WorkItemOpenStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.task.impl
 * @Description: 打开状态变迁处理类
 * @date 2021年8月26日 下午8:30:00
 */
@Service("workItemOpenStateHandler")
public class WorkItemOpenStateHandler extends WorkItemStateHandler {

    @Resource
    private WorkItemDAO workItemDAO;

    public void toOpen(WorkItemBean workItemBean) throws Exception {
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void toClosed(WorkItemBean workItemBean) throws Exception {
        // 将待办状态修改为 closed
        WorkItemBean bean = this.setWorkItemBean(workItemBean, WorkFlowConstants.TASK_STATE_CLOSED);
        // 执行更新处理
        workItemDAO.modifyWorkItem(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void toHangUp(WorkItemBean workItemBean) throws Exception {
        // 将待办状态修改为 hang-up
        WorkItemBean bean = this.setWorkItemBean(workItemBean, WorkFlowConstants.TASK_STATE_HUNGUP);
        // 执行更新处理
        workItemDAO.modifyWorkItem(bean);
    }
}
