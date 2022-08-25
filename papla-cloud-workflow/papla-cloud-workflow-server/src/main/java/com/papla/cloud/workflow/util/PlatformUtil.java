package com.papla.cloud.workflow.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ClassName:PlatformUtil Function: 平台工具类
 * @author GuoXiuFeng
 * @version Ver 1.0
 * @Date 2014年9月18日上午10:23:16
 * @since Ver 1.0
 */
public class PlatformUtil {
    private static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static String dbType = "mysql"; // 平台数据库类型
    public static String contextPath;

    /**
     * getDbType:获取数据库类型
     *
     * @return 返回数据库类型，如果获取时发生异常按Oracle处理
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public static String getDbType() {
        if (dbType != null) {
            return dbType;
        } else {
            try {
                dbType = "";
                return dbType;
            } catch (Exception e) {
                log.error(e);
                return "oracle";
            }
        }
    }

	public static String getSystemParamVal(String noticeMode) {
		return null;
	}

	public static String getParamVal(String string, String string2, String string3) {
		return null;
	}

}
