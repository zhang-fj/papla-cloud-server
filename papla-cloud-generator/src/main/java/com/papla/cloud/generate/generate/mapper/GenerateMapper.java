package com.papla.cloud.generate.generate.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.generate.generate.domain.ModuleGenerateConfig;

public interface GenerateMapper {

    /**
     * @param params
     * @return List<ModuleGenerateConfig>
     * @throws Exception
     * @Title queryGenerateConfigList
     * @Description TODO 模板配置
     */
    public List<ModuleGenerateConfig> queryGenerateConfigList(Map<String, Object> params) throws Exception;

}
