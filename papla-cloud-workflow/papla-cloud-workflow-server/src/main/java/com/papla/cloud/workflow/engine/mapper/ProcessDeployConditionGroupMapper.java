package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ConditionGroupBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessDeployConditionGroupMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 流程或节点属性条件组Mapper : Tab - [PAPLA_WF_DEPLOY_COND_GROUP]
 * @date 2021年8月24日 下午5:41:31
 */
public interface ProcessDeployConditionGroupMapper {

    /**
     * @param conditionGroupList
     * @return void
     * @throws Exception
     * @Title saveConditionGroup
     * @Description TODO 批量保存【流程】或【节点】条件组
     */
    public void saveConditionGroup(List<ConditionGroupBean> conditionGroupList) throws Exception;

    /**
     * @param params
     * @return List<ConditionGroupBean>
     * @throws Exception
     * @Title getConditionGroupList
     * @Description TODO 查询【流程】或【节点】条件组
     */
    public List<ConditionGroupBean> getConditionGroupList(Map<String, Object> params) throws Exception;

}
