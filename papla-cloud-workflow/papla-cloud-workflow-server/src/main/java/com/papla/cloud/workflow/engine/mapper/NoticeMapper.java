/**
 *
 */
package com.papla.cloud.workflow.engine.mapper;

import java.util.HashMap;

/**
 * @ClassName: NoticeMapper
 *
 * @Description: 通知信息处理类
 *
 * @author linpeng
 * @date 2015年8月27日 下午2:19:51 
 *
 */
public interface NoticeMapper {

    /**
     * @Title: insertNotice
     * @Description: 保存通知信息
     * @param map
     * @throws Exception
     * @return void    返回类型
     */
     void storeNotice(HashMap<String, Object> map) throws Exception;

    /**
     * @Title: storeUserNotice
     * @Description: 保存用户与通知关联信息
     * @param map
     * @throws Exception
     * @return void    返回类型
     */
     void storeUserNotice(HashMap<String, Object> map) throws Exception;

}
