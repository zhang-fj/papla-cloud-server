package com.papla.cloud.admin.hr.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: Post
* @Description: TODO 岗位管理
* @author 
* @date 2021-09-26
* @version V1.0
*/
@Getter
@Setter
public class Post extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 岗位ID
	*/
	@ApiModelProperty(value = "岗位ID")
	private String postId;

	/**
	* 岗位编码
	*/
	@ApiModelProperty(value = "岗位编码")
	private String postCode;

	/**
	* 岗位名称
	*/
	@ApiModelProperty(value = "岗位名称")
	private String postName;

	/**
	* 岗位描述
	*/
	@ApiModelProperty(value = "岗位描述")
	private String postDesc;

	/**
	* 岗位级别，值集编码：XSR_PUB_POST_LEVEL
	*/
	@ApiModelProperty(value = "岗位级别，值集编码：XSR_PUB_POST_LEVEL")
	private String postLevel;

	/**
	* 部门ID
	*/
	@ApiModelProperty(value = "部门ID")
	private String deptId;

	/**
	* 组织ID
	*/
	@ApiModelProperty(value = "组织ID")
	private String orgId;

    /**
     * 员工数量
     */
	private Integer subEmpCount;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.postId=this.id;
	}

    public String getId(){
        return this.postId;
    }

}