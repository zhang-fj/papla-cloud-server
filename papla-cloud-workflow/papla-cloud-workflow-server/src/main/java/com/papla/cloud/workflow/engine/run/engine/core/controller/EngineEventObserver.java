package com.papla.cloud.workflow.engine.run.engine.core.controller;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: EngineEventObserver.java
 * @Package com.papla.cloud.wf.run.engine.core.controller
 * @Description: 抽象观察者：流程引擎处理转发接口
 * @date 2021年8月29日 上午11:43:44
 */
public abstract class EngineEventObserver implements IObserver {
    public Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    /*
     * (非 Javadoc)
     * <p>Title: distributeEventAction</p>
     * <p>Description: 将流程引擎事件分发给具体的观察者处理 </p>
     * @param actionEvent
     * @throws Exception
     * @see com.papla.cloud.wf.run.engine.core.controller.IObserver#distributeEventAction(com.papla.cloud.wf.run.engine.core.controller.ActionEvent)
     */
    @SuppressWarnings("rawtypes")
    public void distributeEventAction(ActionEvent actionEvent) throws Exception {
        // 当前观察者
        IObserver observer = actionEvent.getObserver();
        // 观察者实现方法
        String methodName = actionEvent.getAction();
        try {
            // 获取参数类型
            Class[] methodParams = new Class[]{actionEvent.getClass()};
            Method[] methodArray = observer.getClass().getMethods();
            for (Method m : methodArray) {
                if (m.getName().equals(methodName)) {
                    // 根据参数指定的方法名和参数类型，获取类的特定方法
                    Method md = observer.getClass().getMethod(methodName, methodParams);
                    // 调用
                    md.invoke(observer, new Object[]{actionEvent});
                    break;
                }
            }
        } catch (NoSuchMethodException e) {
            log.error("★★★★★★ Not found the method: listeners[" + observer.getClass().getName() + "] method → " + methodName);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
