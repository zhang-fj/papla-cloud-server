package com.papla.cloud.admin.hr.domain;

import java.math.BigDecimal;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: Dept
* @Description: TODO 部门管理
* @author 
* @date 2021-09-26
* @version V1.0
*/
@Getter
@Setter
public class Dept extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 部门id
	*/
	@ApiModelProperty(value = "部门id")
	private String deptId;

	/**
	* 部门编码
	*/
	@ApiModelProperty(value = "部门编码")
	private String deptCode;

	/**
	* 部门名称
	*/
	@ApiModelProperty(value = "部门名称")
	private String deptName;

	/**
	* 部门简称
	*/
	@ApiModelProperty(value = "部门简称")
	private String deptShortName;

	/**
	* 上级部门
	*/
	@ApiModelProperty(value = "上级部门")
	private String upDpId;

	/**
	* 所属组织
	*/
	@ApiModelProperty(value = "所属组织")
	private String orgId;

	/**
	* 排序
	*/
	@ApiModelProperty(value = "排序")
	private BigDecimal sort;

    /**
     * 下级部门数量
     */
    @ApiModelProperty(value = "下级部门数量")
	private Integer subDeptCount;

    /**
     * 岗位数量
     */
    @ApiModelProperty(value = "岗位数量")
    private Integer subPostCount;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.deptId=this.id;
	}

    public String getId(){
        return this.deptId;
    }

}