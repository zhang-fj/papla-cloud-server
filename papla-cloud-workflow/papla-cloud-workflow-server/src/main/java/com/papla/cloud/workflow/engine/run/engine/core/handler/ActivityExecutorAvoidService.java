package com.papla.cloud.workflow.engine.run.engine.core.handler;

import java.util.List;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

/**
 * @author linpeng
 * @ClassName: ActivityExecutorAvoidService
 * @Description: 节点规避处理
 * @date 2015年3月19日 上午10:47:13
 */
public interface ActivityExecutorAvoidService {


    /**
     * @param activityBean
     * @param processInstanceBean
     * @return boolean    返回类型
     * @Title: isAvoidActivityBean
     * @Description: 判断节点是否规避节点
     */
    public boolean isAvoidActivityBean(ActivityBean activityBean, ProcessInstanceBean processInstanceBean);

    /**
     * @param executorList
     * @param processInstanceBean
     * @return List<ActivityExecutorBean>    返回类型
     * @Title: avoidExecutor
     * @Description: 执行人规避处理
     */
    public List<ActivityExecutorBean> avoidExecutor(List<ActivityExecutorBean> executorList, ProcessInstanceBean processInstanceBean);

}
