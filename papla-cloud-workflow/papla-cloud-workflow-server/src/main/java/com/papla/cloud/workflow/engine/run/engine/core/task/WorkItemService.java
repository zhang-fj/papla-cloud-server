package com.papla.cloud.workflow.engine.run.engine.core.task;

import java.util.List;

import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TaskNoticeBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: WorkItemService.java
 * @Package com.papla.cloud.wf.run.engine.core.task
 * @Description: 运行时【流程待办】处理服务接口
 * @date 2021年8月29日 下午1:43:20
 */
public interface WorkItemService {


    /*===============	start	创建待办信息	start	===============*/

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title createSubmitterWorkItem
     * @Description TODO    创建提交人待办信息
     */
    public void createSubmitterWorkItem(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @param excutors
     * @return void
     * @throws Exception
     * @Title createCopyToWorkItem
     * @Description TODO 创建抄送节点待办信息
     */
    public List<TaskNoticeBean> createCopyToWorkItem(ProcessInstanceBean bean, List<ActivityExecutorBean> excutors) throws Exception;

    /**
     * @param bean
     * @param excutors
     * @return void
     * @throws Exception
     * @Title createAvoidWorkItem
     * @Description TODO 创建通知/会签节点的规避待办信息
     */
    public void createAvoidWorkItem(ProcessInstanceBean bean, List<ActivityExecutorBean> excutors) throws Exception;

    /**
     * @param bean
     * @param excutors
     * @return List<HashMap < String, String>>
     * @throws Exception
     * @Title createNormalWorkItem
     * @Description TODO 创建通知/会签节点的待办信息
     */
    public List<TaskNoticeBean> createNormalWorkItem(ProcessInstanceBean bean, List<ActivityExecutorBean> excutors) throws Exception;

    /*===============	end	创建待办信息	end	===============*/

    /*===============	start	关闭待办信息	start	===============*/

    /**
     * @param itemBean
     * @return void
     * @throws Exception
     * @Title closeTask
     * @Description TODO    关闭待办
     */
    public void closeTask(WorkItemBean itemBean) throws Exception;

    /**
     * @param bean
     * @param noticeNode
     * @return void
     * @throws Exception
     * @Title closeTimeoutTask
     * @Description TODO    关闭超时待办
     */
    public void closeTimeoutTask(ProcessInstanceBean bean, String noticeNode) throws Exception;

    /**
     * @param insId
     * @param actId
     * @param tskId
     * @return void
     * @throws Exception
     * @Title closeCountersignTask
     * @Description TODO    关闭回去待办
     */
    public void closeCountersignTask(String insId, String actId, String tskId) throws Exception;

    /*===============	end	关闭待办信息	end	===============*/

    /*===============	start	流程引擎处理	start	===============*/

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doCancelWorkItem
     * @Description TODO    流程撤消处理,关闭待办信息
     */
    public void doCancelWorkItem(ActionEvent actionEvent) throws Exception;

    /**
     * @param actionEvent
     * @return void
     * @throws Exception
     * @Title doRejectWorkItem
     * @Description TODO    流程驳回处理
     */
    public void doRejectWorkItem(ActionEvent actionEvent) throws Exception;

    /*===============	end	流程引擎处理	end	===============*/

}
