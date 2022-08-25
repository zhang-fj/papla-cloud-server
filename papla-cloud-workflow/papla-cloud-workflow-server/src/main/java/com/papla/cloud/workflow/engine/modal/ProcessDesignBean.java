package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessDesignBean extends BaseBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    protected String appId;

    /**
     * 应用编码
     */
    protected String appCode;

    /**
     * 应用名称
     */
    protected String appName;

    /**
     * 流程ID
     */
    protected String processId;

    /**
     * 实体ID
     */
    protected String entityId;

    /**
     * 实体CODE
     */
    protected String entityCode;

    /**
     * 流程编码，全局唯一
     */
    protected String processCode;

    /**
     * 流程名称
     */
    protected String processName;

    /**
     * 流程版本，自增
     */
    protected Integer processVersion;

    /**
     * 流程分类：P-公共流程，O-组织流程
     */
    protected String processCategory;

    /**
     * 组织ID
     */
    protected String orgId;

    /**
     * 组织编码
     */
    protected String orgCode;

    /**
     * 组织名称
     */
    protected String orgName;

    /**
     * 流程描述
     */
    protected String processDesc;

    /**
     * 流程配置JSON文件
     */
    protected String processJson;

}
