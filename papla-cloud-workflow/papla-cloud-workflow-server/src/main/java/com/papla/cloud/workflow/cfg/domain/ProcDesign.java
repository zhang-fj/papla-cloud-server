package com.papla.cloud.workflow.cfg.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: ProcDesign
* @Description: TODO 设计流程管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Getter
@Setter
public class ProcDesign extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 流程ID
	*/
	@ApiModelProperty(value = "流程ID")
	private String processId;

	/**
	* 应用ID
	*/
	@ApiModelProperty(value = "应用ID")
	private String appId;

	/**
	* 组织ID
	*/
	@ApiModelProperty(value = "组织ID")
	private String orgId;

	/**
	* 实体ID
	*/
	@ApiModelProperty(value = "实体ID")
	private String entityId;

	/**
	* 流程编码，全局唯一
	*/
	@ApiModelProperty(value = "流程编码")
	private String processCode;

	/**
	* 流程名称
	*/
	@ApiModelProperty(value = "流程名称")
	private String processName;

	/**
	* 流程版本，自增
	*/
	@ApiModelProperty(value = "流程版本")
	private BigDecimal processVersion;

	/**
	* 流程分类：P-公共流程，O-组织流程
	*/
	@ApiModelProperty(value = "流程分类")
	private String processCategory;

	/**
	* 流程描述
	*/
	@ApiModelProperty(value = "流程描述")
	private String processDesc;

	/**
	* 流程配置XML文件
	*/
	@ApiModelProperty(value = "流程配置XML文件")
	private String processJson;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.processId=this.id;
	}

	public String getId(){
	    return this.processId;
    }

}