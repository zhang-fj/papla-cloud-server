package com.papla.cloud.generate.code.domain;

import com.papla.cloud.common.mybatis.domain.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: CodeTempCfg
 * @Description: TODO   模板配置管理
 * @date 2021-09-05
 */
@Getter
@Setter
public class CodeTempCfg extends Entity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 模板配置编码
     */
    private String cfgCode;

    /**
     * 模板配置名称
     */
    private String cfgName;

    @Override
    public void setId(String id) {
        super.setId(id);
    }

}