package com.papla.cloud.generate.module.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleProject
 * @Description: TODO   项目管理管理
 * @date 2021-09-05
 */
@Getter
@Setter
public class ModuleProject extends Entity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目编码")
    private String projectCode;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**
     * 项目作者
     */
    @ApiModelProperty(value = "项目作者")
    private String projectAuth;

    /**
     * 工程名称
     */
    @ApiModelProperty(value = "工程名称")
    private String projectPath;

    /**
     * 前端路径
     */
    @ApiModelProperty(value = "前端路径")
    private String pagePath;

    /**
     * 数据库连接串含主机端口SID
     */
    @ApiModelProperty(value = "连接地址")
    private String dbUrl;

    /**
     * 数据库类型  ORA；oracle数据库 MYSQL； MYSQL数据库 MSSQL；SQL Server数据库
     */
    @ApiModelProperty(value = "数据库类型")
    private String dbType;

    /**
     * 数据库用户
     */
    @ApiModelProperty(value = "数据库用户")
    private String dbUser;

    /**
     * 数据库用户口令
     */
    @ApiModelProperty(value = "数据库用户口令")
    private String dbPass;

    @Override
    public void setId(String id) {
        super.setId(id);
    }

}