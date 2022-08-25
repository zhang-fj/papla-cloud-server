package com.papla.cloud.workflow.cfg.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: Entities
* @Description: TODO 流程实体管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Getter
@Setter
public class Entities extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

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
	* 实体编码，全局唯一
	*/
	@ApiModelProperty(value = "实体编码")
	private String entityCode;

	/**
	* 实体名称
	*/
	@ApiModelProperty(value = "实体名称")
	private String entityName;

	/**
	* 实体描述
	*/
	@ApiModelProperty(value = "实体描述")
	private String entityDesc;

	/**
	* 回调函数类型：PROC-存储过程, WEB-WebService服务
	*/
	@ApiModelProperty(value = "函数类型")
	private String funcType;

	/**
	* 回调函数
	*/
	@ApiModelProperty(value = "回调函数")
	private String funcValue;

	/**
	* 业务表名
	*/
	@ApiModelProperty(value = "业务表名")
	private String busTableName;

	/**
	* 业务主键名
	*/
	@ApiModelProperty(value = "业务主键名")
	private String busTablePk;

	/**
	* 获取授权审批人实现类
	*/
	@ApiModelProperty(value = "获取授权审批人实现类")
	private String authUserCls;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.entityId=this.id;
	}

	public String getId(){
	    return this.entityId;
    }
}