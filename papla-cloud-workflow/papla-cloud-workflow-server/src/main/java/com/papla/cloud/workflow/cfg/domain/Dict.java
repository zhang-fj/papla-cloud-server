package com.papla.cloud.workflow.cfg.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
* @Title:                Dict
* @Description: TODO   字典管理管理
* @author                
* @date                2021-09-13
* @version            V1.0
*/
@Getter
@Setter
public class Dict extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 字典名称
	*/
	private String dictName;

	/**
	* 字典编码
	*/
	private String dictCode;

	/**
	* 是否启用
	*/
	private String enabled;

	/**
	* 描述
	*/
	private String dictDesc;

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}