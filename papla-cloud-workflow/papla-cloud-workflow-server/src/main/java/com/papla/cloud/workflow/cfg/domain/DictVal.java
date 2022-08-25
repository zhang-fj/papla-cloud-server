package com.papla.cloud.workflow.cfg.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
* @Title:                DictVal
* @Description: TODO   字典明细管理
* @author                
* @date                2021-09-13
* @version            V1.0
*/
@Getter
@Setter
public class DictVal extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 字典id
	*/
	private String dictId;

	/**
	* 字典编码
	*/
	private String dictCode;

	/**
	* 字典标签
	*/
	private String dictLabel;

	/**
	* 字典值
	*/
	private String dictValue;

	/**
	* 排序
	*/
	private Integer sort;

	/**
	* 描述
	*/
	private String description;

	/**
	* 是否启用
	*/
	private String enabled;

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}