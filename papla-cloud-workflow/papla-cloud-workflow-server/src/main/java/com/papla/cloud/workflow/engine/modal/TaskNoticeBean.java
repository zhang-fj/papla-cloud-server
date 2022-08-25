package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: TaskNoticeBean.java
 * @Package com.papla.cloud.wf.modal
 * @Description: 待办提醒VO类
 * @date 2021年8月30日 下午5:49:09
 */
@Getter
@Setter
public class TaskNoticeBean {


    public TaskNoticeBean() {
        super();
    }

    public TaskNoticeBean(String taskId, String recUserId, String taskTitle, String submitter, String submitterName) {
        this.taskId = taskId;
        this.recUserId = recUserId;
        this.taskTitle = taskTitle;
        this.submitter = submitter;
        this.submitterName = submitterName;
    }

    // 待办ID
    private String taskId;

    // 接收人ID
    private String recUserId;

    // 用户名
    private String recUserName;

    // 待办标题
    private String taskTitle;

    // 手机号码
    private String phoneNum;

    // 邮箱地址
    private String email;

    // webHost
    private String webHost;

    // 流程提交人
    private String submitter;

    /**
     * 流程提交人姓名
     */
    private String submitterName;

}
