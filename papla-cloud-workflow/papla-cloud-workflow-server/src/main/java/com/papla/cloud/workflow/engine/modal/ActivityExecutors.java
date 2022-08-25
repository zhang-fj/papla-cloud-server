package com.papla.cloud.workflow.engine.modal;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 调整Oracle存储过程或WebService获取节点执行人时的对应XML文件result标签的JavaBean对象
 *
 * @author zhangfj
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class ActivityExecutors {

    private String flag;
    private String msg;
    private String instanceCode;
    private String totalProperty;
    private List<ActivityExecutorBean> executors;

}
