package com.papla.cloud.workflow.cfg.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: EntityStatus
* @Description: TODO 实体状态管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Getter
@Setter
public class EntityStatus extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 枚举ID
	*/
	@ApiModelProperty(value = "枚举ID")
	private String enumId;

	/**
	* 实体ID
	*/
	@ApiModelProperty(value = "实体ID")
	private String entityId;

	/**
	* 状态分类：A-起草，R-撤回、C-运行中、D-驳回、E-结束
	*/
	@ApiModelProperty(value = "状态分类")
	private String statusCategory;

	/**
	* 状态编码
	*/
	@ApiModelProperty(value = "状态编码")
	private String statusCode;

	/**
	* 状态名称
	*/
	@ApiModelProperty(value = "状态名称")
	private String statusName;

	/**
	* 状态描述
	*/
	@ApiModelProperty(value = "状态描述")
	private String statusDesc;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.enumId=this.id;
	}

	public String getId(){
	    return this.enumId;
    }

}