//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : workflow2.0
//  @ File Name : EntityAttrBean.java
//  @ Date : 2014/6/20
//  @ Author : linp
//
//

package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

/**
 * 实体属性数据项
 **/
@Getter
@Setter
public class EntityAttrBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 属性ID
     */
    protected String attrId;

    /**
     * 实体ID
     */
    protected String entityId;

    /**
     * 属性编码
     */
    protected String attrCode;

    /**
     * 属性名称
     */
    protected String attrName;

    /**
     * 属性值类型：static-静态,dynamic-动态
     */
    protected String attrCategory;

    /**
     * 动态函数类型：PROC-存储过程，WEB- Web服务
     */
    protected String funcType;

    /**
     * 属性值，如果类型为动态，则需要指定存储过程或Web服务
     */
    protected String funcValue;

    /**
     * 属性描述
     */
    protected String attrDesc;

    /**
     * 属性数据类型：1-字符型，2-数值型，3-日期型
     */
    protected String attrDataType;

}
