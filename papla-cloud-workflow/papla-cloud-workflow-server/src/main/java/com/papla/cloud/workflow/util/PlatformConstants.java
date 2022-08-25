package com.papla.cloud.workflow.util;

/**
 * @author linpeng
 * @ClassName: PlatformConstants
 * @Description: 平台常量信息
 * @date 2015年1月21日 上午11:00:27
 */
public class PlatformConstants {

    /*================================================
     *  系统参数常量
     * ==============================================*/

    /**
     * NOTICE_MODE	待办提醒模式： total-汇总提醒 , 和  detail-即时提醒
     */
    public static final String NOTICE_MODE = "NOTICE_MODE";

    /**
     * NOTICE_EMAIL	是否启用邮件提醒: Y-是, N-否
     */
    public static final String NOTICE_EMAIL = "NOTICE_EMAIL";

    /**
     * NOTICE_NOTE	是否启用短信提醒: Y-是, N-否
     */
    public static final String NOTICE_NOTE = "NOTICE_NOTE";

    /**
     * NOTICE_WEIXIN	是否启用微信提醒: Y-是, N-否
     */
    public static final String NOTICE_WEIXIN = "NOTICE_WEIXIN";
    /**
     * NOTICE_MQ	是否启用待办推送消息队列: Y-是, N-否
     */
    public static final String NOTICE_MQ = "NOTICE_MQ";

    /**
     * TASK_NOTICE_INTER 待办提醒接口实现类
     */
    public static final String TASK_NOTICE_INTER = "TASK_NOTICE_INTER";
    /**
     * TASK_NOTICE_MQ_IMPL 待办推送MQ接口实现类
     */
    public static final String TASK_NOTICE_MQ_IMPL = "TASK_NOTICE_MQ_IMPL";
    /**
     * IS_ENABLE_ARCH 流程实例是启用否归处理
     */
    public static final String IS_ENABLE_ARCH = "IS_ENABLE_ARCH";

    /*================================================
     *  系统值集常量
     * ==============================================*/

    /**
     * WF_INNER_ATTR_VS	内置属性值集
     */
    public static final String WF_INNER_ATTR_VS = "WF_INNER_ATTR_VS";

    /*================================================
     *  后台数据库类型说明：
     *  	oracle   - oracle数据库
     * 		mysql - MYSQL数据库
     * ==============================================*/

    /*================================================
     *  通知状态：
     *  	open   - 打开
     * 		closed - 关闭
     * ==============================================*/
    public static final String NOTICE_STATUS_OPEN = "open";

}
