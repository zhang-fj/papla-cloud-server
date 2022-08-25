package com.papla.cloud.workflow.engine.modal;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    protected String id;

    /**
     * 创建人
     */
    protected String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    protected Date createDt;

    /**
     * 更新人
     */
    protected String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    protected Date updateDt;

    /**
     * 是否启用：是（Y）；否（N）
     */
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