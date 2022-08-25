package com.papla.cloud.workflow.engine.mapper;

import java.util.HashMap;
import java.util.List;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.DynaNodeQueueBean;

/**
 * ClassName:ActivityExecutorMapper
 * Function: 节点执行人mapper
 *
 * @author gongbinglai
 * @version Ver 1.0
 * @Date 2014    2014年7月3日		下午2:32:55
 * @since Ver 1.0
 */
public interface ActivityExecutionPathMapper {

    /**
     * getExecutePath : 取得实例已执行的路径（包括当前运行中节点及已完成节点）
     *
     * @param instanceId
     * @return
     */
    List<ActivityBean> getExecutePath(String instanceId);

    /**
     * getExecutedPath:取得实例已执行的路径
     * @return
     * @author gongbinglai
     * @version Ver 1.0
     * @since Ver 1.0
     */
     List<ActivityBean> getExecutedPath(String instanceId);

    /**
     * getExecutedPath:取得实例已执行的路径
     * @param activityBean
     * @return
     * @author gongbinglai
     * @version Ver 1.0
     * @since Ver 1.0
     */
    List<ActivityExecutorBean> getActRealExecutor(ActivityBean activityBean);

    /**
     * getLastExecutionActivityNode:取得最后一个执行的节点(为当前执行节点,状态为running)
     *
     * @param instanceId
     * @return
     * @author gongbinglai
     * @version Ver 1.0
     * @since Ver 1.0
     */
    ActivityBean getLastExecutionActivityNode(String instanceId);

    /**
     * @param activityBean
     * @return 设定文件
     * @Title: getCommpletedActRealExecutors
     * @Description: 获取完成节点的实际执行人信息
     */
    List<ActivityExecutorBean> getCommpletedActRealExecutors(ActivityBean activityBean);

    /**
     * @param map
     * @return 设定文件
     * @Title: getCurrentNodeQueue
     * @Description: 获取当前节点的动态路径信息
     */
    DynaNodeQueueBean getCurrentNodeQueue(HashMap<String, ?> map);

    /**
     * @param map
     * @return 设定文件
     * @Title: getRunningNodeQueue
     * @Description: 获取当前节点运行中的动态路径
     */
    DynaNodeQueueBean getRunningNodeQueue(HashMap<String, ?> map);

    /**
     * @param map
     * @return 设定文件
     * @Title: getFirstClosedNodeQueue
     * @Description: 获取当前节点第一条已审批的队列信息
     */
    DynaNodeQueueBean getFirstClosedNodeQueue(HashMap<String, ?> map);

    /**
     * @param userId
     * @return 设定文件
     * @Title: getExecutors
     * @Description: 获取节点执行人信息
     */
    List<ActivityExecutorBean> getExecutors(String userId);

    /**
     * @param map
     * @return 设定文件
     * @Title: getDynaNodeExecutor
     * @Description: 获取动态节点队列执行人
     */
    List<ActivityExecutorBean> getDynaNodeExecutor(HashMap<String, ?> map);

}
