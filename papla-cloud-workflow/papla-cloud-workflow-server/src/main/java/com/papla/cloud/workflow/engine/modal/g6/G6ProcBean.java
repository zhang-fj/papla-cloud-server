package com.papla.cloud.workflow.engine.modal.g6;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @ClassName: G6ProcBean
 * @Description:
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class G6ProcBean {


    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 流程ID
     */
    private String processId;

    /**
     * 实体ID
     */
    private String entityId;

    /**
     * 流程编码，全局唯一
     */
    private String processCode;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程版本，自增
     */
    private BigDecimal processVerison;

    /**
     * 流程分类：P-公共流程，O-组织流程
     */
    private String processCategory;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 流程描述
     */
    private String processDesc;

    /**
     * 流程配置JSON文件
     */
    private String processJson;

}
