package com.papla.cloud.workflow.cfg.mapper;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.workflow.cfg.domain.Message;
import org.apache.ibatis.annotations.Param;

/**
* @Title: Message
* @Description: TODO 消息定义表管理
* @author 
* @date 2021-10-05
* @version V1.0
*/
public interface MessageMapper extends BaseMapper<Message>{

    String getMessageContent(@Param("language") String language,@Param("messageCode") String messageCode);

}