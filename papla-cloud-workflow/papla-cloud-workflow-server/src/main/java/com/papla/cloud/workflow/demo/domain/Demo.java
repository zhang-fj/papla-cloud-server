package com.papla.cloud.workflow.demo.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: Demo
* @Description: TODO 流程演示管理
* @author 
* @date 2021-10-03
* @version V1.0
*/
@Getter
@Setter
public class Demo extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 订单编号
	*/
	@ApiModelProperty(value = "订单编号")
	private String poNo;

	/**
	* 订单说明
	*/
	@ApiModelProperty(value = "订单说明")
	private String poDesc;

	/**
	* 供应商
	*/
	@ApiModelProperty(value = "供应商")
	private String vendor;

	/**
	* 组织
	*/
	@ApiModelProperty(value = "组织")
	private String orgId;

	/**
	* 部门
	*/
	@ApiModelProperty(value = "部门")
	private String deptId;

	/**
	* 采购员
	*/
	@ApiModelProperty(value = "采购员")
	private String buyer;

	/**
	* 订单金额
	*/
	@ApiModelProperty(value = "订单金额")
	private BigDecimal amt;

	/**
	* 采购日期
	*/
	@ApiModelProperty(value = "采购日期")
	private Date poDate;

	/**
	* 流程实例编码
	*/
	@ApiModelProperty(value = "流程实例编码")
	private String instCode;

	/**
	* 状态
	*/
	@ApiModelProperty(value = "状态")
	private String status;

	/**
	* 状态
	*/
	@ApiModelProperty(value = "状态")
	private String statusDesc;

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}