package com.papla.cloud.workflow.engine.modal;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeOutBean {

    private String processId;        // 流程ID
    private String instanceId;        // 实例ID
    private String actId;            // 节点ID
    private String actType;        // 节点类型
    private Date beginDate;            // 开始日期
    private int days;                // 超时天数
    private String assignUserId;    // 待办所有者
    private String taskId;            // 待办ID

}
