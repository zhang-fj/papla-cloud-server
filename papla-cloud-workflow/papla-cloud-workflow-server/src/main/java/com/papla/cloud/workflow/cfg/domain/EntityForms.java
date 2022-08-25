package com.papla.cloud.workflow.cfg.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: EntityForms
* @Description: TODO 实体表单管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Getter
@Setter
public class EntityForms extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 表单ID
	*/
	@ApiModelProperty(value = "表单ID")
	private String formId;

	/**
	* 表单URL
	*/
	@ApiModelProperty(value = "表单URL")
	private String formUrl;

	/**
	* 表单说明
	*/
	@ApiModelProperty(value = "表单说明")
	private String formDesc;

	/**
	* 应用ID
	*/
	@ApiModelProperty(value = "应用ID")
	private String appId;

	/**
	* 实体ID
	*/
	@ApiModelProperty(value = "实体ID")
	private String entityId;

	/**
	* 客户端分类：PC-电脑客户端、PAD-平板客户端、Moblie-手机客户端
	*/
	@ApiModelProperty(value = "客户端分类")
	private String clientType;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.formId=this.id;
	}

	public String getId(){
	    return this.formId;
    }
}