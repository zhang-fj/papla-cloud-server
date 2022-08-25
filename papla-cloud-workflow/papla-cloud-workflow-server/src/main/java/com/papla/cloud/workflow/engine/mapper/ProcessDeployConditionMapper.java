package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ConditionBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessDeployConditionMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 【流程】或【节点】属性条件单元Mapper : Tab - [PAPLA_WF_DEPLOY_COND]
 * @date 2021年8月24日 下午5:50:22
 */
public interface ProcessDeployConditionMapper {

    /**
     * @param conditionList
     * @return void
     * @throws Exception
     * @Title saveCondtionBean
     * @Description TODO 批量保存【流程】或【节点】属性条件单元
     */
    public void saveCondtionBean(List<ConditionBean> conditionList) throws Exception;

    /**
     * @param params
     * @return List<ConditionBean>
     * @throws Exception
     * @Title getConditonBeanList
     * @Description TODO 获取【流程】或【节点】属性条件单元
     */
    public List<ConditionBean> getConditonBeanList(Map<String, Object> params) throws Exception;

}
