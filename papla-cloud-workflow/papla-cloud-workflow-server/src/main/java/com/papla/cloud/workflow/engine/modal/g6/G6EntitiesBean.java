package com.papla.cloud.workflow.engine.modal.g6;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class G6EntitiesBean {

    // 实体编码
    private String entitycode;
    // 实体名称
    private String entityname;
    // 回调函数类型
    private String funcType;
    // 回调函数
    private String funcName;
    // 包含业务实体属性
    private G6EntityAttrsBean attrs;
    // 包含业务状态信息
    private G6BizStatusBean bizs;
    // 包含流程
    private G6ProcessBean processes;

    private String appId;

    private String entityId;
    // 是否生效
    private String isEffective;

    /**
     * 创建日期
     **/
    private Date creationDate;
    /**
     * 创建人
     **/
    private String createBy;

    /**
     * 最后更新日期
     **/
    private Date lastUpdateDate;

    /**
     * 最后更新人
     **/
    private String lastUpdatedBy;

}
