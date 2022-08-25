package com.papla.cloud.workflow.engine.run.engine.core.controller;

import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ActionEvent.java
 * @Package com.papla.cloud.wf.run.engine.core.controller
 * @Description: 引擎流转控制器参数
 * @date 2021年8月29日 上午11:42:09
 */
@Setter
@Getter
public class ActionEvent {

    /**
     * 流程实例Bean
     */
    private ProcessInstanceBean bean;

    /**
     * 执行事件
     **/
    private String action;

    /**
     * 当前观察者代理对象
     */
    private IObserver observer;

    /**
     * 构造方法
     *
     * @param bean
     * @param action
     */
    public ActionEvent(ProcessInstanceBean bean, String action) {
        this.bean = bean;
        this.action = action;
    }

}
