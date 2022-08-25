package com.papla.cloud.workflow.engine.mapper;

import com.papla.cloud.workflow.engine.modal.EntityBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EntityMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 业务实体属性Mapper: tab - [PAPLA_WF_ENTITIES]
 * @date 2021年8月24日 下午3:36:36
 */
public interface EntityMapper {

    /**
     * @param instanceId
     * @return EntityBean
     * @throws Exception
     * @Title getEntityByInstanceId
     * @Description TODO 根据【流程实例ID】取得【业务实体信息】
     */
    EntityBean getEntityByInstanceId(String instanceId) throws Exception;

}
