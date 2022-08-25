package com.papla.cloud.generate.module.domain;

import com.papla.cloud.common.mybatis.domain.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: Column
 * @Description: TODO   数据库列管理
 * @date 2021-09-03
 */
@Getter
@Setter
public class Column extends Entity {

    private static final long serialVersionUID = 362013224742003121L;

    private String tableName;

    private String columnName;

    private String columnType;

    private String keyType;

    private String notNull;

    private String remark;

    private String extra;

    private Integer sort;

    @Override
    public void setId(String id) {
        super.setId(id);
    }

}