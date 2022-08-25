package com.papla.cloud.workflow.engine.common.enums;

import lombok.Getter;

@Getter
public enum ActivityPropertyEnum {

    /**
     * 节点属性 - 编码
     */
    A_CODE("a_code", "", true, false, ""),

    /**
     * 节点属性 - 描述
     */
    A_DESC("a_desc", "", true, false, ""),

    /**
     * 节点属性-程序类型：PROC-存储过程，WEB-WEB服务
     */
    A_FUNC_TYPE("a_func_type", "XIP_WF_COMMON_0079", false, false, ""),

    /**
     * 节点属性-函数值
     */
    A_FUNC_NAME("a_func_name", "XIP_WF_COMMON_0068", false, false, ""),

    /**
     * 节点属性-开始节点关联表单
     */
    A_FORM("a_form", "XIP_WF_COMMON_0078", false, false, ""),

    /**
     * 节点属性-关联表单类型
     */
    A_FORM_TYPE("a_form_type", "XIP_WF_COMMON_0054", false, true, ""),

    /**
     * 节点属性-静态表单
     */
    A_STATIC_FORM("a_static_form", "XIP_WF_COMMON_0059", false, true, ""),

    /**
     * 节点属性-变量表单
     */
    A_VAR_FORM("a_var_form", "XIP_WF_COMMON_0060", false, true, ""),

    /**
     * 节点属性 - 路由表单
     */
    A_ROUTE_FORM("a_route_form", "XIP_WF_COMMON_0061", false, true, ""),

    /**
     * 节点属性 - 绑定条件
     */
    A_BOUND_CONDITON("a_bound_condition", "XIP_WF_COMMON_0066", false, false, ""),

    /**
     * 节点属性 - 执行人类型 : SU-静态用户、SP-静态岗位、RU-路由用户、RP-路由岗位、GRANT-授权类型、VAR-变量类型、PROC-过程类型、SQL-SQL类型、WEB-WEB服务、JAVA方法
     */
    A_EXECUTOR_TYPE("a_executor_type", "XIP_WF_COMMON_0056", false, true, "US"),

    /**
     * 节点属性 - 静态用户
     */
    A_STATIC_USER("a_static_user", "", false, true, ""),

    /**
     * 节点属性 - 静态岗位
     */
    A_STATIC_POSTION("a_static_postion", "", false, true, ""),

    /**
     * 节点属性 - 路由用户
     */
    A_ROUTE_USER("a_route_user", "XIP_WF_COMMON_0083", false, true, ""),

    /**
     * 节点属性 - 路由岗位
     */
    A_ROUTE_POSTION("a_route_postion", "XIP_WF_COMMON_0084", false, true, ""),

    /**
     * 节点规避类型  single-单人规避,"",true,false,""),  multiple-多人规避
     */
    ACTIVITY_AVOID_SINGLE("single", "", false, false, ""),
    ACTIVITY_AVOID_MULTIPE("multiple", "", false, false, ""),

    /**
     * 节点属性 - 是否按部门隔离
     */
    A_DEPT_FILTER("a_dept_filter", "XIP_WF_COMMON_0085", false, true, "N"),

    /**
     * 节点属性 - 执行人变量
     */
    A_EXEC_VAR("a_exec_var", "XIP_WF_COMMON_0086", false, true, ""),

    /**
     * 节点属性 - 节点属性 - 存过过程
     */
    A_EXEC_PROC("a_exec_proc", "XIP_WF_COMMON_0087", false, true, ""),

    /**
     * WEB服务
     */
    A_EXEC_WEB("a_exec_web", "XIP_WF_COMMON_0088", false, true, ""),

    /**
     * 节点属性 - SQL类型-SQL语句
     */
    A_CONFIG_SQL("a_config_sql", "XIP_WF_COMMON_0089", false, true, ""),

    /**
     * 节点属性 - SQL类型-更多SQL
     */
    A_MORE_SQL("a_more_sql", "XIP_WF_COMMON_0090", false, true, ""),

    /**
     * 节点属性 - JAVA方法-方法名
     */
    A_MORE_JAVA("a_more_java", "XIP_WF_COMMON_0056_0001", false, true, ""),

    /**
     * 节点属性 - 执行前事件
     */
    A_BEFORE_EVENT("a_before_event", "XIP_WF_COMMON_0091", false, false, ""),

    /**
     * 节点属性 - 执行前函数名
     */
    A_BEFORE_FUNC("a_before_func", "XIP_WF_COMMON_0068", false, false, ""),

    /**
     * 节点属性 - 执行后事件
     */
    A_AFTER_EVENT("a_after_event", "XIP_WF_COMMON_0092", false, false, ""),

    /**
     * 节点属性 - 执行后函数名
     */
    A_AFTER_FUNC("a_after_func", "XIP_WF_COMMON_0068", false, false, ""),

    /**
     * 节点属性 - 通知标题
     */
    A_TITLE("a_title", "XIP_WF_COMMON_0093", false, false, ""),

    /**
     * 节点属性 - 超时天数
     */
    A_TIMEOUT_DAYS("a_timeout_days", "XIP_WF_COMMON_0094", false, false, ""),

    /**
     * 节点属性 - 超时执行线
     */
    A_TIMEOUT_LINE_CODE("a_timeout_line_code", "XIP_WF_COMMON_0095", false, false, ""),

    /**
     * 节点属性 - 参与规避
     */
    A_IS_AVOID_FLAG("a_is_avoid_flag", "XIP_WF_COMMON_0096", false, true, "N"),

