package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;


/**
 * 连线信息数据项
 **/
@Getter
@Setter
public class TransitionBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    private String processId;    // 流程ID
    private String deployId;    //部署ID
    private String id;            // 连线ID
    private String code;        // 连线编码
    private String name;        // 连线名称
    private String category;    // 连线分类(运行时使用) : Y-同意，N-驳回
    private String funcType;    // 函数类型 : P-存储过程，S-Web服务
    private String funcName;    // 函数名称
    private String type;        // 连线类型(定义时使用):Y-是 , N-否
    private int fromActId;        // 迁出节点序号
    private int toActId;        // 迁入节点序号
    private String instanceId;        // 实例ID
    private String state;        // 连线状态
    private String disable;    // 按钮是否可用
    private String lineNum;    // 连线序号
    private String bizStatus;    // 业务状态ID
    private int orderBy;

}
