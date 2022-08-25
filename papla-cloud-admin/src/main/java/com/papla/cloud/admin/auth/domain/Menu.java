package com.papla.cloud.admin.auth.domain;

import com.papla.cloud.common.mybatis.domain.Entity;

import lombok.Getter;
import lombok.Setter;

/**
* @Title:                Menu
* @Description: TODO   菜单管理管理
* @author                
* @date                2021-09-10
* @version            V1.0
*/
@Getter
@Setter
public class Menu extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 上级菜单ID
	*/
	private String pid;

	/**
	* 子菜单数目
	*/
	private Integer subCount;

	/**
	* 菜单类型
	*/
	private String type;

	/**
	* 菜单标题
	*/
	private String title;

	/**
	* 组件名称
	*/
	private String name;

	/**
	* 组件
	*/
	private String component;

	/**
	* 排序
	*/
	private Integer menuSort;

	/**
	* 图标
	*/
	private String icon;

	/**
	* 链接地址
	*/
	private String path;

	/**
	* 是否外链
	*/
	private String iFrame;

	/**
	* 缓存
	*/
	private String cache;

	/**
	* 隐藏
	*/
	private String hidden;

    /**
     * 是否为叶子节点
     */
	public boolean getLeaf(){
        return subCount == null || subCount == 0;
    }

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}