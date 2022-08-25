package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.util.List;

import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.handler.ActivityExecutorAvoidService;

/**
 * @author linpeng
 * @ClassName: AbstractActivityExecutorAvoidServiceImpl
 * @Description: 节点规避处理类
 * @date 2015年3月19日 上午10:52:53
 */
public abstract class AbstractActivityExecutorAvoidServiceImpl implements ActivityExecutorAvoidService {

    /*
     * (非 Javadoc)
     * <p>Title: isAvoidActivityBean</p>
     * <p>Description: 判断节点是否规避节点 </p>
     * @param activityBean
     * @param processInstanceBean
     * @return
     * @see com.papla.cloud.wf.run.engine.core.handler.ActivityExecutorAvoidService#isAvoidActivityBean(com.papla.cloud.wf.modal.ActivityBean, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public boolean isAvoidActivityBean(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) {

        //取得节点配置执行人列表
        List<ActivityExecutorBean> configExecutorList = activityBean.getActivityConfigExecutorList();

        // 规避模式(待扩展)
//		String avoidModel = PlatformUtil.getSystemParamVal(PlatformConstants.WF_AVOID_MODEL) ;
        String avoidModel = WorkFlowConstants.ACTIVITY_AVOID_MULTIPE;

        /**
         * 单人规避：当节点配置执行人为多个时不规避节点; 当配置执行人为1个并且规避后节点执行人为零个时节点规避
         * 多人规避：判断规避后节点执行人列表是否为空，如果为空则规避，否则不规避
         */
        if (WorkFlowConstants.ACTIVITY_AVOID_SINGLE.equals(avoidModel)) {

            if (configExecutorList.size() > 1) {
                return false;
            } else if (configExecutorList.size() == 1) {

                List<ActivityExecutorBean> afterAvoidExecutorList = this.avoidExecutor(configExecutorList, processInstanceBean);
                if (afterAvoidExecutorList == null || afterAvoidExecutorList.size() == 0) return true;
            }

        } else if (WorkFlowConstants.ACTIVITY_AVOID_MULTIPE.equals(avoidModel)) {

            List<ActivityExecutorBean> afterAvoidExecutorList = this.avoidExecutor(configExecutorList, processInstanceBean);
            if (afterAvoidExecutorList == null || afterAvoidExecutorList.size() == 0) return true;

        }
        return false;
    }

    /*
     * (非 Javadoc)
     * <p>Title: avoidExecutor</p>
     * <p>Description: 执行人规避处理 </p>
     * @param executorList
     * @param processInstanceBean
     * @return
     * @see com.papla.cloud.wf.run.engine.core.handler.ActivityExecutorAvoidService#avoidExecutor(java.util.List, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public List<ActivityExecutorBean> avoidExecutor(List<ActivityExecutorBean> executorList, ProcessInstanceBean processInstanceBean) {

        // 规避模式(待扩展)
//		String avoidModel = PlatformUtil.getSystemParamVal(PlatformConstants.WF_AVOID_MODEL) ;
//		avoidModel = avoidModel==null ? WorkFlowConstants.ACTIVITY_AVOID_MULTIPE : avoidModel ;
        String avoidModel = WorkFlowConstants.ACTIVITY_AVOID_MULTIPE;

        if (WorkFlowConstants.ACTIVITY_AVOID_SINGLE.equals(avoidModel)) { // 单人规避处理
            return executorList.size() == 1 ? this.avoidExecutorForSingle(executorList, processInstanceBean) : executorList;

        } else if (WorkFlowConstants.ACTIVITY_AVOID_MULTIPE.equals(avoidModel)) { // 多人规避处理
            return this.avoidExecutorForMultiple(executorList, processInstanceBean);
        }

        return executorList;
    }

    /**
     * @param executorList
     * @param processInstanceBean
     * @return List<ActivityExecutorBean>    返回类型
     * @Title: avoidExecutorForSingle
     * @Description: 单人规避
     */
    protected List<ActivityExecutorBean> avoidExecutorForSingle(List<ActivityExecutorBean> executorList, ProcessInstanceBean processInstanceBean) {

        return this.avoidExecutorForMultiple(executorList, processInstanceBean);
    }

    /**
     * @param executorList
     * @param processInstanceBean
     * @return List<ActivityExecutorBean>    返回类型
     * @Title: avoidExecutorForMultiple
     * @Description: 多人规避
     */
    protected abstract List<ActivityExecutorBean> avoidExecutorForMultiple(List<ActivityExecutorBean> executorList, ProcessInstanceBean processInstanceBean);


}
