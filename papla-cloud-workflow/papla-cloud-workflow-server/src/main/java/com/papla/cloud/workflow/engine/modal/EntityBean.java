package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @ClassName: EntityBean
 * @Description: 实体数据项
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class EntityBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 实体ID
     */
    private String entityId;

    /**
     * 实体编码
     */
    private String entityCode;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 实体描述
     */
    private String entityDesc;

    /**
     * 回调函数类型: PROC-存储过程, WEB- WebService服务, JAVA - Java方法
     */
    private String funcType;

    /**
     * 回调函数
     */
    private String funcValue;

    /**
     * 业务表名
     */
    private String buzTableName;

    /**
     * 业务主键名
     */
    private String busTablePk;

    /**
     * 获取授权审批人实现类
     */
    private String authUserCls;

}
