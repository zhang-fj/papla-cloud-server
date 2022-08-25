package com.papla.cloud.workflow.engine.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.TransStateBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: RunInsTransitionStateMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 实例连线状态Mapper : Tab - [PAPLA_WF_RUN_INS_TRANS_STATE]
 * @date 2021年8月24日 下午8:14:09
 */
public interface RunInsTransitionStateMapper {

    /**
     * @param instanceId
     * @return List<TransStateBean>    返回类型
     * @Title: getInsTransStateList
     * @Description: 查询实例下连线状态信息
     */
    public List<TransStateBean> getTransStateList(Map<String, Object> params);

    /**
     * 记录流程流经的连线
     *
     * @param transBean
     */
    public void saveTransitionState(TransitionBean transBean);

    /**
     * 流程回退时, 批量清除连线状态信息
     *
     * @param hashmap
     */
    public void batDelTransState(HashMap<String, Object> hashmap);

    /**
     * 清除流程实例连线状态信息
     *
     * @param insId
     */
    public void clearInsTransStateByInsId(String insId);

    /**
     * @param instanceId
     * @return 设定文件
     * @Title: getMaxInsTransOrder
     * @Description: 获取流程实例的最大连线序号
     */
    public int getMaxInsTransOrder(String instanceId);

    /**
     * @param instanceId
     * @return 设定文件
     * @Title: getInsTransStateList
     * @Description: 查询环路流程的回退连线轨迹
     */
    public List<TransStateBean> getBackTransStateList(HashMap<String, ?> map);

    /**
     * @param map 设定文件
     * @Title: deleteBackTransState
     * @Description: 删除环路流程的回退连线轨迹
     */
    public void deleteBackTransState(HashMap<String, ?> map);
}
