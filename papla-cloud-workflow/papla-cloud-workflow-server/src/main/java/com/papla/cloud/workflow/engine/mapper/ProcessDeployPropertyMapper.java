package com.papla.cloud.workflow.engine.mapper;

import java.util.List;

import com.papla.cloud.workflow.engine.modal.DeployPropertyBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessDeployPropertyMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description:    【流程部署属性】Mapper : Tab - [PAPLA_WF_DEPLOY_ATTRS]
 * @date 2021年8月24日 下午6:09:39
 */
public interface ProcessDeployPropertyMapper {

    /**
     * @param processPropertyList
     * @return void
     * @throws Exception
     * @Title saveProcessProperty
     * @Description TODO 批量保存【流程属性】
     */
    public void saveDeployProperty(List<DeployPropertyBean> processPropertyList) throws Exception;

    /**
     * @param deployId
     * @return List<DeployPropertyBean>
     * @throws Exception
     * @Title getPropertyListByDeployId
     * @Description TODO 根据【流程部署】获取【流程属性】列表
     */
    List<DeployPropertyBean> getPropertyListByDeployId(String deployId) throws Exception;


}
