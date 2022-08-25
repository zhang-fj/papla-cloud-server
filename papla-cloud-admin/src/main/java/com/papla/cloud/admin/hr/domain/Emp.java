package com.papla.cloud.admin.hr.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
* @Title: Emp
* @Description: TODO 员工管理
* @author 
* @date 2021-09-27
* @version V1.0
*/
@Getter
@Setter
public class Emp extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 员工ID
	*/
	@ApiModelProperty(value = "员工ID")
	private String empId;

	/**
	* 员工编号
	*/
	@ApiModelProperty(value = "员工编号")
	private String empCode;

	/**
	* 员工姓名
	*/
	@ApiModelProperty(value = "员工姓名")
	private String empName;

    /**
     * 员工类型
     */
    @ApiModelProperty(value = "员工类型")
	private String empType;

    /**
     * 员工状态
     */
    @ApiModelProperty(value = "员工状态")
	private String empStatus;

    /**
     * 入职时间
     */
    @ApiModelProperty(value = "入职时间")
    private Date entryTime;

    /**
     * 离职时间
     */
    @ApiModelProperty(value = "离职时间")
    private Date deparTime;

	/**
	* 性别
	*/
	@ApiModelProperty(value = "性别")
	private String empSex;

	/**
	* 身份证
	*/
	@ApiModelProperty(value = "身份证")
	private String idNo;

	/**
	* 电话
	*/
	@ApiModelProperty(value = "电话")
	private String phoneNo;

    /**
     * 固定电话
     */
    @ApiModelProperty(value = "固定电话")
	private String fixedPhoneNo;

	/**
	* 地址
	*/
	@ApiModelProperty(value = "地址")
	private String address;

	/**
	* 邮箱
	*/
	@ApiModelProperty(value = "邮箱")
	private String email;

	/**
	* 出生日期
	*/
	@ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date birthday;


    /**
     * 用户名 返回时忽略字段
     */
	private String username;

    /**
     * 创建用户 返回时忽略字段
     */
	private String createUser;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.empId=this.id;
	}

	public String getId(){
	    return this.empId;
    }

    @JsonIgnore
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getCreateUser() {
        return createUser;
    }

    @JsonProperty
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}