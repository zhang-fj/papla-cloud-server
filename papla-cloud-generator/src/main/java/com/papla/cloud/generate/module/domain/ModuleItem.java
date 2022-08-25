package com.papla.cloud.generate.module.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleItem
 * @Description: TODO   模块管理管理
 * @date 2021-09-03
 */
@Getter
@Setter
public class ModuleItem extends Entity {

    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 模块编码
     */
    private String moduleCode;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块包名
     */
    private String packageName;

    /**
     * 模块路径
     */
    private String modulePath;

    @Override
    public void setId(String id) {
        super.setId(id);
    }

}