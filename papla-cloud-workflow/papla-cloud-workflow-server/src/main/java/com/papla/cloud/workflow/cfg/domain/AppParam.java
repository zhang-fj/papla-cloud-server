package com.papla.cloud.workflow.cfg.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: AppParam
* @Description: TODO 应用参数管理
* @author 
* @date 2021-10-15
* @version V1.0
*/
@Getter
@Setter
public class AppParam extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 参数ID
	*/
	@ApiModelProperty(value = "参数ID")
	private String paramId;

	/**
	* 应用ID
	*/
	@ApiModelProperty(value = "应用ID")
	private String appId;

	/**
	* 参数编码
	*/
	@ApiModelProperty(value = "参数编码")
	private String paramCode;

	/**
	* 参数名称
	*/
	@ApiModelProperty(value = "参数名称")
	private String paramName;

	/**
	* 参数值
	*/
	@ApiModelProperty(value = "参数值")
	private String paramValue;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.paramId=this.id;
	}

	public String getId(){
	    return this.paramId;
    }

}