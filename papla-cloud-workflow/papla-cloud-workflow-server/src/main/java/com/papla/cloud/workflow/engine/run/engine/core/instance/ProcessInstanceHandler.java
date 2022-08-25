package com.papla.cloud.workflow.engine.run.engine.core.instance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;


/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessInstanceHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.instance
 * @Description: 流程实例抽象类
 * @date 2021年8月26日 下午8:31:47
 */
public abstract class ProcessInstanceHandler {

    // 日志记录器
    public Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    // 实例状态处理类
    private InstanceStateHandler instanceStateHandler;

    public InstanceStateHandler getInstanceStateHandler() {
        return instanceStateHandler;
    }

    public void setInstanceStateHandler(InstanceStateHandler instanceStateHandler) {
        this.instanceStateHandler = instanceStateHandler;
    }

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toStart
     * @Description TODO 启动流程实例
     */
    public abstract void toStart(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toRun
     * @Description TODO 运行流程实例
     */
    public abstract void toRun(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toTerminate
     * @Description TODO 终止流程实例
     */
    public abstract void toTerminate(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toCancel
     * @Description TODO 撤消流程实例
     */
    public abstract void toCancel(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title toComplete
     * @Description TODO 完成流程实例
     */
    public abstract void toComplete(ProcessInstanceBean bean) throws Exception;

}
