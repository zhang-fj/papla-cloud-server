package com.papla.cloud.generate.module.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: Table
 * @Description: TODO   数据库表管理
 * @date 2021-09-03
 */
@Getter
@Setter
public class Table extends Entity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 数据库名称
     */
    private String tableSchema;

    /**
     * 数据库表名
     */
    private String tableName;

    /**
     * 数据库引擎
     */
    private String engine;

    /**
     * 编码集
     */
    private String coding;

    /**
     * 备注
     */
    private String remark;

}