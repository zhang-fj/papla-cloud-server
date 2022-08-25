package com.papla.cloud.generate.module.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj·
 * @version V1.0
 * @Title: ModuleColumn
 * @Description: TODO   字段配置管理
 * @date 2021-09-04
 */
@Getter
@Setter
public class ModuleColumn extends Entity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 模块ID
     */
    private String moduleId;

    /**
     * 关联表ID
     */
    private String tableId;

    /**
     * 数据库表名
     */
    private String tableName;

    /**
     * 数据库列名
     */
    private String columnName;

    /**
     * 字段名称
     */
    private String aliasName;

    /**
     * 数据库列类型
     */
    private String columnType;

    /**
     * 数据库键类型
     */
    private String keyType;

    /**
     * 数据库列额外参数
     */
    private String extra;

    /**
     * 数据库列描述
     */
    private String remark;

    /**
     * 是否必填
     */
    private String notNull;

    /**
     * 是否在列表显示
     */
    private String listShow;

    /**
     * 是否表单显示
     */
    private String formShow;

    /**
     * 表单类型
     */
    private String formType;

    /**
     * 查询类型
     */
    private String queryType;

    /**
     * 查询方式
     */
    private String queryWay;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 关联字典
     */
    private String dictName;

    /**
     * 数据URL
     */
    private String dataUrl;

    @Override
    public void setId(String id) {
        super.setId(id);
    }

}