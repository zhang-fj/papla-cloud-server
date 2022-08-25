package com.papla.cloud.workflow.engine.run.engine.core.handler;

import java.util.List;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

/**
 * @author linpeng
 * @ClassName: ActivityExecutionPathService
 * @Description: 节点执行路径服务类
 * @date 2015年4月15日 上午10:39:53
 */
public interface ActivityExecutionPathService {

    /**
     * @param instanceId
     * @return List<ActivityBean>    返回类型
     * @Title: getExecutePath
     * @Description: 取得流程实例当前运行中节点及已完成节点的执行路径，适用于与规避处理
     */
     List<ActivityBean> getExecutePath(String instanceId);

    /**
     * @param activityBean
     * @param ProcessInstanceBean
     * @return List<ActivityBean>    返回类型
     * @throws Exception
     * @Title: getNextExecutionActivityPath
     * @Description: 取得本节点到下一节点的执行路径
     */
     List<ActivityBean> getNextExecutionActivityPath(ActivityBean activityBean, ProcessInstanceBean insBean) throws Exception;

    /**
     * @param instanceId
     * @return ActivityBean    返回类型
     * @Title: getLastExecutionActivityNode
     * @Description: 取得最后一个执行的节点
     */
     ActivityBean getLastExecutionActivityNode(String instanceId);

}
