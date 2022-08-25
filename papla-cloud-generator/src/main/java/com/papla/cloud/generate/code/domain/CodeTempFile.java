package com.papla.cloud.generate.code.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: CodeTempFile
 * @Description: TODO   模板文件管理
 * @date 2021-09-05
 */
@Getter
@Setter
public class CodeTempFile extends Entity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 模板配置ID
     */
    private String cfgId;

    /**
     * 模板名称
     */
    private String tempName;

    /**
     * 模板文件
     */
    private String tempFileName;

    /**
     * 模板路径
     */
    private String tempFilePath;

    /**
     * 模板内容
     */
    private String tempContent;

    /**
     * 文件前缀
     */
    private String genFilePrefix;

    /**
     * 文件后缀
     */
    private String genFileSuffix;

    /**
     * 文件格式
     */
    private String genFileFormat;

    /**
     * 资源路径
     */
    private String genRootPath;

    /**
     * 模板分类
     */
    private String tempCate;

    /**
     * 是否必选
     */
    private String required;

    /**
     * 生成路径
     */
    private String genFilePath;

    private Integer sort;

    @Override
    public void setId(String id) {
        super.setId(id);
    }

}