package com.papla.cloud.workflow.engine.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.papla.cloud.common.security.utils.JwtUtils;

/**
 * 功能： 当前用户信息
 * 作者：林鹏
 * 时间：2014年7月13日
 * 版本：2.0
 * <p>
 * 修改记录
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class CurrentUserUtil {

    /**
     * 取得当前时间
     *
     * @return
     * @throws Exception
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    ;

    /**
     * 取得当前项目的年月日
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String getCurrentYearAndMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 取得当前时间字符串
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


    /**
     * 取得当前用户ID
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentUserId() {
        return JwtUtils.getUserId();
    }

    /**
     * @return String    返回类型
     * @Title: getUserName
     * @Description: 取得用户名称
     */
    public static String getUserName() {
        return JwtUtils.getUsername();
    }

    /**
     * @return String   返回类型
     * @Title: getEmpName
     * @Description: 取得员工姓名
     */
    public static String getEmpName() {
        return JwtUtils.getUsername();
    }


    
}
