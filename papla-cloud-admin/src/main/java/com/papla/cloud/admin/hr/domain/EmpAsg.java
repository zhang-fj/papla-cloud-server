package com.papla.cloud.admin.hr.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: EmpAsg
* @Description: TODO 员工分配管理
* @author 
* @date 2021-09-27
* @version V1.0
*/
@Getter
@Setter
public class EmpAsg extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 主键ID
	*/
	@ApiModelProperty(value = "主键ID")
	private String asgId;

	/**
	* 员工ID
	*/
	@ApiModelProperty(value = "员工ID")
	private String empId;

	/**
	* 岗位ID
	*/
	@ApiModelProperty(value = "岗位ID")
	private String postId;

	/**
	* 部门ID，冗余字段
	*/
	@ApiModelProperty(value = "部门ID，冗余字段")
	private String deptId;

	/**
	* 组织ID，冗余字段
	*/
	@ApiModelProperty(value = "组织ID，冗余字段")
	private String orgId;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.asgId=this.id;
	}

	public String getId(){
	    return this.asgId;
    }

}