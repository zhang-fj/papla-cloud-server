package com.papla.cloud.workflow.engine.run.engine.core.task.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.papla.cloud.workflow.engine.run.engine.core.handler.impl.ActivityConfigExecutorServiceFacade;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.common.mybatis.page.PageUtils;
import com.papla.cloud.workflow.util.XipUtil;
import com.papla.cloud.workflow.engine.common.api.ExpressionService;
import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.dao.base.WorkFlowUserDAO;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ActivityPropertyBean;
import com.papla.cloud.workflow.engine.modal.DynaNodeQueueBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;
import com.papla.cloud.workflow.engine.run.dao.ProcessInstanceDAO;
import com.papla.cloud.workflow.engine.run.dao.WorkItemDAO;
import com.papla.cloud.workflow.engine.run.engine.core.common.EngineCommonHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;
import com.papla.cloud.workflow.engine.run.engine.core.controller.EngineEvent;
import com.papla.cloud.workflow.engine.run.engine.core.handler.ActivityExecutionPathService;
import com.papla.cloud.workflow.engine.run.engine.core.instance.ProcessInstanceHandler;
import com.papla.cloud.workflow.engine.run.engine.core.task.WorkTaskService;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: WorkItemServiceImpl.java
 * @Package com.papla.cloud.wf.run.engine.core.task.impl
 * @Description: 待办处理服务接口实现类
 * @date 2021年8月29日 下午1:44:52
 */
@Service("workTaskServiceImpl")
public class WorkTaskServiceImpl implements WorkTaskService {

    public Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    private EngineEvent engineEvent;

    @Resource
    private ExpressionService expressionService;

    @Resource
    private EngineCommonHandler engineCommonHandler;

    @Resource
    private ProcessEngineCommonService processEngineCommonService;

    @Resource
    private ActivityExecutionPathService activityExecutionPathService;

    @Resource
    private ActivityConfigExecutorServiceFacade activityConfigExecutorServiceFacade;

    @Resource
    private WorkItemDAO workItemDAO;

    @Resource
    private WorkFlowUserDAO workFlowUserDAO;

    @Resource
    private ActivityDAO activityDAO;

    @Resource
    private ProcessInstanceDAO processInstanceDAO;


    /**
     * @param workItemBean
     * @param userId
     * @return void
     * @throws Exception
     * @Title checkWorkItem
     * @Description TODO    校验【待办任务】
     */
    private void checkWorkItem(WorkItemBean workItemBean, String userId) throws Exception {

        //	判断【待办任务】是否存在
        if (workItemBean == null) {
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
        }

        //	获取当前【任务状态】
        String taskState = workItemBean.getTaskState();

        //	判断【待办任务】是否【关闭】
        if (WorkFlowConstants.TASK_STATE_CLOSED.equals(taskState)) {
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
        }

        //	判断【审批权限】是否【收回】
        if (!workItemBean.getAssignUser().equals(userId) && !workItemBean.getExecuteUser().equals(userId)) {
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0065", null));
        }

    }

    /*===============	start	创建待办信息	start	===============*/

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Map<String, Object> completeWorkItem(ProcessInstanceBean bean) throws Exception {

        Map<String, Object> reslut = new HashMap<String, Object>();

        try {

            //	获取【登录用户】
            String userId = CurrentUserUtil.getCurrentUserId();

            //	判断【登录状态】
            if (userId == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));
            }

            //	获取当前【待办任务】
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(bean.getCurrentTaskId());

            //	校验当前【任务状态】及【审批权限】
            this.checkWorkItem(workItemBean, userId);

