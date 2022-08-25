package com.papla.cloud.workflow.engine.run.engine.core.task;

import java.util.List;
import java.util.Map;

import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: WorkItemService.java
 * @Package com.papla.cloud.wf.run.engine.core.task
 * @Description: 待办处理服务接口
 * @date 2021年8月29日 下午1:43:20
 */
public interface WorkTaskService {

    /*===============	start	待办处理 	start 	================*/

    /**
     * @param bean
     * @return Map<String, Object>
     * @throws Exception
     * @Title completeWorkItem
     * @Description TODO    完成待办处理, 如: 同意 / 驳回处理
     */
    public Map<String, Object> completeWorkItem(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title completeTimeoutWorkItem
     * @Description TODO    完成超时待办处理
     */
    public void completeTimeoutWorkItem(ProcessInstanceBean bean) throws Exception;

    /**
     * @param taskId
     * @param consultUserId
     * @param comment
     * @return Map<String, Object>
     * @throws Exception
     * @Title doConsult
     * @Description TODO    征询处理
     */
    public Map<String, Object> doConsult(String taskId, String consultUserId, String comment) throws Exception;

    /**
     * @param taskId
     * @param comment
     * @return HashMap<String, String>
     * @throws Exception
     * @Title revokeConsultTask
     * @Description TODO    撤销征询处理
     */
    public Map<String, Object> revokeConsultTask(String taskId, String comment) throws Exception;

    /**
     * @param taskId
     * @param comment
     * @param processType
     * @return Map<String, Object>
     * @throws Exception
     * @Title closeConsultTask
     * @Description TODO    征询反馈处理
     */
    public Map<String, Object> closeConsultTask(String taskId, String comment, String processType) throws Exception;

    /**
     * @param taskId
     * @param assginUserId
     * @param comment
     * @return Map<String, Object>
     * @throws Exception
     * @Title doDelegate
     * @Description TODO    委派处理
     */
    public Map<String, Object> doDelegate(String taskId, String assginUserId, String comment) throws Exception;

    /**
     * @param taskId
     * @param userArray
     * @return void
     * @throws Exception
     * @Title doForward
     * @Description TODO    执行转发
     */
    public void doForward(String taskId, String[] userArray) throws Exception;

    /**
     * @param taskId
     * @param comment
     * @return Map<String, Object>
     * @throws Exception
     * @Title closeCopyToTask
     * @Description TODO    关闭抄送待办
     */
    public Map<String, Object> closeCopyToTask(String taskId, String comment) throws Exception;

    /**
     * @param taskId
     * @param backToTargetActId
     * @param comment
     * @return Map<String, Object>
     * @throws Exception
     * @Title doBackProcess
     * @Description TODO    回退处理
     */
    public Map<String, Object> doBackProcess(String taskId, String backToTargetActId, String comment) throws Exception;

    /*===============	end		待办处理 	end		================*/

    /*===============	start	待办查询 	start 	================*/

    /**
     * @param taskId
     * @return List<TransitionBean>
     * @throws Exception
     * @Title getButtonsByTaskId
     * @Description TODO    取得打开选中待办信息中按钮信息
     */
    public List<TransitionBean> getButtonsByTaskId(String taskId) throws Exception;

    /**
     * @param taskId
     * @return Map<String, Object>
     * @throws Exception
     * @Title getForwardExecutors
     * @Description TODO    获取待办转发执行人信息
     */
    public Map<String, Object> getForwardExecutors(String taskId) throws Exception;

    /**
     * @param params
     * @return TabPage<WorkItemBean>
     * @throws MessageException
     * @Title getUserTaskPage
     * @Description TODO    根据查询条件获取用户待办信息
     */
    public TabPage<WorkItemBean> getUserTaskPage(Map<String, Object> params) throws Exception;

    /**
     * @param instanceCode
     * @return List<WorkItemBean>
     * @throws Exception
     * @Title findWorkItemListByInstanceCode
     * @Description TODO    根据流程实例编码, 查询审批历史信息
     */
    public List<WorkItemBean> findWorkItemListByInstanceCode(String instanceCode) throws Exception;


    /*===============	end	待办查询	end		================*/

}