    /**
     * 节点属性 - 手动选人
     */
    A_IS_ASSGIN_EXECUTOR("a_is_assgin_executor", "XIP_WF_COMMON_0097", false, true, "N"),

    /**
     * 节点属性 - 征询
     */
    A_IS_CONSULT("a_is_consult", "XIP_WF_COMMON_0098", false, true, "N"),

    /**
     * 节点属性 - 委派
     */
    A_IS_DELEGATE("a_is_delegate", "XIP_WF_COMMON_0099", false, true, "N"),

    /**
     * 节点属性 - 允许回退
     */
    A_IS_ALLOW_BACK("a_is_allow_back", "XIP_WF_COMMON_0100", false, true, "N"),

    /**
     * 节点属性 - 允许撤办
     */
    A_IS_ALLOW_REVOKE("a_is_allow_revoke", "XIP_WF_COMMON_0101", false, true, "N"),

    /**
     * 节点属性 - 客户化待办
     */
    A_IS_CUSTOM_TASK("a_is_custom_task", "XIP_WF_COMMON_0102", false, true, "N"),

    /**
     * 节点属性 - 是否提醒
     */
    A_IS_NOTICE("a_is_notice", "XIP_WF_COMMON_0103", false, true, "N"),

    /**
     * 节点属性 - 会签规则
     */
    A_COUNTERSIGN_RULE("a_countersign_rule", "XIP_WF_COMMON_0104", false, false, ""),

    /**
     * 节点属性 - 会签比例
     */
    A_COUNTERSIGN_PERCENT("a_countersign_percent", "XIP_WF_COMMON_0105", false, false, ""),

    /**
     * 节点属性 - 回退后业务状态信息
     */
    A_BIZ_STATUS("a_biz_status", "XIP_WF_COMMON_0106", false, false, ""),

    /**
     * 节点属性 - 是否驳回
     */
    A_IS_REJECT("a_is_reject", "XIP_WF_COMMON_0107", false, false, ""),

    /**
     * 节点属性 - 驳回按钮名称
     */
    A_REJECT_CST_NAME("a_reject_cst_name", "XIP_WF_COMMON_0108", false, false, ""),

    /**
     * 节点属性 - 驳回后业务状态信息
     */
    A_BIZ_REJECT_STATUS("a_biz_reject_status", "XIP_WF_COMMON_0109", false, false, ""),

    /**
     * 节点属性 - 平板端表单类型
     */
    A_PAD_FORM_TYPE("a_pad_form_type", "XIP_WF_COMMON_0081", false, true, ""),

    /**
     * 节点属性 - 平板端表单 - 静态表单
     */
    A_PAD_STATIC_FORM("a_pad_static_form", "XIP_WF_COMMON_0071", false, true, ""),

    /**
     * 节点属性 - 平板端表单 - 变量表单
     */
    A_PAD_VAR_FORM("a_pad_var_form", "XIP_WF_COMMON_0072", false, true, ""),

    /**
     * 节点属性 - 平板端表单 - 路由表单
     */
    A_PAD_ROUTE_FORM("a_pad_route_form", "XIP_WF_COMMON_0073", false, true, ""),

    /**
     * 节点属性 - 手机端表单类型
     */
    A_M_FORM_TYPE("a_m_form_type", "XIP_WF_COMMON_0082", false, true, ""),

    /**
     * 节点属性 - 手机端表单 - 静态表单
     */
    A_M_STATIC_FORM("a_m_static_form", "XIP_WF_COMMON_0075", false, true, ""),

    /**
     * 节点属性 - 手机端表单 - 变量表单
     */
    A_M_VAR_FORM("a_m_var_form", "XIP_WF_COMMON_0076", false, true, ""),

    /**
     * 节点属性 - 手机端表单 - 路由表单
     */
    A_M_ROUTE_FORM("a_m_route_form", "XIP_WF_COMMON_0077", false, true, ""),

    /**
     * 节点属性 - 是否转发
     */
    A_IS_FORWARD("a_is_forward", "XIP_WF_COMMON_0110", false, false, ""),

    /**
     * 节点属性 - 是否动态节点
     */
    A_IS_DYNAMIC_NODE("a_is_dynamic_node", "XIP_WF_COMMON_A_IS_DYNAMIC_NODE", false, false, ""),

    /**
     * 节点属性 - 动态节点 - 函数类型 程序类型：根据WF_FUNC_TYPE_VS值集获取
     */
    A_DYNA_FUNC_TYPE("a_dyna_func_type", "XIP_WF_COMMON_A_DYNA_FUNC_TYPE", false, false, ""),

    /**
     * 节点属性 - 动态节点 - 函数名
     */
    A_DYNA_FUNC_NAME("a_dyna_func_name", "XIP_WF_COMMON_A_DYNA_FUNC_NAME", false, false, "");

    public final String code;
    public final String name;
    public final boolean entityProperty;
    public final boolean required;
    public final String defaultValue;

    ActivityPropertyEnum(String code, String name, Boolean entityProperty, Boolean required, String defaultValue) {
        this.code = code;
        this.name = name;
        this.required = required;
        this.entityProperty = entityProperty;
        this.defaultValue = defaultValue;
    }

    public static void main(String[] args) {
        for (ActivityPropertyEnum processPropertyEnum : ActivityPropertyEnum.values()) {
            System.out.println(processPropertyEnum);
            System.out.println(processPropertyEnum.getName());
        }
    }
}
