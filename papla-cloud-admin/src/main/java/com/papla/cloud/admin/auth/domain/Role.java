package com.papla.cloud.admin.auth.domain;

import com.papla.cloud.common.mybatis.domain.Entity;

import lombok.Getter;
import lombok.Setter;

/**
* @Title:                Role
* @Description: TODO   角色管理管理
* @author                
* @date                2021-09-14
* @version            V1.0
*/
@Getter
@Setter
public class Role extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 角色编码
	*/
	private String roleCode;

	/**
	* 角色名称
	*/
	private String roleName;

	/**
	* 角色描述
	*/
	private String roleDesc;

	/**
	* 启用状态：Y-是；N-否
	*/
	private String enabled;

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}