package com.papla.cloud.workflow.engine.run.engine.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EngineEventHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.controller
 * @Description: 具体主题：流程引擎运转事件
 * @date 2021年8月29日 上午11:43:20
 */
@Service("engineEvent")
public class EngineEventHandler implements EngineEvent {
    @Resource
    private List<IObserver> observers;

    /*
     * (非 Javadoc)
     * <p>Title: attachObserver</p>
     * <p>Description: 增加一个观察者 </p>
     * @param observer
     * @see com.papla.cloud.wf.run.engine.core.controller.EngineEvent#attachObserver(com.papla.cloud.wf.run.engine.core.controller.EngineEventObserver)
     */
    public void attachObserver(EngineEventObserver observer) {
        observers.add(observer);
    }

    ;

    /*
     * (非 Javadoc)
     * <p>Title: detachObserver</p>
     * <p>Description:  删除一个观察者 </p>
     * @param observer
     * @see com.papla.cloud.wf.run.engine.core.controller.EngineEvent#detachObserver(com.papla.cloud.wf.run.engine.core.controller.EngineEventObserver)
     */
    public void detachObserver(EngineEventObserver observer) {
        observers.remove(observer);
    }

    ;

    /*
     * (非 Javadoc)
     * <p>Title: notifyObserver</p>
     * <p>Description: 调用观察者的事件分发功能，通知所有登记过的观察者 </p>
     * @param actionEvent
     * @throws Exception
     * @see com.papla.cloud.wf.run.engine.core.controller.EngineEvent#notifyObserver(com.papla.cloud.wf.run.engine.core.controller.ActionEvent)
     */
    public void notifyObserver(ActionEvent actionEvent) throws Exception {
        try {
            for (IObserver observer : observers) {
                // 设置观察者对象(使用了Spring AOP编程, 须记录监听者的代理对象)
                actionEvent.setObserver(observer);
                // 调用分发事件
                observer.distributeEventAction(actionEvent);
            }
        } catch (InvocationTargetException ex) {
            throw new Exception(ex.getTargetException().getMessage());

        } catch (Exception e) {
            throw e;
        }
    }

    ;
}
