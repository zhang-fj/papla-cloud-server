package com.papla.cloud.admin.auth.domain;

import com.papla.cloud.common.mybatis.domain.Entity;

import lombok.Getter;
import lombok.Setter;

/**
* @Title:                UserRole
* @Description: TODO   角色管理管理
* @author                
* @date                2021-09-14
* @version            V1.0
*/
@Getter
@Setter
public class UserRole extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 用户ID
	*/
	private String userId;

	/**
	* 角色ID
	*/
	private String roleId;

	/**
	* 默认角色：Y-是；N-否
	*/
	private String isDefault;

    private String roleCode;
    private String roleName;
    private String username;
    private String nickName;
    private String empCode;
    private String empName;

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}