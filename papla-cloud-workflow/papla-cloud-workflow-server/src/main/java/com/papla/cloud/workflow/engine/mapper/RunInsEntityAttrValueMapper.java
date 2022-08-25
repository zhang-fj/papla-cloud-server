package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.PubValSetBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: RunInsEntityAttrValueMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 实例实体属性值Mapper : Tab - [PAPLA_WF_RUN_INS_ATTR_VALUE]
 * @date 2021年8月24日 下午7:45:09
 */
public interface RunInsEntityAttrValueMapper {

    /**
     * @param attrList
     * @return void
     * @throws Exception
     * @Title saveInnerEntityAttrValue
     * @Description TODO 批量保存【内置流程实体属性值】
     */
     void saveInnerEntityAttrValue(List<InstanceEntityAttrBean> attrList) throws Exception;

    /**
     * @param params
     * @return List<InstanceEntityAttrBean>
     * @throws Exception
     * @Title getInstanceEntityAttrValueList
     * @Description TODO 根据【流程实例ID】获取【流程实体属性值】列表
     */
     List<InstanceEntityAttrBean> getInstanceEntityAttrValueList(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return List<InstanceEntityAttrBean>
     * @throws Exception
     * @Title getInstanceEntityAttrValueListByInstanceCode
     * @Description TODO 根据【流程实例编码】查找【流程实体属性值】列表
     */
     List<InstanceEntityAttrBean> getInstanceEntityAttrValueListByInstanceCode(Map<String, Object> params) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title updateInsEntityAttrValue
     * @Description TODO 更新【流程实体属性值】
     */
     void updateInsEntityAttrValue(InstanceEntityAttrBean bean) throws Exception;

    /**
     * @param insId
     * @return void
     * @throws Exception
     * @Title clearEntityAttrByInsId
     * @Description TODO 清除【流程实体属性值】
     */
     void clearEntityAttrByInsId(String insId) throws Exception;

    /**
     * @param valCode
     * @return List<PubValSetBean>    返回类型
     * @Title: getInnerEntityAttrs
     * @Description: TODO 获取值集表中的【流程内置属性】
     */
     List<PubValSetBean> getInnerEntityAttrs(String valCode) throws Exception;

    /**
     * @param params
     * @return String    返回类型
     * @Title: getInsSingleEntityAttrValue
     * @Description: TODO 查询单个【流程实体属性值】信息
     */
     String getInsSingleEntityAttrValue(Map<String, String> params) throws Exception;

}
