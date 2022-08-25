package com.papla.cloud.generate.module.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleTable
 * @Description: TODO   关联表管理
 * @date 2021-09-03
 */
@Getter
@Setter
public class ModuleTable extends Entity {

    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 项目名称
     */
    private String projectId;

    /**
     * 模块ID
     */
    private String moduleId;

    /**
     * 关联表名
     */
    private String tableName;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 接口描述
     */
    private String apiAlias;

    @Override
    public void setId(String id) {
        super.setId(id);
    }

}