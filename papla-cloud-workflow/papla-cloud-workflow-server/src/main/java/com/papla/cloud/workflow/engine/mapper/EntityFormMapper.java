package com.papla.cloud.workflow.engine.mapper;

import com.papla.cloud.workflow.engine.modal.EntityFormBean;

import java.util.List;
import java.util.Map;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EntityFormMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 流程实体业务表单Mapper： tab - [PAPLA_WF_ENTITIES]
 * @date 2021年8月24日 下午3:38:25
 */
public interface EntityFormMapper {

    /**
     * @param params
     * @return List<Map < String, String>>
     * @throws Exception
     * @Title getEntityFormList
     * @Description TODO 获取【流程关联表单】信息
     */
    List<EntityFormBean> getEntityFormList(Map<String, Object> params) throws Exception;

}
