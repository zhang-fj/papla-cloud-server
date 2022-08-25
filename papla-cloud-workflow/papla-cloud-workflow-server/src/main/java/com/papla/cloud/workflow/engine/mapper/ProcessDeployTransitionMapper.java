package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.TransitionBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessDeployTransitionMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 流程连线处理Mapper: tab - [PAPLA_WF_DEPLOY_ACT_TRANS]
 * @date 2021年8月24日 下午6:10:22
 */
public interface ProcessDeployTransitionMapper {

    /**
     * @param transitionBeanList
     * @return void
     * @throws Exception
     * @Title saveTransitions
     * @Description TODO 批量保存【流程连线】信息
     */
    public void saveTransitions(List<TransitionBean> transitionBeanList) throws Exception;

    /**
     * @param deployId 根据【流程部署ID】获取【流程连线】列表
     * @return List<TransitionBean>
     * @throws Exception
     * @Title getTransitionsByDeployId
     * @Description TODO
     */
    public List<TransitionBean> getTransitionsByDeployId(String deployId) throws Exception;

    /**
     * @param params
     * @return List<TransitionBean>
     * @throws Exception
     * @Title getTransitionList
     * @Description TODO 根据【流程部署ID】和【迁出节点ID】获取【流程连线】列表
     */
    public List<TransitionBean> getTransitionList(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return TransitionBean
     * @throws Exception
     * @Title getRunTransitionList
     * @Description TODO 根据【流程实例ID】和【迁出节点ID】获取【流程连线】信息
     */
    public TransitionBean getTransitionBean(Map<String, Object> params) throws Exception;

}
