package com.papla.cloud.workflow.cfg.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: EntityAttrs
* @Description: TODO 实体属性管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Getter
@Setter
public class EntityAttrs extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 属性ID
	*/
	@ApiModelProperty(value = "属性ID")
	private String attrId;

	/**
	* 实体ID
	*/
	@ApiModelProperty(value = "实体ID")
	private String entityId;

	/**
	* 属性编码
	*/
	@ApiModelProperty(value = "属性编码")
	private String attrCode;

	/**
	* 属性名称
	*/
	@ApiModelProperty(value = "属性名称")
	private String attrName;

	/**
	* 属性值类型：static-静态,dynamic-动态
	*/
	@ApiModelProperty(value = "值类型")
	private String attrCategory;

	/**
	* 动态函数类型：PROC-存储过程，WEB- Web服务
	*/
	@ApiModelProperty(value = "函数类型")
	private String funcType;

	/**
	* 属性值，如果类型为动态，则需要指定存储过程或Web服务
	*/
	@ApiModelProperty(value = "属性值")
	private String funcValue;

	/**
	* 属性描述
	*/
	@ApiModelProperty(value = "属性描述")
	private String attrDesc;

	/**
	* 属性数据类型：1-字符型，2-数值型，3-日期型
	*/
	@ApiModelProperty(value = "数据类型")
	private String attrDataType;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.attrId=this.id;
	}

	public String getId(){
	    return this.attrId;
    }

}