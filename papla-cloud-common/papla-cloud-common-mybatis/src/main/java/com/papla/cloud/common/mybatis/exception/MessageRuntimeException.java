package com.papla.cloud.common.mybatis.exception;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: MessageRuntimeException.java
 * @Description: TODO    包含业务实体的运行时消息异常（当业务方法无指定抛出异常时使用）
 * @date 2018年9月2日
 */

public class MessageRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * entity	引发异常的消息实体
     */
    private Object entity = null;

    /**
     * 构造包含异常信息，业务实体的运行时消息异常
     * @param message 异常信息
     * @author zhangfj
     * @date 2018年1月6日
     */
    public MessageRuntimeException(String message) {
        super(message);
    }

    /**
     * 构造包含异常信息，业务实体的运行时消息异常
     *
     * @param message 异常信息
     * @param entity     业务实体对象
     * @author zhangfj
     * @date 2018年1月6日
     */
    public MessageRuntimeException(String message, Object entity) {
        super(message);
        this.entity = entity;
    }

    /**
     * 构造包含异常信息，业务实体及嵌套异常运行时消息异常
     *
     * @param message 异常信息
     * @param cause   导致此异常被抛出的嵌套异常
     * @param entity     业务实体对象
     * @author zhangfj
     * @date 2018年1月7日
     */
    public MessageRuntimeException(String message, Throwable cause, Object entity) {
        super(message, cause);
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

}