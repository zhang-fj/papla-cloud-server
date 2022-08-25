package com.papla.cloud.generate.module.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleTableTemp
 * @Description: TODO   模块关联表模板配置管理
 * @date 2021-09-05
 */
@Getter
@Setter
public class ModuleTableTemp extends Entity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 关联表ID
     */
    private String tableId;

    /**
     * 模板配置
     */
    private String tempCfgId;

    /**
     * 模板文件
     */
    private String tempFileId;

    /**
     * 模板名称
     */
    private String tempCfgName;

    /**
     * 模板名称
     */
    private String tempFileName;

    /**
     * 模板分类
     */
    private String tempCate;

    /**
     * 是否覆盖
     */
    private String cover;

    /**
     * 生成菜单
     */
    private String genMenu;

    /**
     * 生成接口
     */
    private String ganApi;

    @Override
    public void setId(String id) {
        super.setId(id);
    }

}