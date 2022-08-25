package com.papla.cloud.admin.auth.domain;

import com.papla.cloud.common.mybatis.domain.Entity;

import lombok.Getter;
import lombok.Setter;

/**
* @Title:                Perm
* @Description: TODO   资源权限管理管理
* @author                
* @date                2021-09-21
* @version            V1.0
*/
@Getter
@Setter
public class Perm extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 菜单ID
	*/
	private String menuId;

	/**
	* 权限名称
	*/
	private String permName;

	/**
	* 权限类型
	*/
	private String permType;

	/**
	* 权限编码
	*/
	private String permCode;

	/**
	* 请求方式
	*/
	private String permMethod;

	/**
	* 资源路径
	*/
	private String permUrl;

	/**
	* 排序
	*/
	private Integer premSort;

	/**
	* 是否有效
	*/
	private String enabled;

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}