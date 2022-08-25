package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: RunInsActivityStateMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 实例节点状态Mapper : Tab - [PAPLA_WF_RUN_INS_ACT_STATE]
 * @date 2021年8月24日 下午6:39:20
 */
public interface RunInsActivityStateMapper {

    /**
     * @param processInstanceBean
     * @return void
     * @throws Exception
     * @Title saveActivityState
     * @Description TODO 启动时创建【流程节点状态】信息
     */
    public void saveActivityState(ProcessInstanceBean processInstanceBean) throws Exception;

    /**
     * @param activityStateBean
     * @return void
     * @throws Exception
     * @Title insertActivityState
     * @Description TODO 创建单条【节点状态】信息
     */
    public void insertActivityState(ActivityStateBean activityStateBean) throws Exception;

    /**
     * @param activityStateBean
     * @return void
     * @throws Exception
     * @Title updateActivityState
     * @Description TODO 更新【流程实例】下某个【节点状态】
     */
    public void updateActivityState(ActivityStateBean activityStateBean) throws Exception;

    /**
     * @param params
     * @return void
     * @throws Exception
     * @Title batchUpdateActivityState
     * @Description TODO 【撤销流程】或【重启撤销】流程时, 批量更新【节点状态】信息
     */
    public void batchUpdateActivityState(Map<String, Object> params) throws Exception;

    /**
     * @param insId
     * @return void
     * @throws Exception
     * @Title deleteActivityStateByInstanceId
     * @Description TODO 删除【流程实例】下的【节点状态】信息
     */
    public void deleteActivityStateByInstanceId(String instanceId) throws Exception;

    /**
     * @param params
     * @return List<ActivityStateBean>
     * @throws Exception
     * @Title getActivityStateList
     * @Description TODO 根据【流程实例ID】获取【节点状态】列表
     */
    public List<ActivityStateBean> getActivityStateList(Map<String, Object> params) throws Exception;

    /**
     * @param instanceId
     * @return int
     * @throws Exception
     * @Title getMaxOrderBy
     * @Description TODO 查询最大的顺序号
     */
    public int getMaxOrderBy(String instanceId) throws Exception;

    /**
     * @param params
     * @return List<ActivityStateBean>
     * @throws Exception
     * @Title getBackActivityState
     * @Description TODO 查询环路流程的回退节点轨迹
     */
    public List<ActivityStateBean> getBackActivityState(Map<String, Object> params) throws Exception;

    /**
     * @param map
     * @return void
     * @throws Exception
     * @Title deleteBackActivityState
     * @Description TODO 删除流程回退节点轨迹信息
     */
    public void deleteBackActivityState(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return String
     * @throws Exception
     * @Title getInstanceActivityState
     * @Description TODO 获取【流程实例】节点状态
     */
    public String getInstanceActivityState(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return ActivityStateBean
     * @throws Exception
     * @Title getLastActivityState
     * @Description TODO 获取【获取实例】最新节点状态
     */
    public ActivityStateBean getLastActivityState(Map<String, Object> params) throws Exception;
}
