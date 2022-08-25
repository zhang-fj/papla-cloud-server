package com.papla.cloud.workflow.engine.run.engine.core.controller;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EngineEvent.java
 * @Package com.papla.cloud.wf.run.engine.core.controller
 * @Description: 抽象主题：流程引擎运转事件接口
 * @date 2021年8月29日 上午11:43:04
 */
public interface EngineEvent {

    /**
     * @param observer
     * @return void    返回类型
     * @Title: attachObserver
     * @Description: 增加一个观察者
     */
     void attachObserver(EngineEventObserver observer);

    /**
     * @param observer
     * @return void    返回类型
     * @Title: detachObserver
     * @Description: 删除一个观察者
     */
     void detachObserver(EngineEventObserver observer);

    /**
     * @param actionEvent
     * @return void    返回类型
     * @throws Exception
     * @Title: notifyObserver
     * @Description: 调用观察者的事件分发功能，通知所有登记过的观察者
     */
     void notifyObserver(ActionEvent actionEvent) throws Exception;
}
