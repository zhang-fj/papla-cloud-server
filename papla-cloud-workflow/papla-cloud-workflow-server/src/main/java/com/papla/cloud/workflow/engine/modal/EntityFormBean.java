package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityFormBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 表单ID
     */
    private String formId;

    /**
     * 表单URL
     */
    private String formUrl;

    /**
     * 表单说明
     */
    private String formDesc;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 实体ID
     */
    private String entityId;

    /**
     * 客户端分类：PC-电脑客户端、PAD-平板客户端、Moblie-手机客户端
     */
    private String clientType;

}
