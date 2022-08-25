package com.papla.cloud.workflow.cfg.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: App
* @Description: TODO 流程应用管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Getter
@Setter
public class App extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;

	/**
	* 应用ID
	*/
	@ApiModelProperty(value = "应用ID")
	private String appId;

	/**
	* 应用编码
	*/
	@ApiModelProperty(value = "应用编码")
	private String appCode;

	/**
	* 应用名称
	*/
	@ApiModelProperty(value = "应用名称")
	private String appName;

	/**
	* 应用类型
	*/
	@ApiModelProperty(value = "应用类型")
	private String appType;

	/**
	* 应用描述
	*/
	@ApiModelProperty(value = "应用描述")
	private String appDesc;

	/**
	* 应用语音
	*/
	@ApiModelProperty(value = "应用语音")
	private String appLang;

	/**
	* 验证地址
	*/
	@ApiModelProperty(value = "验证地址")
	private String appLoginUrl;

	/**
	* 客户端ID
	*/
	@ApiModelProperty(value = "客户端ID")
	private String clientId;

	/**
	* 客户端密码
	*/
	@ApiModelProperty(value = "客户端密码")
	private String clientSecret;

	/**
	* WEB应用主机可以是IP也可以是域名
	*/
	@ApiModelProperty(value = "WEB应用主机可以是IP也可以是域名")
	private String webHost;

	/**
	* WEB应用端口
	*/
	@ApiModelProperty(value = "WEB应用端口")
	private String webPort;

	/**
	* WEB功能名
	*/
	@ApiModelProperty(value = "WEB功能名")
	private String webProject;

	/**
	* 数据库类型  ORA；oracle数据库 MYSQL； MYSQL数据库 MSSQL；SQL Server数据库
	*/
	@ApiModelProperty(value = "数据库类型  ORA；oracle数据库 MYSQL； MYSQL数据库 MSSQL；SQL Server数据库")
	private String dbType;

	/**
	* 数据库连接串含主机端口SID
	*/
	@ApiModelProperty(value = "数据库连接串含主机端口SID")
	private String dbUrl;

	/**
	* 数据库用户
	*/
	@ApiModelProperty(value = "数据库用户")
	private String dbUser;

	/**
	* 数据库用户口令
	*/
	@ApiModelProperty(value = "数据库用户口令")
	private String dbPassword;

	@Override
	public void setId(String id) {
		super.setId(id);
	    this.appId=this.id;
	}

	public String getId(){
	    return this.appId;
    }

}