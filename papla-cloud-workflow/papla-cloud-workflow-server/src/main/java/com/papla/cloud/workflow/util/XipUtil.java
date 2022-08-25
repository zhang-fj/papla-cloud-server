package com.papla.cloud.workflow.util;

import com.papla.cloud.workflow.cfg.mapper.MessageMapper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class XipUtil {

    /**
     * getMessage:(根据语言，消息编码，以及替代字获取消息内容（平台异常消息维护中定义）)
     *
     * @param language    语言
     * @param messageCode 异常消息编码
     * @param tokens      替代字
     * @return 返回消息内容
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public static String getMessage(String language, String messageCode, Map<String, String> tokens) {
        MessageMapper messageMapper = AppContext.getApplicationContext().getBean(MessageMapper.class);
        String content = messageMapper.getMessageContent(language,messageCode);
        if (content != null && tokens != null) {
            Iterator<Entry<String, String>> entries = tokens.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, String> entry = entries.next();
                content = content.replaceAll("\\$\\{" + entry.getKey() + "\\}", (String) entry.getValue());
            }
        }
        return content;
    }

    /**
     * getMessage:(根据当前Session内的语言，消息编码，以及替代字获取消息内容（平台异常消息维护中定义）)
     *
     * @param messageCode 异常消息编码
     * @param tokens      替代字
     * @return 返回消息内容
     * @author GuoXiuFeng
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public static String getMessage(String messageCode, Map<String, String> tokens) {
        String content = getMessage("zh", messageCode, tokens);
        return content;
    }
    
}
