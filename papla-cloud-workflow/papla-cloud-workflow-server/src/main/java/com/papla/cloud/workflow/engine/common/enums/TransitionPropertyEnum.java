package com.papla.cloud.workflow.engine.common.enums;

import lombok.Getter;

@Getter
public enum TransitionPropertyEnum {

    /**
     * 连线属性 - 编码
     */
    T_CODE("t_code", "", true, true, ""),

    /**
     * 连线属性 - 连线类型：Y-是, N-否, T-超时。
     */
    T_TYPE("t_type", "", true, true, "N"),

    /**
     * 连线属性 - 程序类型：PROC-存储过程，WEB-WEB服务
     */
    T_FUNC_TYPE("t_func_type", "", true, true, ""),

    /**
     * 连线属性 - 函数值
     */
    T_FUNC_NAME("t_func_name", "", true, true, ""),

    /**
     * 连线属性 - 流经后业务状态信息
     */
    T_BIZ_STATUS("t_biz_status", "", true, true, "");

    private final String code;
    private final String name;
    private final boolean entityProperty;
    private final boolean required;
    private final String defaultValue;

    TransitionPropertyEnum(String code, String name, boolean entityProperty, boolean required, String defaultValue) {
        this.code = code;
        this.name = name;
        this.required = required;
        this.entityProperty = entityProperty;
        this.defaultValue = defaultValue;
    }

    public static void main(String[] args) {
        for (TransitionPropertyEnum processPropertyEnum : TransitionPropertyEnum.values()) {
            System.out.println(processPropertyEnum.getCode());
            System.out.println(processPropertyEnum.getName());
        }
    }
}
