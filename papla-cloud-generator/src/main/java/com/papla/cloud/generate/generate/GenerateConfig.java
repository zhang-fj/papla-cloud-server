package com.papla.cloud.generate.generate;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ModuleGenerateConfig.java
 * @Package com.papla.cloud.generate
 * @Description:
 * @date 2021年9月5日 下午9:49:12
 */
@Setter
@Getter
public class GenerateConfig {

    /**
     * 资源模式
     */
    private String srcMode;

    /**
     * 是否输出
     */
    private String cover;

    /**
     * 生成模板
     */
    private String template;

    /**
     * 输出路径
     */
    private String outFilePath;

    /**
     * 输出文件名
     */
    private String outFileName;

    /**
     * 生成数据
     */
    public Map<String, Object> generateData = new HashMap<String, Object>();


    /**
     * @param columnsData
     * @return void
     * @Title putColumnsData
     * @Description TODO 设置字段数据
     */
    public void putColumnsData(Map<String, Object> columnsData) {
        generateData.putAll(columnsData);
    }

    /**
     * @param baseData
     * @return void
     * @Title putBaseData
     * @Description TODO 设置基础数据
     */
    public void putBaseData(Map<String, Object> baseData) {
        generateData.putAll(baseData);
    }


}
