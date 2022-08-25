package com.papla.cloud.workflow.util;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author linpeng
 * @ClassName: DBUtil
 * @Description: 数据库对象特殊处理工具类
 * @date 2015年6月18日 上午8:41:11
 */
public class DBUtil {

    // 日志记录器
    private static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    /**
     * @param connection
     * @param procedureName
     * @param paramsAl      存储过程接收的传入参数队列
     * @return String    返回一个XML串  <result><flag></flag><msg></msg><data><row></row><row></row></data></result>
     * @throws Exception
     * @Title: executeProcedure
     * @Description: 执行存储过程 （在Oracle与MySQL数据库上测试通过）
     */
    public static String executeProcedure(Connection connection, String procedureName, ArrayList<String> paramsAl) throws Exception {

        String sql = "call " + procedureName + "(";
        if (paramsAl == null) {
            paramsAl = new ArrayList<String>();
        }
        ;
        int argsLength = paramsAl.size();
        for (int i = 0; i < argsLength; i++) {
            sql += "?,";
        }
        sql += "?)";
        log.info("存储过程  ：" + procedureName);
        CallableStatement cs = connection.prepareCall(sql);

        for (int i = 0; i < argsLength; i++) {
            if (paramsAl.get(i) == null) {
                cs.setString(i + 1, null);
                log.info("   参数 " + i + ":" + null);
            } else {
                cs.setString(i + 1, paramsAl.get(i));
                log.info("   参数 " + i + ":" + paramsAl.get(i));
            }
        }

        //目前用clob来接收，以防返回字符串长度过长
        cs.registerOutParameter(argsLength + 1, Types.CLOB);
        cs.execute();
        Clob clob = cs.getClob(argsLength + 1);

        return (clob != null ? clob.getSubString(1, (int) clob.length()) : "");

    }
    
}