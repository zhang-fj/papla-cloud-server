package com.papla.cloud.workflow.engine.run.engine.core.controller;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: IObserver.java
 * @Package com.papla.cloud.wf.run.engine.core.controller
 * @Description: 监听者时间转发接口
 * @date 2021年8月29日 上午11:44:05
 */
public interface IObserver {

    /**
     * 事件转发接口
     *
     * @param actionEvent
     * @throws Exception
     */
     void distributeEventAction(ActionEvent actionEvent) throws Exception;
}
