package com.papla.cloud.workflow.engine.run.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessRunService.java
 * @Package com.papla.cloud.wf.run.service
 * @Description: 流程运行后台数据处理服务接口
 * @date 2021年8月24日 下午8:16:10
 */
public interface ProcessRunService {

    /**
     * @param processCode
     * @return String
     * @throws Exception
     * @Title getDeployIdByProcessCode
     * @Description TODO    根据【流程编码】查找【流程部署ID】
     */
    public String getDeployIdByProcessCode(String processCode) throws Exception;

    /**
     * @param processCode 流程编码
     * @param businessId  业务ID
     * @return {flag:0-正确、1-错误,msg:提示信息,instanceCode:流程实例编码}
     * @Title createInstanceByProcessCode
     * @Description TODO 按【流程编码】创建流程实例
     */
    public Map<String, String> createInstanceByProcessCode(String processCode, String businessId);

    /**
     * @param deployId   流程部署ID
     * @param businessId 业务ID
     * @return {flag:0-正确、1-错误,msg:提示信息,instanceCode:流程实例编码}
     * @Title createInstanceByDeployId
     * @Description TODO    按【流程部署ID】创建流程实例
     */
    public Map<String, String> createInstanceByDeployId(String deployId, String businessId);

    /**
     * @param instanceCode 流程实例CODE
     * @return {flag:0-正确、1-错误,msg:提示信息}
     * @Title restartInstance
     * @Description TODO    按【流程实例CODE】重新启动流程
     */
    public Map<String, String> restartInstance(String instanceCode);

    /**
     * @param instanceCode 流程实例CODE
     * @param attrMap      流程实体属性值
     * @param userIdArray  用户ID数组
     * @return 1、流程实例提交成功时，返回JSON如下，flag=0
     * {"flag":"","msg":""}
     * 2、方法执行出现异常时，返回JSON如下，flag=1，msg标签中为执行错误原因
     * {"flag":"","msg":""}
     * 3、提交不成功，需要手动选人，返回JSON如下，flag=2
     * {"flag":"","msg":"","multiselect":"","data":[{"userId":"","userName":"","empName":"","orgName":""},...]}
     * @Title submitProcess
     * @Description TODO    提交流程
     */
    public Map<String, Object> submitProcess(String instanceCode, Map<String, String> attrMap, String[] userIdArray);

    /**
     * @param processId
     * @param businessId
     * @param attrMap
     * @return 1、流程实例提交成功时，返回JSON如下，flag=0，instanceCode为流程的编码
     * {"flag":"","msg":"","instanceCode":""}
     * 2、方法执行出现异常时，返回JSON如下，flag=1，msg标签中为执行错误原因，instanceCode为空指 流程实例未生成时报错，instanceCode不为空指流程实例生成后，在提交时报错。
     * {"flag":"","msg":"","instanceCode":""}
     * 3、指提交不成功，flag=2，需要手动选人  msg 需要手动选人，instanceCode为创建流程的编码，row标签下为审批人的用户ID，用户名称，员工姓名
     * {"flag":"","msg":"","instanceCode":"","data":[{"userId":"","userName":"","empName":""},...]}
     * @Title startAndSubmitProcess
     * @Description TODO    启动并提交流程
     */
    public Map<String, Object> startAndSubmitProcess(String processId, String businessId, HashMap<String, String> attrMap);

    /**
     * @param taskId
     * @param lineCode
     * @param comment
     * @param userIdArray
     * @return 1.待办完成成功时，返回JSON如下，flag=0
     * {"flag":"","msg":""}
     * 2.方法执行出现异常时，返回JSON如下，flag=1，msg标签中为执行错误原因。
     * {"flag":"","msg":""}
     * 3.待办处理未完成，需要手动选人，flag=2， row标签下为审批人的用户ID，用户名称，员工姓名
     * {"flag":"","msg":"","data":[{"userId":"","userName":"","empName":""},...]}
     * @Title completeWorkItem
     * @Description TODO    完成待办
     */
    public Map<String, Object> completeWorkItem(String taskId, String lineCode, String comment, String[] userIdArray);

    /**
     * @param instanceCode
     * @return {flag:0-正确、1-错误,msg:提示信息}
     * @Title revokeProcess
     * @Description TODO    撤回流程
     */
    public Map<String, Object> revokeProcess(String instanceCode);

    /**
     * @param taskId
     * @param approverComment
     * @return {flag:0-正确、1-错误,msg:提示信息}
     * @Title rejectProcess
     * @Description TODO    驳回流程
     */
    public Map<String, Object> rejectProcess(String taskId, String approverComment);

}
