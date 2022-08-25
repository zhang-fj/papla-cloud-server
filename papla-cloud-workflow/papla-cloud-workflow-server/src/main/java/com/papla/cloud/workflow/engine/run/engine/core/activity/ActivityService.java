package com.papla.cloud.workflow.engine.run.engine.core.activity;

import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ActivityService.java
 * @Package com.papla.cloud.wf.run.engine.core.activity
 * @Description: 运行时【流程节点】处理服务接口
 * @date 2021年8月29日 下午12:12:31
 */
public interface ActivityService {

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doStartActivity
     * @Description TODO    启动节点
     */
    public void doStartActivity(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doRunActivity
     * @Description TODO    运行节点
     */
    public void doRunActivity(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doCancelActivity
     * @Description TODO    撤消节点
     */
    public void doCancelActivity(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doRejectActivity
     * @Description TODO    驳回流程
     */
    public void doRejectActivity(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doTerminateActivity
     * @Description TODO    终止节点
     */
    public void doTerminateActivity(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doCompleteActivity
     * @Description TODO    完成节点
     */
    public void doCompleteActivity(ActionEvent actionEvent) throws Exception;
}
