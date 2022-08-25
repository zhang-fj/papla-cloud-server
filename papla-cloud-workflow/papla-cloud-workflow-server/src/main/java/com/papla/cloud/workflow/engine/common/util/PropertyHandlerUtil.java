package com.papla.cloud.workflow.engine.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityPropertyBean;
import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.PVEnumBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.PropertyBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: PropertyHandlerUtil.java
 * @Package com.papla.cloud.wf.common.util
 * @Description: 流程属性处理工具类
 * @date 2021年8月26日 上午11:51:54
 */
public class PropertyHandlerUtil {

    /**
     * @param instanceEntityAttrList
     * @param code
     * @return String
     * @Title findEntityPropertyValue
     * @Description TODO    获取【业务实体】属性值
     */
    public static String findEntityPropertyValue(List<InstanceEntityAttrBean> instanceEntityAttrList, String code) {
        String value = "";
        for (InstanceEntityAttrBean ea : instanceEntityAttrList) {
            if (code.equals(ea.getAttrCode())) {
                value = ea.getAttrResult();
                break;
            }
        }
        return value;
    }

    /**
     * @param activityBean
     * @param prorpertyCode
     * @return String
     * @Title findActivityPropertyValue
     * @Description TODO 获取【节点】属性值
     */
    public static String findActivityPropertyValue(ActivityBean activityBean, String prorpertyCode) {
        if (activityBean == null || prorpertyCode == null) return null;
        List<ActivityPropertyBean> properytBeanList = activityBean.getActPropertyBeanList();
        return findPropertyValue(properytBeanList, prorpertyCode);
    }

    /**
     * @param properytBeanList
     * @param prorpertyCode
     * @return String
     * @Title findPropertyValue
     * @Description TODO    获取【流程】或【节点】属性值
     */
    public static String findPropertyValue(List<? extends PropertyBean> properytBeanList, String prorpertyCode) {
        if (properytBeanList != null && properytBeanList.size() > 0) {
            for (PropertyBean bean : properytBeanList) {
                if (bean.getPropertyCode().equals(prorpertyCode)) {
                    return bean.getPropertyValue();
                }
            }
        }
        return null;
    }

    /**
     * @param propertyMap
     * @param prorpertyCode
     * @return String
     * @Title findPropertyValue
     * @Description TODO    获取【流程】或【节点】属性值
     */
    public static String findPropertyValue(Map<String, String> propertyMap, String prorpertyCode) {
        return propertyMap.get(prorpertyCode);
    }

    /**
     * @param actPropEnumList
     * @param prorpertyCode
     * @return List<String>
     * @Title findInsActPVEnumValue
     * @Description TODO    查询【枚举值】信息
     */
    public static List<String> findActivityPVEnumValue(List<PVEnumBean> actPropEnumList, String prorpertyCode) {
        List<String> userOrPositionIdList = new ArrayList<String>();

        if (actPropEnumList != null && actPropEnumList.size() > 0) {
            for (PVEnumBean bean : actPropEnumList) {
                if (bean.getPropertyCode().equals(prorpertyCode)) {
                    userOrPositionIdList.add(bean.getExecutorValue());
                }
            }
        }
        return userOrPositionIdList;
    }

    /**
     * @param propertyList
     * @return Map<String, String>
     * @Title propertyListToMap
     * @Description TODO    将【流程】或【节点】属性列表转换为Map
     */
    public static Map<String, String> propertyListToMap(List<? extends PropertyBean> propertyList) {
        Map<String, String> propertyMap = new HashMap<String, String>();
        // 执行转换
        if (propertyList != null && propertyList.size() > 0) {
            for (PropertyBean bean : propertyList) {
                String key = bean.getPropertyCode();
                String value = bean.getPropertyValue();
                propertyMap.put(key, value);
            }
        }
        return propertyMap;
    }


