package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.PVEnumBean;


/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessDeployActPVEnumMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 节点属性枚举值Mapper : Tab - [PAPLA_WF_DEPLOY_ACT_PV_ENUM]
 * @date 2021年8月24日 下午5:02:49
 */
public interface ProcessDeployActPVEnumMapper {

    /**
     * @param actPropEnumList
     * @return void
     * @throws Exception
     * @Title saveActivityPvEnum
     * @Description TODO 批量保存【流程部署】流程属性枚举信息
     */
    public void saveActivityPvEnum(List<PVEnumBean> actPropEnumList) throws Exception;

    /**
     * @param param
     * @return List<PVEnumBean>
     * @throws Exception
     * @Title getActivityPVEnumList
     * @Description TODO 删除流程节点【枚举属性】信息
     */
    public void deleteActivityPvEnum(String string) throws Exception;

    /**
     * @param param
     * @return List<PVEnumBean>
     * @throws Exception
     * @Title getActivityPVEnumList
     * @Description TODO 根据【节点ID】和【流程部署ID】获取【枚举属性】信息
     */
    public List<PVEnumBean> getActivityPVEnumList(Map<String, Object> param) throws Exception;

}
