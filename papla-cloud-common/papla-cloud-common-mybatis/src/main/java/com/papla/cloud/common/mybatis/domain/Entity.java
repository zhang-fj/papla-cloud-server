package com.papla.cloud.common.mybatis.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: Entity.java
 * @Description: TODO 普通的数据库映射实体公共父类，提供普通业务实体数据库映射的公共字段，及统一设置公共字段的方法
 * @date 2018年9月2日
 */
@Setter
@Getter
public class Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键")
    protected String id;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    protected String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    protected Date createDt;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    protected String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    protected Date updateDt;

    /**
     * 版本标识
     */
    @ApiModelProperty("版本标识")
    protected Integer version;

    /**
     * 是否启用：是（Y）；否（N）
     */
    @ApiModelProperty("是否启用：是（Y）；否（N）")
    private String enabled;

    /**
     * @param id
     * @return void
     * @throws
     * @Title: setId
     * @Description: 默认设置主键
     */
    public void setId(String id) {
        //当实体主键由外部提供时，屏蔽掉自动设置主键，例如：主键有前端自动生成；当其他表完全复制数据（包括主键复制）时
        if (StringUtils.isBlank(id)) {
            this.id = null;
        } else {
            if (StringUtils.isBlank(this.id)) {
                this.id = id;
            }
        }
    }

    /**
     * @param userId
     * @return void
     * @throws
     * @Title: setWhoForInsert
     * @Description: 插入时，设置创建用户，创建时间，更新用户，更新时间
     */
    public void setWhoForInsert(String userId) {
        this.createBy = userId;
        this.createDt = new Date();
        this.updateBy = userId;
        this.updateDt = createDt;
    }

    /**
     * @param userId
     * @return void
     * @throws
     * @Title: setWhoForUpdate
     * @Description: 修改时，设置更新用户，更新时间
     */
    public void setWhoForUpdate(String userId) {
        this.updateBy = userId;
        this.updateDt = new Date();
    }

}
