package com.papla.cloud.workflow.engine.common.enums;

import lombok.Getter;

@Getter
public enum ProcessPropertyEnum {

    /**
     * 流程属性-流程名称
     */
    P_NAME("p_name", "", true, true, ""),

    /**
     * 流程属性-流程编码
     */
    P_CODE("p_code", "", true, true, ""),

    /**
     * 流程属性-流程描述
     */
    P_DESC("p_desc", "", true, true, ""),

    /**
     * 流程属性-流程实体
     */
    P_ENTITY_ID("p_entity_id", "", true, true, ""),

    /**
     * 流程属性-流程分类
     */
    P_CATEGORY("p_category", "", true, true, ""),

    /**
     * 流程属性-流程组织
     */
    P_ORG_ID("p_org_id", "", true, true, ""),

    /**
     * 流程属性-流程绑定类型 : S-静态、R-路由、V-变量
     */
    P_FORM_TYPE("p_form_type", "XIP_WF_COMMON_0054", false, true, ""),

    /**
     * 流程属性-静态表单编码
     */
    P_STATIC_FORM("p_static_form", "XIP_WF_COMMON_0059", false, true, ""),

    /**
     * 流程属性-变量表单编码
     */
    P_VAR_FORM("p_var_form", "XIP_WF_COMMON_0060", false, true, ""),

    /**
     * 流程属性-路由表单编码
     */
    P_ROUTE_FORM("p_route_form", "XIP_WF_COMMON_0061", false, true, ""),

    /**
     * 流程属性-相邻节点编码
     */
    P_ADJACENT_NODE("p_adjacent_node", "XIP_WF_COMMON_0062", false, true, "N"),

    /**
     * 流程属性-流程内部编码
     */
    P_INTERNAL_FLOW("p_internal_flow", "XIP_WF_COMMON_0063", false, true, "N"),

    /**
     * 流程属性-提交人编码
     */
    P_AVOID_SUBMITTER("p_avoid_submitter", "XIP_WF_COMMON_0064", false, true, "N"),

    /**
     * 流程属性-提交人变量编码
     */
    P_SUBMITTER_VAR("p_submitter_var", "XIP_WF_COMMON_0065", false, true, ""),

    /**
     * 流程属性-绑定条件编码
     */
    P_BOUND_CONDITION("p_bound_condition", "XIP_WF_COMMON_0066", false, true, ""),

    /**
     * 流程属性-撤回函数类型编码 : PROC-存储过程，WEB-WEB服务
     */
    P_REVOKE_TYPE("p_revoke_type", "XIP_WF_COMMON_0067", false, true, ""),

    /**
     * 流程属性-撤回函数编码
     */
    P_REVOKE_FUNC("p_revoke_func", "XIP_WF_COMMON_0068", false, true, ""),

    /**
     * 流程属性-撤回时业务状态信息
     */
    P_BIZ_STATUS("p_biz_status", "XIP_WF_COMMON_0069", false, true, ""),

    /**
     * 流程属性-平板端表单类型
     */
    P_PAD_FORM_TYPE("p_pad_form_type", "XIP_WF_COMMON_0070", false, true, ""),

    /**
     * 流程属性-平板端表单-静态表单
     */
    P_PAD_STATIC_FORM("p_pad_static_form", "XIP_WF_COMMON_0071", false, true, ""),

    /**
     * 流程属性-平板端表单-变量表单
     */
    P_PAD_VAR_FORM("p_pad_var_form", "XIP_WF_COMMON_0072", false, true, ""),

    /**
     * 流程属性-平板端表单-路由表单
     */
    P_PAD_ROUTE_FORM("p_pad_route_form", "XIP_WF_COMMON_0073", false, true, ""),

    /**
     * 流程属性-手机端表单类型
     */
    P_M_FORM_TYPE("p_m_form_type", "XIP_WF_COMMON_0074", false, true, ""),

    /**
     * 流程属性-手机端表单-静态表单
     */
    P_M_STATIC_FORM("p_m_static_form", "XIP_WF_COMMON_0075", false, true, ""),

    /**
     * 流程属性-手机端表单-变量表单
     */
    P_M_VAR_FORM("p_m_var_form", "XIP_WF_COMMON_0076", false, true, ""),

    /**
     * 流程属性-手机端表单-路由表单
     */
    P_M_ROUTE_FORM("p_m_route_form", "XIP_WF_COMMON_0077", false, true, "");

    public final String code;
    public final String name;
    public final boolean entityProperty;
    public final boolean required;
    public final String defaultValue;

    ProcessPropertyEnum(String code, String name, boolean entityProperty, boolean required, String defaultValue) {
        this.code = code;
        this.name = name;
        this.required = required;
        this.entityProperty = entityProperty;
        this.defaultValue = defaultValue;
    }

    public static void main(String[] args) {
        for (ProcessPropertyEnum processPropertyEnum : ProcessPropertyEnum.values()) {
            System.out.println(processPropertyEnum.getCode());
            System.out.println(processPropertyEnum.getName());
        }
    }
}
