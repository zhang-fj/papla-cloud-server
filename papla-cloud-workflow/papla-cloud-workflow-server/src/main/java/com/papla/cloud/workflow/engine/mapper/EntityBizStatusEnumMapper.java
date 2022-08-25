package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.BizStatusEnumBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EntityBizStatusEnumMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 业务状态信息Mapper: tab - [PAPLA_WF_ENTITY_STATUS_ENUM]
 * @date 2021年8月24日 下午3:40:43
 */
public interface EntityBizStatusEnumMapper {

    /**
     * @param params
     * @return List<BizStatusEnumBean>    返回类型
     * @Title: getEntityBizStatusList
     * @Description: 根据参数查询【业务状态】列表
     */
    List<BizStatusEnumBean> getEntityBizStatusList(Map<String, Object> params) throws Exception;

}
