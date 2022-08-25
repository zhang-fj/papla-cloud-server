package com.papla.cloud.workflow.engine.run.engine.core;

import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessEngine.java
 * @Package com.papla.cloud.wf.run.engine.core
 * @Description: 流程引擎
 * @date 2021年8月24日 下午8:15:08
 */
public interface ProcessEngine {

    /**
     * @param bean
     * @return {flag:0-正确、1-错误,instanceCode:流程实例编码,msg:提示信息}
     * @throws Exception
     * @Title startProcess
     * @Description TODO 启动流程
     */
    public Map<String, String> startProcess(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return {flag:0-正确、1-错误,msg:提示信息}
     * @throws Exception
     * @Title restartInstance
     * @Description TODO    重启流程实例
     */
    public Map<String, String> restartInstance(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return {flag:0-正确、1-错误,msg:提示信息}
     * @throws Exception
     * @Title submitProcess
     * @Description TODO 提交流程
     */
    public Map<String, Object> submitProcess(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return Map<String, String>
     * @throws Exception
     * @Title revokeProcess
     * @Description TODO    撤回流程
     */
    public Map<String, String> revokeProcess(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return Map<String, String>
     * @throws Exception
     * @Title rejectProcess
     * @Description TODO    驳回流程
     */
    public Map<String, String> rejectProcess(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return Map<String, String>
     * @throws Exception
     * @Title stopProcess
     * @Description TODO    终止流程
     */
    public Map<String, String> stopProcess(ProcessInstanceBean bean) throws Exception;

    /**
     * @return void    返回类型
     * @throws Exception
     * @Title: close
     * @Description: 关闭流程引擎
     */
    public void close() throws Exception;

    /**
     * @param bean
     * @return Map<String, Object>
     * @throws Exception
     * @Title doProcessSubsequentPath
     * @Description TODO 计算流程后续执行路径(含节点审批人信息)
     */
    public Map<String, Object> doProcessSubsequentPath(ProcessInstanceBean bean) throws Exception;

}
