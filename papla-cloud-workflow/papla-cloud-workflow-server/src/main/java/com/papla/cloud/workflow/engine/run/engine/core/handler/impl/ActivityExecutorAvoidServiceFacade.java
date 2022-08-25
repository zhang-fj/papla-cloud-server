package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.DeployPropertyBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.handler.ActivityExecutorAvoidService;

/**
 * ClassName:ActivityExecutorAvoidServiceFacade
 * Function: 节点执行人规避服务门面类
 *
 * @author gongbinglai
 * @version Ver 1.0
 * @Date 2014    2014年7月7日		下午2:13:56
 * @since Ver 1.0
 */
@Service("activityExecutorAvoidServiceFacade")
public class ActivityExecutorAvoidServiceFacade {

    @Resource
    private ActivityExecutorAvoidService submitterExecutorAvoidService;
    @Resource
    private ActivityExecutorAvoidService processInternalAvoidService;
    @Resource
    private ActivityExecutorAvoidService adjacentActivityAvoidService;

    /**
     * avoidExecutor:执行人规避处理
     *
     * @param executorList
     * @return
     * @author gongbinglai
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public List<ActivityExecutorBean> avoidExecutor(List<ActivityExecutorBean> executorList, ProcessInstanceBean processInstanceBean) {

        List<DeployPropertyBean> insPropertyList = processInstanceBean.getDeployPropertyList();

        List<ActivityExecutorBean> finalExecutorList = this.copyActivityExecutor(executorList);

        //提交人规避
        String isAvoidSubmitter = PropertyHandlerUtil.findPropertyValue(insPropertyList, WorkFlowConstants.P_AVOID_SUBMITTER);

        if ("Y".equals(isAvoidSubmitter)) {

            finalExecutorList = submitterExecutorAvoidService.avoidExecutor(finalExecutorList, processInstanceBean);

            if (finalExecutorList == null || finalExecutorList.size() == 0) return finalExecutorList;
        }

        //流程内部规避
        String isAvoidProcessInternal = PropertyHandlerUtil.findPropertyValue(insPropertyList, WorkFlowConstants.P_INTERNAL_FLOW);

        if ("Y".equals(isAvoidProcessInternal)) {

            finalExecutorList = processInternalAvoidService.avoidExecutor(finalExecutorList, processInstanceBean);
            if (finalExecutorList == null || finalExecutorList.size() == 0) return finalExecutorList;
        }

        //相邻节点规避
        String isAvoidAdjacentActivity = PropertyHandlerUtil.findPropertyValue(insPropertyList, WorkFlowConstants.P_ADJACENT_NODE);

        if ("Y".equals(isAvoidAdjacentActivity)) {

            finalExecutorList = adjacentActivityAvoidService.avoidExecutor(finalExecutorList, processInstanceBean);
            if (finalExecutorList == null || finalExecutorList.size() == 0) return finalExecutorList;
        }

        return finalExecutorList;

    }

    /**
     * isAvoidActivityBean:是否规避节点
     *
     * @param activityBean
     * @param processInstanceBean
     * @return
     * @author gongbinglai
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public boolean isAvoidActivityBean(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) {

        List<ActivityExecutorBean> finalExecutorList = this.avoidExecutor(activityBean.getActivityConfigExecutorList(), processInstanceBean);

        if (finalExecutorList == null || finalExecutorList.size() != activityBean.getActivityConfigExecutorList().size())
            return true;

        return false;

    }

    /**
     * copyActivityExecutor:复制节点执行人列表
     *
     * @param finalExecutorList
     * @return
     * @author gongbinglai
     * @version Ver 1.0
     * @since Ver 1.0
     */
    private List<ActivityExecutorBean> copyActivityExecutor(List<ActivityExecutorBean> executorList) {

        List<ActivityExecutorBean> finalList = new ArrayList<ActivityExecutorBean>();

        for (ActivityExecutorBean aeb : executorList) {

            ActivityExecutorBean bean = new ActivityExecutorBean();
            bean.setUserId(aeb.getUserId());
            bean.setUserName(aeb.getUserName());
            bean.setEmpId(aeb.getEmpId());
            bean.setEmpName(aeb.getEmpName());

            finalList.add(bean);
        }
        return finalList;

    }

}
