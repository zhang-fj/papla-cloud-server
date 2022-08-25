package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.WorkflowServiceUtil;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.handler.ActivityExecutionPathService;


/**
 * @author linpeng
 * @ClassName: AdjacentActivityAvoidServiceImpl
 * @Description: 相邻节点规避
 * @date 2015年3月19日 上午10:52:37
 */
@Service("adjacentActivityAvoidService")
public class AdjacentActivityAvoidServiceImpl extends AbstractActivityExecutorAvoidServiceImpl {

    @Resource
    private ActivityExecutionPathService activityExecutionPathService;

    /*
     * (非 Javadoc)
     * <p>Title: avoidExecutorForMultiple</p>
     * <p>Description: 相邻节点规避处理 </p>
     * @param executorList
     * @param processInstanceBean
     * @return
     * @see com.papla.cloud.wf.run.engine.core.handler.impl.AbstractActivityExecutorAvoidServiceImpl#avoidExecutorForMultiple(java.util.List, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @Override
    protected List<ActivityExecutorBean> avoidExecutorForMultiple(List<ActivityExecutorBean> executorList, ProcessInstanceBean processInstanceBean) {

        List<ActivityExecutorBean> finalExecutorList = new ArrayList<ActivityExecutorBean>();
        //取得最后一个节点的执行人
        ActivityBean activityBean = activityExecutionPathService.getLastExecutionActivityNode(processInstanceBean.getInstanceId());

        if (activityBean != null) {
            for (ActivityExecutorBean aeb : executorList) {
                boolean isExecuted = WorkflowServiceUtil.isExecutorInActivityExecutorList(aeb, activityBean.getActivityExecutorList());
                if (!isExecuted) finalExecutorList.add(aeb);
            }
        } else {
            finalExecutorList.addAll(executorList);
        }

        return finalExecutorList;

    }
}
