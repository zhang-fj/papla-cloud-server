package com.papla.cloud.workflow.engine.run.engine.core.instance;

import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ExecutionService.java
 * @Package com.papla.cloud.wf.run.engine.core.instance
 * @Description: 运行时【流程实例】处理服务接口
 * @date 2021年8月25日 下午4:12:12
 */
public interface ExecutionService {

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doStartProcess
     * @Description TODO    启动流程
     */
    public void doStartProcess(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doStartInstance
     * @Description TODO    重启流程实例
     */
    public void doStartInstance(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doRunProcess
     * @Description TODO    运行流程
     */
    public void doRunProcess(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doCancelProcess
     * @Description TODO    撤回流程
     */
    public void doCancelProcess(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doRejectProcess
     * @Description TODO    驳回流程
     */
    public void doRejectProcess(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doTerminateProcess
     * @Description TODO     终止流程
     */
    public void doTerminateProcess(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doCompleteProcess
     * @Description TODO    完成流程
     */
    public void doCompleteProcess(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title sigal
     * @Description TODO    引擎调度节点执行处理
     */
    public void sigal(ActionEvent actionEvent) throws Exception;
}
