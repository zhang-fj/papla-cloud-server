package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.common.util.WorkflowServiceUtil;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.handler.ActivityExecutionPathService;


/**
 * ClassName:ProcessInternalAvoidServiceImpl
 * Function: 流程内部规避
 *
 * @author gongbinglai
 * @version Ver 1.0
 * @Date 2014    2014年7月4日		上午10:51:05
 * @since Ver 1.0
 */
@Service("processInternalAvoidService")
public class ProcessInternalAvoidServiceImpl extends AbstractActivityExecutorAvoidServiceImpl {

    @Resource
    private ActivityExecutionPathService activityExecutionPathService;

    @Override
    protected List<ActivityExecutorBean> avoidExecutorForMultiple(List<ActivityExecutorBean> executorList, ProcessInstanceBean processInstanceBean) {

        // 取得实例已执行的路径（包括当前运行中节点及已完成节点）
        List<ActivityBean> activityList = activityExecutionPathService.getExecutePath(processInstanceBean.getInstanceId());

        List<ActivityExecutorBean> finalExecutorList = new ArrayList<ActivityExecutorBean>();

        for (ActivityExecutorBean activityExecutorBean : executorList) {
            //当未在执行路径存在时 添加到节点执行人列表中
            if (!this.isActivityExecutorExecuted(activityExecutorBean, activityList))
                finalExecutorList.add(activityExecutorBean);
        }

        return finalExecutorList;
    }

    /**
     * isActivityExecutorExecuted:判断节点执行人是否已经执行过
     *
     * @param activityExecutorBean
     * @param activityList
     * @return
     * @author gongbinglai
     * @version Ver 1.0
     * @since Ver 1.0
     */
    private boolean isActivityExecutorExecuted(ActivityExecutorBean activityExecutorBean, List<ActivityBean> activityList) {

        boolean isExecuted = false;
        for (ActivityBean ab : activityList) {

            String activityType = ab.getActType();

            // 仅处理通知, 会签, 抄送节点
            if (!(WorkFlowConstants.NOTICE_NODE.equals(activityType)
                    || WorkFlowConstants.COUNTERSIGN_NODE.equals(activityType)
                    || WorkFlowConstants.RESPONSE_NODE.equals(activityType))) {

                continue;
            }

            // 如果未在执行过的节点路径中存在
            if (WorkflowServiceUtil.isExecutorInActivityExecutorList(activityExecutorBean, ab.getActivityExecutorList())) {
                isExecuted = true;
                break;
            }

        }

        //当未在执行路径存在时 添加到节点执行人列表中
        return isExecuted;
    }

}
