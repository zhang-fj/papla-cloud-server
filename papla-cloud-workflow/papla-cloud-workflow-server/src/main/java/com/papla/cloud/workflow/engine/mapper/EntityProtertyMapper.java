package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.EntityAttrBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EntityProtertyMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 业务实体属性Mapper：tab - [papla_wf_entity_attrs]
 * @date 2021年8月24日 下午4:13:04
 */
public interface EntityProtertyMapper {

    /**
     * @param params
     * @return List<EntityAttrBean>
     * @throws Exception
     * @Title getEntityAttrList
     * @Description TODO 根据【实体ID】获取【实体属性】列表
     */
    List<EntityAttrBean> getEntityAttrList(Map<String, Object> params) throws Exception;

}
