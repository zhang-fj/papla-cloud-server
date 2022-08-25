package com.papla.cloud.generate.generate.util;

import org.apache.commons.configuration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sql字段转java
 *
 * @author Zheng Jie
 * @date 2019-01-03
 */
public class ColUtil {
    private static final Logger log = LoggerFactory.getLogger(ColUtil.class);

    /**
     * 转换mysql数据类型为java数据类型
     *
     * @param type 数据库字段类型
     * @return String
     */
    public static String cloToJava(String type) {
        Configuration config = getConfig();
        assert config != null;
        return config.getString(type, "unknowType");
    }

    /**
     * 获取配置信息
     */
    public static PropertiesConfiguration getConfig() {
        return getConfig("generator.properties");
    }

    /**
     * 获取配置信息
     */
    public static PropertiesConfiguration getConfig(String fileName) {
        try {
            return new PropertiesConfiguration(fileName);
        } catch (ConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 转换mysql数据类型为jdbcType数据类型
     *
     * @param type 数据库字段类型
     * @return String
     */
    public static String cloToJdbcType(String type) {
        Configuration config = getConfig("jdbctype.properties");
        assert config != null;
        return config.getString(type, "unknowType");
    }
}
