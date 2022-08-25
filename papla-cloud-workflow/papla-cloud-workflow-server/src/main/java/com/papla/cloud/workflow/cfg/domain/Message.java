package com.papla.cloud.workflow.cfg.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: Message
* @Description: TODO 消息定义表管理
* @author 
* @date 2021-10-05
* @version V1.0
*/
@Getter
@Setter
public class Message extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* ID
	*/
	@ApiModelProperty(value = "ID")
	private String mesId;

	/**
	* 异常消息编码
	*/
	@ApiModelProperty(value = "消息编码")
	private String mesCode;

	/**
	* 语言
	*/
	@ApiModelProperty(value = "语言")
	private String mesLang;

	/**
	* 内容
	*/
	@ApiModelProperty(value = "内容")
	private String mesContent;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.mesId=this.id;
	}

	public String getId(){
	    return this.mesId;
    }

}