package com.papla.cloud.workflow.engine.run.dao;

import java.util.List;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.DynaNodeQueueBean;


/**
 * @author linpeng
 * @ClassName: ActivityExecutionPathDao
 * @Description: 节点执行路径dao
 * @date 2015年4月16日 上午9:41:41
 */
public interface ActivityExecutionPathDao {

    /**
     * @param instanceId
     * @return List<ActivityBean>    返回类型
     * @Title: getExecutePath
     * @Description: 取得流程实例已执行的路径（包括当前运行中通知/会签节点和已完成通知/会签节点）
     */
    List<ActivityBean> getExecutePath(String instanceId);

    /**
     * @param instanceId 流程实例ID
     * @return 设定文件
     * @Title: getExecutedPath
     * @Description: 取得流程实例已执行完成的通知和会签节点信息
     */
    List<ActivityBean> getExecutedPath(String instanceId);

    /**
     * @param activityBean
     * @return List<ActivityExecutorBean>    返回类型
     * @Title: getActRealExecutor
     * @Description: 循环取得路径中每个节点的实际执行人信息
     */
    List<ActivityExecutorBean> getActRealExecutor(ActivityBean activityBean);

    /**
     * @param instanceId
     * @return ActivityBean    返回类型
     * @Title: getLastExecutionActivityNode
     * @Description: 取得最后一个执行的节点(为当前执行节点, 状态为running)
     */
    ActivityBean getLastExecutionActivityNode(String instanceId);

    /**
     * @param activityBean
     * @return 设定文件
     * @Title: getCurrentNodeQueue
     * @Description: 获取当前节点的队列信息
     */
    DynaNodeQueueBean getCurrentNodeQueue(ActivityBean activityBean);

    /**
     * @param activityBean
     * @return 设定文件
     * @Title: getRunningNodeQueue
     * @Description: 获取当前节点运行中的队列信息
     */
    DynaNodeQueueBean getRunningNodeQueue(ActivityBean activityBean);

    /**
     * @param activityBean
     * @return 设定文件
     * @Title: getFirstClosedNodeQueue
     * @Description: 获取当前节点第一条已审批的队列信息
     */
    DynaNodeQueueBean getFirstClosedNodeQueue(ActivityBean activityBean);

    /**
     * @param approverId
     * @return 设定文件
     * @Title: getConfigExecutorList
     * @Description: 获取动态审批的队列的执行人信息
     */
    List<ActivityExecutorBean> getConfigExecutorList(String approverId);

}
