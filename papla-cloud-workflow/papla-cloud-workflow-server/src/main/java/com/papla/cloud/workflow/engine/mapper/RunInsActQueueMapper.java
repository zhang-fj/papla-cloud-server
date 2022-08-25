package com.papla.cloud.workflow.engine.mapper;

import java.util.Map;
import java.util.List;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.DynaNodeQueueBean;

/**
 * @author linpeng
 * @ClassName: InsActMapper
 * @Description: 实例节点Mapper : Tab - [XIP_WF_INS_ACTS]
 * @date 2015年3月2日 上午11:22:06
 */
public interface RunInsActQueueMapper {

    /**
     * @param params
     * @return 设定文件
     * @Title: getDynaActQueue
     * @Description: 查询流程实例动态节点的审批队列
     */
    public List<DynaNodeQueueBean> getDynaActQueue(Map<String, Object> params);

    /**
     * @param params
     * @return 设定文件
     * @Title: getInsDynaActQueue
     * @Description: 查询流程实例某个动态通知节点的审批队列信息
     */
    public List<DynaNodeQueueBean> getInsDynaActQueue(Map<String, Object> params);

    /**
     * @param instanceId
     * @return 设定文件
     * @Title: getDynaActivities
     * @Description: 查询流程实例下所有的动态通知节点信息
     */
    public List<ActivityBean> getDynaActivities(String instanceId);

    /**
     * @param insCode 设定文件
     * @Title: delDynaActivityQueue
     * @Description: 删除动态通知节点的审批队列信息
     */
    public void delDynaActivityQueue(String insCode);

    /**
     * @param params 设定文件
     * @Title: updDynaActQueueStatus
     * @Description: 修改通知节点动态审批队列状态
     */
    public void updDynaActQueueStatus(Map<String, Object> params);

    /**
     * @param params 设定文件
     * @Title: batchUpDynaActQueueStatus
     * @Description: 批量更新回退路径中动态通知节点的审批队列状态
     */
    public void batchUpDynaActQueueStatus(Map<String, Object> params);

}