            //	判断【待办任务】是否【挂起】
            if (WorkFlowConstants.TASK_STATE_HUNGUP.equals(workItemBean.getTaskState())) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0066", null));
            }

            //	设置实例信息处理类
            ProcessInstanceHandler processInstanceHandler = engineCommonHandler.getProcessInstanceHandler(bean);
            bean.setProcessInstanceHandler(processInstanceHandler);

            // 计算执行路径
            if (bean.getProcessExecPath() == null) {
                // 取得当前节点基本信息
                ActivityBean activityBean = workItemDAO.getBaseActivityBean(bean.getInstanceId(), bean.getCurrentTaskId());
                // 取得当前节点信息
                activityBean = activityDAO.getActivityBean(bean.getInstanceId(), activityBean, false);
                // 设置当前节点迁出线的编码
                activityBean.setChooseLineCode(bean.getLineCode());
                // 取得执行路径
                List<ActivityBean> processExecPath = activityExecutionPathService.getNextExecutionActivityPath(activityBean, bean);
                bean.setProcessExecPath(processExecPath);
            }

            // 设置当前处理节点
            bean = engineCommonHandler.setCurrentActivityHandler(bean);

            // 调度节点完成事件
            ActionEvent actionEvent = new ActionEvent(bean, "doCompleteActivity");
            engineEvent.notifyObserver(actionEvent);

            // 返回信息
            reslut.put("flag", "0");
            reslut.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0067", null));

        } catch (Exception e) {
            throw e;
        }
        return reslut;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void completeTimeoutWorkItem(ProcessInstanceBean bean) throws Exception {
        try {
            // 设置实例信息处理类
            ProcessInstanceHandler processInstanceHandler = engineCommonHandler.getProcessInstanceHandler(bean);
            bean.setProcessInstanceHandler(processInstanceHandler);
            // 设置当前处理节点
            bean = engineCommonHandler.setCurrentActivityHandler(bean);
            // 调度节点完成事件
            ActionEvent actionEvent = new ActionEvent(bean, "doCompleteActivity");
            engineEvent.notifyObserver(actionEvent);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Map<String, Object> doConsult(String taskId, String consultUserId, String comment) throws Exception {

        // 返回参数定义
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 当前登录用户
            String userId = CurrentUserUtil.getCurrentUserId();

            if (userId == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));
            }

            // 查询当前待办
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);

            // 校验当前【任务状态】及【审批权限】
            this.checkWorkItem(workItemBean, userId);

            // 设置待办状态处理类
            workItemBean = engineCommonHandler.setWorkItemStateHandler(workItemBean);

            // 挂起征询人待办
            workItemBean.getWorkItemStateHandler().toHangUp(workItemBean);

            // 将其他同时发送的待办关闭
            String actType = activityDAO.getActivityCategory(workItemBean.getInstanceId(), workItemBean.getActId());

            //判断如果是通知节点
            if (WorkFlowConstants.NOTICE_NODE.equals(actType)) {

                HashMap<String, Object> params = new HashMap<String, Object>();

                params.put("currentInstanceId", workItemBean.getInstanceId());
                params.put("currentActId", workItemBean.getActId());
                params.put("currentState", WorkFlowConstants.TASK_STATE_OPEN);

                params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
                params.put("endDate", CurrentUserUtil.getCurrentDate());

                workItemDAO.modifyWorkItem(params);
            }

            //	创建征询待办
            WorkItemBean consultWorkItem = new WorkItemBean();

            //	从【当前待办】复制信息到【征询待办】
            BeanUtils.copyProperties(workItemBean, consultWorkItem);

            String consultTaskId = UUID.randomUUID().toString();
            consultWorkItem.setTaskId(consultTaskId);                    //	征询待办任务ID
            consultWorkItem.setFromTaskId(workItemBean.getTaskId());    //	来源待办任务ID
            consultWorkItem.setFromUser(workItemBean.getAssignUser());    //	待办发起人
            consultWorkItem.setAssignUser(consultUserId);                //	待办所有者
            consultWorkItem.setExecuteUser(consultUserId);                //	待办执行人
            consultWorkItem.setTaskState(WorkFlowConstants.TASK_STATE_OPEN); // 设置待办状态
            consultWorkItem.setBeginDate(CurrentUserUtil.getCurrentDate());    //	待办开始时间
            //待办标题
            consultWorkItem.setTaskTitle(XipUtil.getMessage("XIP_WF_SERVICE_0061", null) + "：" + workItemBean.getTaskTitle());
            consultWorkItem.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0061", null));    //	处理结果
            consultWorkItem.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0068", null));    //	描述
            consultWorkItem.setCreateType(WorkFlowConstants.TASK_CREATE_CONSULT);    //	待办类型 ： 征询待办
            consultWorkItem.setComment(comment);    //	征询或委派的批注信息
            consultWorkItem.setCreateDt(CurrentUserUtil.getCurrentDate());
            consultWorkItem.setCreateBy(CurrentUserUtil.getCurrentUserId());

            // 保存征询待办信息
            workItemDAO.createWorkItem(consultWorkItem);

            // 待办提醒处理
            //handlerTaskNotice(consultTaskId, consultUserId, null);

            // 返回参数
            result.put("flag", "0");
            result.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0067", null));

        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    public Map<String, Object> revokeConsultTask(String taskId, String comment) throws Exception {
        // 取得征询待办ID
        String consultTaskId = workItemDAO.getConsultTaskId(taskId);
        if (consultTaskId == null) {
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0072", null));
        }
        // 关闭征询待办
        return this.closeConsultTask(consultTaskId, comment, WorkFlowConstants.CONSULT_PROCESS_REVOKE);

    }

    @Override
    public Map<String, Object> closeConsultTask(String taskId, String comment, String processType) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            // 	当前用户和当前时间
            String currentUserId = CurrentUserUtil.getCurrentUserId();
            String empName = CurrentUserUtil.getEmpName();

            // 	查询待办信息
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);

            //	判断【待办任务】是否存在
            if (workItemBean == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0073", null));
            }

            // 	判断【待办任务】是否关闭
            if (WorkFlowConstants.TASK_STATE_CLOSED.equals(workItemBean.getTaskState())) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0073", null));
            }
            //	设置征询反馈意见
            workItemBean.setApproveCommnet(comment);

            //	设置关闭原因：如果
            if (processType.equals(WorkFlowConstants.CONSULT_PROCESS_RESPONSE)) {
                workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0058", null));
                workItemBean.setCloseCause(WorkFlowConstants.TASK_CLOSE_NORMAL);    // 关闭原因： 正常关闭

            } else if (processType.equals(WorkFlowConstants.CONSULT_PROCESS_REVOKE)) {
                workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0060", null));
                workItemBean.setCloseCause(WorkFlowConstants.TASK_CLOSE_REVOKE);    // 关闭原因： 撤消征询
            }

            workItemBean.setEndDate(CurrentUserUtil.getCurrentDate());            // 结束时间
            workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);    // 关闭
            workItemBean.setExecuteUser(currentUserId);                            // 待办执行人
            workItemBean.setUpdateDt(CurrentUserUtil.getCurrentDate());
            workItemBean.setUpdateBy(currentUserId);

            // 关闭被征询人待办
            workItemDAO.modifyWorkItem(workItemBean);


            // =================处理征询人待办信息 begin====================

            // 取得征询人待办信息
            WorkItemBean consultWorkItemBean = workItemDAO.getWorkItemBean(workItemBean.getFromTaskId());
            if (comment != null && !"".equals(comment) && processType.equals(WorkFlowConstants.CONSULT_PROCESS_RESPONSE)) {
                String _comment = empName + XipUtil.getMessage("XIP_WF_SERVICE_0069", null) + ":" + comment;
                if (consultWorkItemBean.getComment() != null && !"".equals(consultWorkItemBean.getComment())) {
                    _comment = _comment.concat(";  ").concat(consultWorkItemBean.getComment());
                }
                consultWorkItemBean.setComment(_comment);
            }

            // 设置待办状态处理类
            consultWorkItemBean = engineCommonHandler.setWorkItemStateHandler(consultWorkItemBean);
            // 打开征询人待办
            consultWorkItemBean.getWorkItemStateHandler().toOpen(consultWorkItemBean);

            // =================处理征询人待办信息 end======================

            // 返回值
            result.put("flag", "0");
            result.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0067", null));

        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    public Map<String, Object> doDelegate(String taskId, String assginUserId, String comment) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();

        try {
            // 	当前登录用户
            String _userId = CurrentUserUtil.getCurrentUserId();

            if (_userId == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));
            }

            //	取得委派人待办信息
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);

            //	校验当前【任务状态】及【审批权限】
            this.checkWorkItem(workItemBean, _userId);

            workItemBean.setExecuteUser(assginUserId);    // 修改待办执行人

            if (comment != null && !"".equals(comment)) {
                String _comment = XipUtil.getMessage("XIP_WF_SERVICE_0069", null) + ":".concat(comment);
                if (workItemBean.getComment() != null && !"".equals(workItemBean.getComment())) {
                    _comment = _comment.concat(";  ").concat(workItemBean.getComment());
                }
                workItemBean.setComment(_comment);
            }

            // 修改【更新时间】和【更新人】
            workItemBean.setUpdateDt(CurrentUserUtil.getCurrentDate());
            workItemBean.setUpdateBy(CurrentUserUtil.getCurrentUserId());

            workItemDAO.modifyWorkItem(workItemBean);

            // 将其他同时发送的待办关闭 added by qp at 16/04/07 start
            String actType = activityDAO.getActivityCategory(workItemBean.getInstanceId(), workItemBean.getActId());
            //判断如果是通知节点
            if (WorkFlowConstants.NOTICE_NODE.equals(actType)) {
                HashMap<String, Object> params = new HashMap<String, Object>();

                params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
                params.put("endDate", new Date());

                params.put("currentInstanceId", workItemBean.getInstanceId());
                params.put("currentActId", workItemBean.getActId());
                params.put("currentState", WorkFlowConstants.TASK_STATE_OPEN);
                params.put("currentTaskId", workItemBean.getTaskId());

                workItemDAO.modifyWorkItem(params);
            }
            // 将其他同时发送的待办关闭 added by qp at 16/04/07 end

            // 待办提醒处理
            //handlerTaskNotice(taskId, assginUserId, null);

            // 返回参数
            result.put("flag", "0");
            result.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0067", null));

        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    public void doForward(String taskId, String[] userArray) throws Exception {

        // 当前待办
        WorkItemBean currentWorkItemBean = workItemDAO.getWorkItemBean(taskId);

        // 待办发起人
        String fromUserId = currentWorkItemBean.getFromUser();
        Date cdate = CurrentUserUtil.getCurrentDate();
        String userId = CurrentUserUtil.getCurrentUserId();

        for (int i = 0; i < userArray.length; i++) {
            String targetUserId = userArray[i];
            if (i == 0) {

                currentWorkItemBean.setAssignUser(targetUserId);
                currentWorkItemBean.setExecuteUser(targetUserId);
                currentWorkItemBean.setUpdateDt(cdate);
                currentWorkItemBean.setUpdateBy(userId);
                currentWorkItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0075", null) + ":" + currentWorkItemBean.getDescription());

                workItemDAO.modifyWorkItem(currentWorkItemBean);
            } else {

                WorkItemBean worItemBean = new WorkItemBean();

                BeanUtils.copyProperties(currentWorkItemBean, worItemBean);

                worItemBean.setTaskId(UUID.randomUUID().toString());
                worItemBean.setFromUser(fromUserId);
                worItemBean.setAssignUser(targetUserId);
                worItemBean.setExecuteUser(targetUserId);
                worItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0075", null) + ":" + worItemBean.getDescription());
                worItemBean.setCreateBy(userId);
                worItemBean.setCreateDt(cdate);

                workItemDAO.createWorkItem(worItemBean);
            }
        }
    }

    @Override
    public Map<String, Object> closeCopyToTask(String taskId, String comment) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        try {
            // 取得当前待办
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);
            if (workItemBean != null && !workItemBean.getTaskState().equals(WorkFlowConstants.TASK_STATE_OPEN)) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
            }

            // 设置待办信息
            workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0057", null));
            workItemBean.setApproveCommnet(comment == null ? comment : XipUtil.getMessage("XIP_WF_SERVICE_0057", null));
            workItemBean.setEndDate(CurrentUserUtil.getCurrentDate());
            workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);
            workItemBean.setCloseCause(WorkFlowConstants.TASK_CLOSE_NORMAL); //  正常关闭

            workItemBean.setUpdateDt(CurrentUserUtil.getCurrentDate());
            workItemBean.setUpdateBy(CurrentUserUtil.getCurrentUserId());

            // 关闭待办
            workItemDAO.modifyWorkItem(workItemBean);

            // 返回信息
            result.put("flag", "0");
            result.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0067", null));
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    public Map<String, Object> doBackProcess(String taskId, String backToTargetActId, String comment) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        return params;
    }

    /**
     * @param instanceBean
     * @param workItemBean
     * @param backToTargetActId
     * @throws Exception 设定文件
     * @Title: doBackLinearProcess
     * @Description: 回退线性流程
     */
    private void doBackLinearProcess(ProcessInstanceBean instanceBean, WorkItemBean workItemBean, String backToTargetActId) throws Exception {
    }

    /**
     * @param instanceBean
     * @param workItemBean
     * @param backToTargetActId
     * @throws Exception 设定文件
     * @Title: doBackLoopProcess
     * @Description: 回退循环流程
     */
    private void doBackLoopProcess(ProcessInstanceBean instanceBean, WorkItemBean workItemBean, String backToTargetActId) throws Exception {
    }

    /*===============	end		待办处理 	end		================*/

    /*===============	start	待办查询 	start 	================*/

    private TransitionBean createTransitionBean(String code, String disable) {
        TransitionBean transitionBean = new TransitionBean();

        String name = null;

        switch (code) {
            case WorkFlowConstants.TASK_BTN_CANCEL:    //	撤销流程
                name = XipUtil.getMessage("XIP_WF_SERVICE_0056", null);
                break;
            case WorkFlowConstants.TASK_BTN_COPYTO:    //	关闭抄送待办
                name = XipUtil.getMessage("XIP_WF_SERVICE_0057", null);
                break;
            case WorkFlowConstants.TASK_BTN_CONSULT_CLOSE:    //	征询反馈
                name = XipUtil.getMessage("XIP_WF_SERVICE_0058", null);
                break;
            case WorkFlowConstants.TASK_BTN_BACK:    // 回退
                name = XipUtil.getMessage("XIP_WF_SERVICE_0059", null);
                break;
            case WorkFlowConstants.TASK_BTN_CONSULT_REVOKE:    // 撤消征询
                name = XipUtil.getMessage("XIP_WF_SERVICE_0060", null);
                break;
            case WorkFlowConstants.TASK_BTN_CONSULT:    // 征询
                name = XipUtil.getMessage("XIP_WF_SERVICE_0061", null);
                break;
            case WorkFlowConstants.TASK_BTN_DELEGATE:    // 委派
                name = XipUtil.getMessage("XIP_WF_SERVICE_0062", null);
                break;
            case WorkFlowConstants.TASK_BTN_FORWARD:    // 转发
                name = XipUtil.getMessage("XIP_WF_SERVICE_0063", null);
                break;
            case WorkFlowConstants.TASK_BTN_LOOP_AGREE:    //
                transitionBean.setCategory("N");
                name = XipUtil.getMessage("XIP_WF_SERVICE_0053", null);
                break;
            case WorkFlowConstants.TASK_BTN_REJECT:    //	驳回
                name = XipUtil.getMessage("XIP_WF_SERVICE_0037", null);
                break;
            case WorkFlowConstants.TASK_BTN_SIGN_REJECT: //	会签驳回
                transitionBean.setCategory("N");
                name = XipUtil.getMessage("XIP_WF_SERVICE_0037", null);
                break;

            default:
                break;
        }

        transitionBean.setId(UUID.randomUUID().toString());
        transitionBean.setCode(code);    // 撤回流程
        transitionBean.setName(name);
        transitionBean.setDisable(disable);    // 是否启用 ：N-启动, Y-不启动
        return transitionBean;
    }

    @Override
    public List<TransitionBean> getButtonsByTaskId(String taskId) throws Exception {
        List<TransitionBean> transList = new ArrayList<TransitionBean>();

        // 取得待办信息
        WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);

        // 当前用户ID
        String currentUserId = CurrentUserUtil.getCurrentUserId();

        if (currentUserId == null) throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));

        // 判断当前用户是否为待办所有者
        boolean isOwner = true;

        if (!currentUserId.equals(workItemBean.getAssignUser())
                && currentUserId.equals(workItemBean.getExecuteUser())) {
            isOwner = false;
        }

        String createType = workItemBean.getCreateType();

        // ========================= 生成按钮信息 ===================

        if (WorkFlowConstants.TASK_CREATE_START.equals(createType) || WorkFlowConstants.TASK_BTN_COPYTO.equals(createType) || WorkFlowConstants.TASK_BTN_CONSULT_CLOSE.equals(createType)) {    //提交人待办

            transList.add(createTransitionBean(createType, "N"));

        } else {
            // 判断当前待办是否发起征询
            boolean isConsult = false;

            if (WorkFlowConstants.TASK_STATE_HUNGUP.equals(workItemBean.getTaskState())) {

                // 如果待办为挂起状态时,判断其是否发起了征询处理
                String consultTaskId = workItemDAO.getConsultTaskId(workItemBean.getTaskId());
                if (consultTaskId != null) isConsult = true;

            }

            // 当前流程实例节点
            ActivityBean actBean = activityDAO.getActivityBean(workItemBean.getInstanceId(), workItemBean.getActId());

            // 节点分类
            String actCategory = actBean.getActType();

            // 查找节点属性配置信息
            List<ActivityPropertyBean> properytBeanList = actBean.getActPropertyBeanList();

            // 判断当前通知节点是否动态节点且仍存在未审批的队列

            boolean isExistOpenQueue = false;

            if (WorkFlowConstants.NOTICE_NODE.equals(actCategory) && "Y".equals(processEngineCommonService.isDynamicNode(actBean))) {

                List<DynaNodeQueueBean> queues = actBean.getQueues();

                if (queues != null && queues.size() > 0) {
                    for (DynaNodeQueueBean queue : queues) {
                        if (WorkFlowConstants.TASK_STATE_OPEN.equals(queue.getApproveStatus())) {
                            isExistOpenQueue = true;
                            break;
                        }
                    }
                }
            }

            // 查询获取节点连线信息，如果当前节点为动态通知节点且仍存在未审批的队列,则无须计算其迁出线信息并动态产生出同意和驳回按钮
            if (isExistOpenQueue) {

                // 同意按钮
                transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_LOOP_AGREE, isConsult ? "Y" : "N"));

                // 驳回按钮

                transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_REJECT, isConsult ? "Y" : "N"));

            } else {

                transList = workItemDAO.getMoveOutTransitions(taskId);

                if (transList != null && transList.size() > 0) {
                    for (int i = 0; i < transList.size(); i++) {
                        TransitionBean tb = transList.get(i);
                        if (isConsult) {        // 如果已发起征询, 则将按钮设置不启用
                            tb.setDisable("Y");
                        } else {
                            tb.setDisable("N");
                        }
                        transList.set(i, tb);
                    }

                    /*
                     * 会签节点特殊处理 :
                     * 如果会签节点只有一条迁出线,则为其生成一条默认的驳回线; 且该驳回线默认指向开始节点。
                     */
                    if (WorkFlowConstants.COUNTERSIGN_NODE.equals(actCategory) && transList.size() == 1) {

                        TransitionBean transBean = createTransitionBean(WorkFlowConstants.TASK_BTN_SIGN_REJECT, isConsult ? "Y" : "N");

                        String btnName = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_REJECT_CST_NAME);

                        if (StringUtils.isNotBlank(btnName)) {
                            transBean.setName(btnName);
                        }

                        transList.add(transBean);
                    }
                }
            }

            // 通知节点: 回退与驳回
            if (!WorkFlowConstants.COUNTERSIGN_NODE.equals(actCategory)) {

                // 是否回退 , Y-是 , N-否
                String allowBackFlag = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_IS_ALLOW_BACK);

                if ("Y".equals(allowBackFlag)) {
                    transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_BACK, isConsult ? "Y" : "N"));
                }

                // 是否驳回 , Y-是, N-否
                if (!isExistOpenQueue) {
                    // 按流程设置属性产生驳回按钮
                    String isRejectFlag = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_IS_REJECT);

                    if ("Y".equals(isRejectFlag)) {

                        TransitionBean transBean = createTransitionBean(WorkFlowConstants.TASK_BTN_REJECT, isConsult ? "Y" : "N");
                        String btnName = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_REJECT_CST_NAME);
                        if (StringUtils.isNotBlank(btnName)) {
                            transBean.setName(btnName);
                        }
                        transList.add(transBean);
                    }
                }
            }

            // 如果当前用户为待办所有者, 则可以发起征询、委派、转发和撤消征询处理
            if (isOwner) {

                // 是否征询 , Y-是 , N-否
                String consultFlag = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_IS_CONSULT);

                if ("Y".equals(consultFlag)) {

                    if (isConsult) {    // 发起征询
                        transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_CONSULT_REVOKE, ""));
                    } else {
                        transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_CONSULT, ""));
                    }

                }

                // 是否委派, Y-是 , N-否
                String delegateFlag = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_IS_DELEGATE);
                if ("Y".equals(delegateFlag) && !isConsult) {
                    transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_DELEGATE, isConsult ? "Y" : "N"));
                }

                //判断是否添加配置可转发
                String forwardFlag = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_IS_FORWARD);

                if ("Y".equals(forwardFlag) && !isConsult) {

                    transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_FORWARD, isConsult ? "Y" : "N"));

                }
            }
        }
        // 返回按钮信息
        return transList;
    }

    @Override
    public Map<String, Object> getForwardExecutors(String taskId) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();

        //获取流程实例对象
        ProcessInstanceBean insBean = processInstanceDAO.getProcessInstanceByTaskId(taskId);

        // 节点ID
        HashMap<String, String> map = workItemDAO.getIdListByTaskId(taskId);
        String actId = map.get("ACTID");

        //取得当前节点
        ActivityBean currentActivityBean = new ActivityBean();
        for (ActivityBean actBean : insBean.getActivityBeanList()) {
            if (actBean.getId().equals(actId)) {
                currentActivityBean = actBean;
                break;
            }
        }

        // 取得节点配置执行人
        List<ActivityExecutorBean> configExecutors = activityConfigExecutorServiceFacade.getActivityConfigExecutorList(currentActivityBean, insBean);
        if (configExecutors != null && configExecutors.size() > 0) {
            currentActivityBean.setActivityConfigExecutorList(configExecutors);
        } else {
            HashMap<String, String> tokens = new HashMap<String, String>();
            tokens.put("code", currentActivityBean.getCode());
            tokens.put("name", currentActivityBean.getName());
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0031", tokens));
        }

        // 查询已经发送待办的人员信息
        HashMap<String, List<String>> hisExecutorsMap = workItemDAO.getHasSendExecutors(insBean.getInstanceId(), actId);
        List<String> assignUsers = hisExecutorsMap.get("assign");
        List<String> executeUsers = hisExecutorsMap.get("execute");

        // 可转发的执行人信息
        List<ActivityExecutorBean> forwardExecutors = new ArrayList<ActivityExecutorBean>();
        for (ActivityExecutorBean bean : configExecutors) {
            if (!assignUsers.contains(bean.getUserId()) && !executeUsers.contains(bean.getUserId())) {
                forwardExecutors.add(bean);
            }
        }

        result.put("flag", "0");
        result.put("msg", "");
        result.put("multiselect", "N");
        result.put("taskId", taskId);
        result.put("actName", currentActivityBean.getName());

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        for (ActivityExecutorBean p : forwardExecutors) {
            Map<String, String> obj = new HashMap<String, String>();

            obj.put("userId", p.getUserId());
            obj.put("actName", currentActivityBean.getName());
            obj.put("userName", p.getUserName());
            obj.put("empName", p.getEmpName());
            obj.put("orgName", p.getOrgName());

            data.add(obj);

        }
        ;

        return result;
    }


    @Override
    public TabPage<WorkItemBean> getUserTaskPage(Map<String, Object> params) throws Exception{
        return PageUtils.selectForPage(params, new PageUtils.SelectCallBack<WorkItemBean>() {
	            @Override
	            public List<WorkItemBean> query(Map<String, Object> params) throws Exception {
	                return workItemDAO.getUserTaskList(params);
	            }
	        }
        );
    }

    @Override
    public List<WorkItemBean> findWorkItemListByInstanceCode(String instanceCode) throws Exception {
        List<WorkItemBean> returnList = new ArrayList<WorkItemBean>();
        ProcessInstanceBean bean = processInstanceDAO.getBaseInstanceBeanByCode(instanceCode);
        if (bean.isQueryArch()) {// 流程已归档
            returnList = workItemDAO.getAllArchWorkItemListByInstanceId(bean.getInstanceId());
        } else {// 流程未归档
            returnList = workItemDAO.getAllRunWorkItemListByInstanceId(bean.getInstanceId());
        }
        return returnList;
    }

    /*===============	end		待办查询 	end 	================*/

}
