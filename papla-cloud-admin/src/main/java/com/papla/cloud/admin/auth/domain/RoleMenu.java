package com.papla.cloud.admin.auth.domain;

import com.papla.cloud.common.mybatis.domain.Entity;

import lombok.Getter;
import lombok.Setter;

/**
* @Title:                RoleMenu
* @Description: TODO   角色菜单分配管理
* @author                
* @date                2021-09-21
* @version            V1.0
*/
@Getter
@Setter
public class RoleMenu extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 角色编码
	*/
	private String roleId;

	/**
	* 角色名称
	*/
	private String menuId;

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}