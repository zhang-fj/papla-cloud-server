package com.papla.cloud.workflow.engine.run.dao;

import java.util.List;

import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.StartActivityHandler;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.modal.DynaNodeQueueBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

/**
 * @author linpeng
 * @ClassName: ActivityDAO
 * @Description: 节点处理DAO接口
 * @date 2015年4月20日 下午2:46:01
 */
public interface ActivityDAO {

    /**
     * @param instanceId
     * @return List<ActivityBean>    返回类型
     * @throws Exception
     * @Title: getInsActivityList
     * @Description: 根据实例ID , 获取流程实例下节点信息
     */
    List<ActivityBean> getInsActivityList(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @return StartActivityHandler    返回类型
     * @throws Exception
     * @Title: getStartActivityHandler
     * @Description: 获取实例的开始处理节点
     */
    StartActivityHandler getStartActivityHandler(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @return ActivityBean    返回类型
     * @throws Exception
     * @Title: getStartActivityBean
     * @Description: 获取流程实例开始节点信息
     */
    ActivityBean getStartActivityBean(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @param actId
     * @return
     * @throws Exception 设定文件
     * @Title: getLastActivityState
     * @Description: 查询流程实例中某个节点的最新状态
     */
    ActivityStateBean getLastActivityState(String instanceId, String actId) throws Exception;

    /**
     * @param instanceId
     * @param activityBean
     * @return ActivityBean    返回类型
     * @throws Exception
     * @Title: getActivityBean
     * @Description: 获取节点配置信息
     */
    ActivityBean getActivityBean(String instanceId, ActivityBean activityBean, boolean isArched) throws Exception;

    /**
     * @param instanceId
     * @param actId
     * @return ActivityBean    返回类型
     * @throws Exception
     * @Title: getActivityBean
     * @Description: 取得实例下节点基本信息
     */
    ActivityBean getActivityBean(String instanceId, String actId) throws Exception;

    /**
     * @param instanceId
     * @param actId
     * @return String    返回类型
     * @throws Exception
     * @Title: getActivityCategory
     * @Description: 查询节点类型
     */
    String getActivityCategory(String instanceId, String actId) throws Exception;

    /**
     * @param activityStateBean
     * @throws Exception 设定文件
     * @Title: updateInsActState
     * @Description: 更新单个节点状态
     */
    void updateInsActState(ActivityStateBean activityStateBean) throws Exception;

    /**
     * @param bean
     * @return void    返回类型
     * @throws Exception
     * @Title: cancelInsActState
     * @Description: 将实例节点更新为启动状态
     */
    void cancelInsActState(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void    返回类型
     * @throws Exception
     * @Title: deleteInsActState
     * @Description: 删除实例节点状态信息
     */
    void deleteInsActState(ProcessInstanceBean bean) throws Exception;

    /**
     * @param activityStateBean
     * @throws Exception 设定文件
     * @Title: insertInsActState
     * @Description: 创建节点状态信息
     */
    void insertInsActState(ActivityStateBean activityStateBean) throws Exception;

    /**
     * @param instanceId
     * @return
     * @throws Exception 设定文件
     * @Title: getNextActivityOrder
     * @Description: 获取下一个节点序号
     */
    int getNextActivityOrder(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @return
     * @throws Exception 设定文件
     * @Title: getDynaActivities
     * @Description: 获取流程实例下的动态节点信息
     */
    List<ActivityBean> getDynaActivities(String instanceId) throws Exception;

    /**
     * @param insCode
     * @throws Exception 设定文件
     * @Title: delDynaActivityQueue
     * @Description: 删除动态通知节点的审批队列信息
     */
    void delDynaActivityQueue(String insCode) throws Exception;

    /**
     * @param queueBean
     * @throws Exception 设定文件
     * @Title: updDynaActQueueStatus
     * @Description: 修改通知节点的动态审批队列状态
     */
    void updDynaActQueueStatus(DynaNodeQueueBean queueBean, String newStatus) throws Exception;

    /**
     * @param insCode
     * @param actCodeList
     * @throws Exception 设定文件
     * @Title: batchUpdateActQueueStatus
     * @Description: 批量更新回退路径中动态通知节点的审批队列状态
     */
    void batchUpDynaActQueueStatus(String insCode, List<String> actCodeList) throws Exception;

}