    /**
     * @param bean
     * @param attrMap
     * @return ProcessInstanceBean
     * @throws Exception
     * @Title setInstanceEntityAttrValue
     * @Description TODO    复制流程实例实体属性值
     */
    public static ProcessInstanceBean setInstanceEntityAttrValue(ProcessInstanceBean bean, Map<String, String> attrMap) throws Exception {
        // 取得流程实例实体属性
        List<InstanceEntityAttrBean> attrList = bean.getAttrList();
        if (attrList == null) return bean;

        if (attrMap == null) attrMap = new HashMap<String, String>();

        // HashMap键集合
        Set<String> keySet = attrMap.keySet();

        try {
            // 执行赋值处理
            if (attrList != null && attrList.size() > 0) {
                for (InstanceEntityAttrBean attrValueBean : attrList) {
                    if ("static".equals(attrValueBean.getAttrCategory())) {

                        // 流程实例实体属性编码
                        String attrCode = attrValueBean.getAttrCode();
                        if (attrCode == null) continue;


                        /*==========================================
                         * 1.流程引擎对内置属性的赋值处理（参数值由引擎自动生成）
                         *=========================================*/
                        if (attrCode.equals(WorkFlowConstants.E_SUBMITTER)) {    // 设置提交人
                            String rst = attrMap.get(attrCode);
                            attrValueBean.setAttrResult(rst == null ? CurrentUserUtil.getCurrentUserId() : rst);
                        }
                        if (attrCode.equals(WorkFlowConstants.E_SUBMITTER_NAME)) { //  设置提交人姓名
                            String rst = attrMap.get(attrCode);
                            attrValueBean.setAttrResult(rst == null ? CurrentUserUtil.getEmpName() : rst);
                        }
                        if (attrCode.equals(WorkFlowConstants.E_SUBMIT_DATE)) {    // 设置提交时间
                            attrValueBean.setAttrResult(CurrentUserUtil.getCurrentDateStr());
                        }
                        if (attrCode.equals(WorkFlowConstants.E_INSTANCE_CODE)) {    // 设置流程实例编码
                            attrValueBean.setAttrResult(bean.getInstanceCode());
                        }
                        if (attrCode.equals(WorkFlowConstants.E_PROCESS_NAME)) {    // 设置流程名称
                            attrValueBean.setAttrResult(bean.getProcessName());
                        }


                        /*==========================================
                         * 2.设置流程实例属性赋值处理（参数值由业务系统传入）
                         *=========================================*/
                        if (!keySet.contains(attrCode)) {    // 判断keySet中是否存在当前key
                            continue;
                        }
                        String result = String.valueOf(attrMap.get(attrCode));
                        attrValueBean.setAttrResult(result);


                        /*==========================================
                         * 3.设置流程流程实例Bean中的业务数据信息
                         *=========================================*/
                        if (attrCode.equals(WorkFlowConstants.E_BUSINESS_ID)) {    // 设置业务ID
                            bean.setBusinessId(result);
                        }
                        if (attrCode.equals(WorkFlowConstants.E_BUSINESS_CODE)) {    // 设置业务编码
                            bean.setBusinessCode(result);
                        }
                        if (attrCode.equals(WorkFlowConstants.E_BUSINESS_NAME)) {    // 设置业务名称
                            bean.setBusinessName(result);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }

        // 返回值
        bean.setAttrList(attrList);
        return bean;
    }

    /**
     * @param oldPropertyValue
     * @return int
     * @Title transPropertyValue
     * @Description TODO 属性值转换处理
     */
    public static int transPropertyValue(String oldPropertyValue) {
        int i = -1;
        if (WorkFlowConstants.JOIN_FIRST_PASS.equals(oldPropertyValue)) {    // 抢单
            i = 0;
        } else if (WorkFlowConstants.JOIN_ONE_PASS.equals(oldPropertyValue)) {    // 一票通过
            i = 1;
        } else if (WorkFlowConstants.JOIN_ONE_REFUSE.equals(oldPropertyValue)) { // 一票否决
            i = 2;
        } else if (WorkFlowConstants.JOIN_PERCENT_PASS.equals(oldPropertyValue)) { // 比例通过
            i = 3;
        } else if (WorkFlowConstants.JOIN_PERCENT_REFUSE.equals(oldPropertyValue)) { // 比例驳回
            i = 4;
        } else {
            i = -1;
        }
        return i;
    }
}
