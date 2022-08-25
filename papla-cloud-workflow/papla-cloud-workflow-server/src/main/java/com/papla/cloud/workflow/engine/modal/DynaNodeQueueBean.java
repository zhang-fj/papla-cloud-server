package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @ClassName: DynaNodeQueueBean
 * @Description: 动态节点审批队列
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class DynaNodeQueueBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    private String queueId;
    private String instanceCode;
    private String actCode;
    private int orderby;
    private String approver;
    private String nodeName;
    private String taskTitle;
    private String approveStatus;
    private String userId;
    private String userName;
    private String empId;
    private String empCode;
    private String empName;
    private String orgId;
    private String orgName;

}
