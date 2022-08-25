package com.papla.cloud.admin.hr.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Title: Org
 * @Description: TODO 组织管理
 * @author
 * @date 2021-09-26
 * @version V1.0
 */
@Getter
@Setter
public class Org extends Entity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 组织ID
     */
    @ApiModelProperty(value = "组织ID")
    private String orgId;

    /**
     * 组织编码
     */
    @ApiModelProperty(value = "组织编码")
    private String orgCode;

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称")
    private String orgName;

    /**
     * 组织简称
     */
    @ApiModelProperty(value = "组织简称")
    private String orgShortName;

    /**
     * 上级组织
     */
    @ApiModelProperty(value = "上级组织")
    private String upOrgId;

    /**
     * 类型：O，实体组织；V，虚拟组织
     */
    @ApiModelProperty(value = "组织类型")
    private String orgType;

    /**
     * 下级组织数量
     */
    @ApiModelProperty(value = "下级组织数量")
    private Integer subOrgCount;

    /**
     * 部门数量
     */
    @ApiModelProperty(value = "部门数量")
    private Integer subDeptCount;

    /**
     * 岗位数量
     */
    @ApiModelProperty(value = "岗位数量")
    private Integer subPostCount;

    @Override
    public void setId(String id) {
        super.setId(id);
        this.orgId=this.id;
    }

    public String getId(){
        return this.orgId;
    }

}