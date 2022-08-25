package com.papla.cloud.admin.auth.domain;

import com.papla.cloud.common.mybatis.domain.Entity;

import lombok.Getter;
import lombok.Setter;

/**
* @Title:                RolePerm
* @Description: TODO   角色资源分配管理
* @author                
* @date                2021-09-21
* @version            V1.0
*/
@Getter
@Setter
public class RolePerm extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 角色ID
	*/
	private String roleId;

	/**
	* 权限ID
	*/
	private String permId;

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}