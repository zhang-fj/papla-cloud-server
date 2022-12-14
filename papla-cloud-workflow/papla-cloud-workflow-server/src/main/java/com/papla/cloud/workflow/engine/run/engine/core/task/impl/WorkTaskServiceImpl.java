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
 * @Description: ?????????????????????????????????
 * @date 2021???8???29??? ??????1:44:52
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
     * @Description TODO    ????????????????????????
     */
    private void checkWorkItem(WorkItemBean workItemBean, String userId) throws Exception {

        //	????????????????????????????????????
        if (workItemBean == null) {
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
        }

        //	??????????????????????????????
        String taskState = workItemBean.getTaskState();

        //	??????????????????????????????????????????
        if (WorkFlowConstants.TASK_STATE_CLOSED.equals(taskState)) {
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
        }

        //	??????????????????????????????????????????
        if (!workItemBean.getAssignUser().equals(userId) && !workItemBean.getExecuteUser().equals(userId)) {
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0065", null));
        }

    }

    /*===============	start	??????????????????	start	===============*/

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Map<String, Object> completeWorkItem(ProcessInstanceBean bean) throws Exception {

        Map<String, Object> reslut = new HashMap<String, Object>();

        try {

            //	????????????????????????
            String userId = CurrentUserUtil.getCurrentUserId();

            //	????????????????????????
            if (userId == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));
            }

            //	??????????????????????????????
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(bean.getCurrentTaskId());

            //	???????????????????????????????????????????????????
            this.checkWorkItem(workItemBean, userId);

            //	??????????????????????????????????????????
            if (WorkFlowConstants.TASK_STATE_HUNGUP.equals(workItemBean.getTaskState())) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0066", null));
            }

            //	???????????????????????????
            ProcessInstanceHandler processInstanceHandler = engineCommonHandler.getProcessInstanceHandler(bean);
            bean.setProcessInstanceHandler(processInstanceHandler);

            // ??????????????????
            if (bean.getProcessExecPath() == null) {
                // ??????????????????????????????
                ActivityBean activityBean = workItemDAO.getBaseActivityBean(bean.getInstanceId(), bean.getCurrentTaskId());
                // ????????????????????????
                activityBean = activityDAO.getActivityBean(bean.getInstanceId(), activityBean, false);
                // ????????????????????????????????????
                activityBean.setChooseLineCode(bean.getLineCode());
                // ??????????????????
                List<ActivityBean> processExecPath = activityExecutionPathService.getNextExecutionActivityPath(activityBean, bean);
                bean.setProcessExecPath(processExecPath);
            }

            // ????????????????????????
            bean = engineCommonHandler.setCurrentActivityHandler(bean);

            // ????????????????????????
            ActionEvent actionEvent = new ActionEvent(bean, "doCompleteActivity");
            engineEvent.notifyObserver(actionEvent);

            // ????????????
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
            // ???????????????????????????
            ProcessInstanceHandler processInstanceHandler = engineCommonHandler.getProcessInstanceHandler(bean);
            bean.setProcessInstanceHandler(processInstanceHandler);
            // ????????????????????????
            bean = engineCommonHandler.setCurrentActivityHandler(bean);
            // ????????????????????????
            ActionEvent actionEvent = new ActionEvent(bean, "doCompleteActivity");
            engineEvent.notifyObserver(actionEvent);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Map<String, Object> doConsult(String taskId, String consultUserId, String comment) throws Exception {

        // ??????????????????
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // ??????????????????
            String userId = CurrentUserUtil.getCurrentUserId();

            if (userId == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));
            }

            // ??????????????????
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);

            // ???????????????????????????????????????????????????
            this.checkWorkItem(workItemBean, userId);

            // ???????????????????????????
            workItemBean = engineCommonHandler.setWorkItemStateHandler(workItemBean);

            // ?????????????????????
            workItemBean.getWorkItemStateHandler().toHangUp(workItemBean);

            // ????????????????????????????????????
            String actType = activityDAO.getActivityCategory(workItemBean.getInstanceId(), workItemBean.getActId());

            //???????????????????????????
            if (WorkFlowConstants.NOTICE_NODE.equals(actType)) {

                HashMap<String, Object> params = new HashMap<String, Object>();

                params.put("currentInstanceId", workItemBean.getInstanceId());
                params.put("currentActId", workItemBean.getActId());
                params.put("currentState", WorkFlowConstants.TASK_STATE_OPEN);

                params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
                params.put("endDate", CurrentUserUtil.getCurrentDate());

                workItemDAO.modifyWorkItem(params);
            }

            //	??????????????????
            WorkItemBean consultWorkItem = new WorkItemBean();

            //	??????????????????????????????????????????????????????
            BeanUtils.copyProperties(workItemBean, consultWorkItem);

            String consultTaskId = UUID.randomUUID().toString();
            consultWorkItem.setTaskId(consultTaskId);                    //	??????????????????ID
            consultWorkItem.setFromTaskId(workItemBean.getTaskId());    //	??????????????????ID
            consultWorkItem.setFromUser(workItemBean.getAssignUser());    //	???????????????
            consultWorkItem.setAssignUser(consultUserId);                //	???????????????
            consultWorkItem.setExecuteUser(consultUserId);                //	???????????????
            consultWorkItem.setTaskState(WorkFlowConstants.TASK_STATE_OPEN); // ??????????????????
            consultWorkItem.setBeginDate(CurrentUserUtil.getCurrentDate());    //	??????????????????
            //????????????
            consultWorkItem.setTaskTitle(XipUtil.getMessage("XIP_WF_SERVICE_0061", null) + "???" + workItemBean.getTaskTitle());
            consultWorkItem.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0061", null));    //	????????????
            consultWorkItem.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0068", null));    //	??????
            consultWorkItem.setCreateType(WorkFlowConstants.TASK_CREATE_CONSULT);    //	???????????? ??? ????????????
            consultWorkItem.setComment(comment);    //	??????????????????????????????
            consultWorkItem.setCreateDt(CurrentUserUtil.getCurrentDate());
            consultWorkItem.setCreateBy(CurrentUserUtil.getCurrentUserId());

            // ????????????????????????
            workItemDAO.createWorkItem(consultWorkItem);

            // ??????????????????
            //handlerTaskNotice(consultTaskId, consultUserId, null);

            // ????????????
            result.put("flag", "0");
            result.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0067", null));

        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    public Map<String, Object> revokeConsultTask(String taskId, String comment) throws Exception {
        // ??????????????????ID
        String consultTaskId = workItemDAO.getConsultTaskId(taskId);
        if (consultTaskId == null) {
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0072", null));
        }
        // ??????????????????
        return this.closeConsultTask(consultTaskId, comment, WorkFlowConstants.CONSULT_PROCESS_REVOKE);

    }

    @Override
    public Map<String, Object> closeConsultTask(String taskId, String comment, String processType) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        try {

            // 	???????????????????????????
            String currentUserId = CurrentUserUtil.getCurrentUserId();
            String empName = CurrentUserUtil.getEmpName();

            // 	??????????????????
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);

            //	????????????????????????????????????
            if (workItemBean == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0073", null));
            }

            // 	????????????????????????????????????
            if (WorkFlowConstants.TASK_STATE_CLOSED.equals(workItemBean.getTaskState())) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0073", null));
            }
            //	????????????????????????
            workItemBean.setApproveCommnet(comment);

            //	???????????????????????????
            if (processType.equals(WorkFlowConstants.CONSULT_PROCESS_RESPONSE)) {
                workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0058", null));
                workItemBean.setCloseCause(WorkFlowConstants.TASK_CLOSE_NORMAL);    // ??????????????? ????????????

            } else if (processType.equals(WorkFlowConstants.CONSULT_PROCESS_REVOKE)) {
                workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0060", null));
                workItemBean.setCloseCause(WorkFlowConstants.TASK_CLOSE_REVOKE);    // ??????????????? ????????????
            }

            workItemBean.setEndDate(CurrentUserUtil.getCurrentDate());            // ????????????
            workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);    // ??????
            workItemBean.setExecuteUser(currentUserId);                            // ???????????????
            workItemBean.setUpdateDt(CurrentUserUtil.getCurrentDate());
            workItemBean.setUpdateBy(currentUserId);

            // ????????????????????????
            workItemDAO.modifyWorkItem(workItemBean);


            // =================??????????????????????????? begin====================

            // ???????????????????????????
            WorkItemBean consultWorkItemBean = workItemDAO.getWorkItemBean(workItemBean.getFromTaskId());
            if (comment != null && !"".equals(comment) && processType.equals(WorkFlowConstants.CONSULT_PROCESS_RESPONSE)) {
                String _comment = empName + XipUtil.getMessage("XIP_WF_SERVICE_0069", null) + ":" + comment;
                if (consultWorkItemBean.getComment() != null && !"".equals(consultWorkItemBean.getComment())) {
                    _comment = _comment.concat(";  ").concat(consultWorkItemBean.getComment());
                }
                consultWorkItemBean.setComment(_comment);
            }

            // ???????????????????????????
            consultWorkItemBean = engineCommonHandler.setWorkItemStateHandler(consultWorkItemBean);
            // ?????????????????????
            consultWorkItemBean.getWorkItemStateHandler().toOpen(consultWorkItemBean);

            // =================??????????????????????????? end======================

            // ?????????
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
            // 	??????????????????
            String _userId = CurrentUserUtil.getCurrentUserId();

            if (_userId == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));
            }

            //	???????????????????????????
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);

            //	???????????????????????????????????????????????????
            this.checkWorkItem(workItemBean, _userId);

            workItemBean.setExecuteUser(assginUserId);    // ?????????????????????

            if (comment != null && !"".equals(comment)) {
                String _comment = XipUtil.getMessage("XIP_WF_SERVICE_0069", null) + ":".concat(comment);
                if (workItemBean.getComment() != null && !"".equals(workItemBean.getComment())) {
                    _comment = _comment.concat(";  ").concat(workItemBean.getComment());
                }
                workItemBean.setComment(_comment);
            }

            // ??????????????????????????????????????????
            workItemBean.setUpdateDt(CurrentUserUtil.getCurrentDate());
            workItemBean.setUpdateBy(CurrentUserUtil.getCurrentUserId());

            workItemDAO.modifyWorkItem(workItemBean);

            // ???????????????????????????????????? added by qp at 16/04/07 start
            String actType = activityDAO.getActivityCategory(workItemBean.getInstanceId(), workItemBean.getActId());
            //???????????????????????????
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
            // ???????????????????????????????????? added by qp at 16/04/07 end

            // ??????????????????
            //handlerTaskNotice(taskId, assginUserId, null);

            // ????????????
            result.put("flag", "0");
            result.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0067", null));

        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Override
    public void doForward(String taskId, String[] userArray) throws Exception {

        // ????????????
        WorkItemBean currentWorkItemBean = workItemDAO.getWorkItemBean(taskId);

        // ???????????????
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
            // ??????????????????
            WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);
            if (workItemBean != null && !workItemBean.getTaskState().equals(WorkFlowConstants.TASK_STATE_OPEN)) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
            }

            // ??????????????????
            workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0057", null));
            workItemBean.setApproveCommnet(comment == null ? comment : XipUtil.getMessage("XIP_WF_SERVICE_0057", null));
            workItemBean.setEndDate(CurrentUserUtil.getCurrentDate());
            workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);
            workItemBean.setCloseCause(WorkFlowConstants.TASK_CLOSE_NORMAL); //  ????????????

            workItemBean.setUpdateDt(CurrentUserUtil.getCurrentDate());
            workItemBean.setUpdateBy(CurrentUserUtil.getCurrentUserId());

            // ????????????
            workItemDAO.modifyWorkItem(workItemBean);

            // ????????????
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
     * @throws Exception ????????????
     * @Title: doBackLinearProcess
     * @Description: ??????????????????
     */
    private void doBackLinearProcess(ProcessInstanceBean instanceBean, WorkItemBean workItemBean, String backToTargetActId) throws Exception {
    }

    /**
     * @param instanceBean
     * @param workItemBean
     * @param backToTargetActId
     * @throws Exception ????????????
     * @Title: doBackLoopProcess
     * @Description: ??????????????????
     */
    private void doBackLoopProcess(ProcessInstanceBean instanceBean, WorkItemBean workItemBean, String backToTargetActId) throws Exception {
    }

    /*===============	end		???????????? 	end		================*/

    /*===============	start	???????????? 	start 	================*/

    private TransitionBean createTransitionBean(String code, String disable) {
        TransitionBean transitionBean = new TransitionBean();

        String name = null;

        switch (code) {
            case WorkFlowConstants.TASK_BTN_CANCEL:    //	????????????
                name = XipUtil.getMessage("XIP_WF_SERVICE_0056", null);
                break;
            case WorkFlowConstants.TASK_BTN_COPYTO:    //	??????????????????
                name = XipUtil.getMessage("XIP_WF_SERVICE_0057", null);
                break;
            case WorkFlowConstants.TASK_BTN_CONSULT_CLOSE:    //	????????????
                name = XipUtil.getMessage("XIP_WF_SERVICE_0058", null);
                break;
            case WorkFlowConstants.TASK_BTN_BACK:    // ??????
                name = XipUtil.getMessage("XIP_WF_SERVICE_0059", null);
                break;
            case WorkFlowConstants.TASK_BTN_CONSULT_REVOKE:    // ????????????
                name = XipUtil.getMessage("XIP_WF_SERVICE_0060", null);
                break;
            case WorkFlowConstants.TASK_BTN_CONSULT:    // ??????
                name = XipUtil.getMessage("XIP_WF_SERVICE_0061", null);
                break;
            case WorkFlowConstants.TASK_BTN_DELEGATE:    // ??????
                name = XipUtil.getMessage("XIP_WF_SERVICE_0062", null);
                break;
            case WorkFlowConstants.TASK_BTN_FORWARD:    // ??????
                name = XipUtil.getMessage("XIP_WF_SERVICE_0063", null);
                break;
            case WorkFlowConstants.TASK_BTN_LOOP_AGREE:    //
                transitionBean.setCategory("N");
                name = XipUtil.getMessage("XIP_WF_SERVICE_0053", null);
                break;
            case WorkFlowConstants.TASK_BTN_REJECT:    //	??????
                name = XipUtil.getMessage("XIP_WF_SERVICE_0037", null);
                break;
            case WorkFlowConstants.TASK_BTN_SIGN_REJECT: //	????????????
                transitionBean.setCategory("N");
                name = XipUtil.getMessage("XIP_WF_SERVICE_0037", null);
                break;

            default:
                break;
        }

        transitionBean.setId(UUID.randomUUID().toString());
        transitionBean.setCode(code);    // ????????????
        transitionBean.setName(name);
        transitionBean.setDisable(disable);    // ???????????? ???N-??????, Y-?????????
        return transitionBean;
    }

    @Override
    public List<TransitionBean> getButtonsByTaskId(String taskId) throws Exception {
        List<TransitionBean> transList = new ArrayList<TransitionBean>();

        // ??????????????????
        WorkItemBean workItemBean = workItemDAO.getWorkItemBean(taskId);

        // ????????????ID
        String currentUserId = CurrentUserUtil.getCurrentUserId();

        if (currentUserId == null) throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));

        // ??????????????????????????????????????????
        boolean isOwner = true;

        if (!currentUserId.equals(workItemBean.getAssignUser())
                && currentUserId.equals(workItemBean.getExecuteUser())) {
            isOwner = false;
        }

        String createType = workItemBean.getCreateType();

        // ========================= ?????????????????? ===================

        if (WorkFlowConstants.TASK_CREATE_START.equals(createType) || WorkFlowConstants.TASK_BTN_COPYTO.equals(createType) || WorkFlowConstants.TASK_BTN_CONSULT_CLOSE.equals(createType)) {    //???????????????

            transList.add(createTransitionBean(createType, "N"));

        } else {
            // ????????????????????????????????????
            boolean isConsult = false;

            if (WorkFlowConstants.TASK_STATE_HUNGUP.equals(workItemBean.getTaskState())) {

                // ??????????????????????????????,????????????????????????????????????
                String consultTaskId = workItemDAO.getConsultTaskId(workItemBean.getTaskId());
                if (consultTaskId != null) isConsult = true;

            }

            // ????????????????????????
            ActivityBean actBean = activityDAO.getActivityBean(workItemBean.getInstanceId(), workItemBean.getActId());

            // ????????????
            String actCategory = actBean.getActType();

            // ??????????????????????????????
            List<ActivityPropertyBean> properytBeanList = actBean.getActPropertyBeanList();

            // ????????????????????????????????????????????????????????????????????????

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

            // ??????????????????????????????????????????????????????????????????????????????????????????????????????,????????????????????????????????????????????????????????????????????????
            if (isExistOpenQueue) {

                // ????????????
                transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_LOOP_AGREE, isConsult ? "Y" : "N"));

                // ????????????

                transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_REJECT, isConsult ? "Y" : "N"));

            } else {

                transList = workItemDAO.getMoveOutTransitions(taskId);

                if (transList != null && transList.size() > 0) {
                    for (int i = 0; i < transList.size(); i++) {
                        TransitionBean tb = transList.get(i);
                        if (isConsult) {        // ?????????????????????, ???????????????????????????
                            tb.setDisable("Y");
                        } else {
                            tb.setDisable("N");
                        }
                        transList.set(i, tb);
                    }

                    /*
                     * ???????????????????????? :
                     * ???????????????????????????????????????,???????????????????????????????????????; ??????????????????????????????????????????
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

            // ????????????: ???????????????
            if (!WorkFlowConstants.COUNTERSIGN_NODE.equals(actCategory)) {

                // ???????????? , Y-??? , N-???
                String allowBackFlag = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_IS_ALLOW_BACK);

                if ("Y".equals(allowBackFlag)) {
                    transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_BACK, isConsult ? "Y" : "N"));
                }

                // ???????????? , Y-???, N-???
                if (!isExistOpenQueue) {
                    // ???????????????????????????????????????
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

            // ????????????????????????????????????, ????????????????????????????????????????????????????????????
            if (isOwner) {

                // ???????????? , Y-??? , N-???
                String consultFlag = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_IS_CONSULT);

                if ("Y".equals(consultFlag)) {

                    if (isConsult) {    // ????????????
                        transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_CONSULT_REVOKE, ""));
                    } else {
                        transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_CONSULT, ""));
                    }

                }

                // ????????????, Y-??? , N-???
                String delegateFlag = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_IS_DELEGATE);
                if ("Y".equals(delegateFlag) && !isConsult) {
                    transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_DELEGATE, isConsult ? "Y" : "N"));
                }

                //?????????????????????????????????
                String forwardFlag = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_IS_FORWARD);

                if ("Y".equals(forwardFlag) && !isConsult) {

                    transList.add(createTransitionBean(WorkFlowConstants.TASK_BTN_FORWARD, isConsult ? "Y" : "N"));

                }
            }
        }
        // ??????????????????
        return transList;
    }

    @Override
    public Map<String, Object> getForwardExecutors(String taskId) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();

        //????????????????????????
        ProcessInstanceBean insBean = processInstanceDAO.getProcessInstanceByTaskId(taskId);

        // ??????ID
        HashMap<String, String> map = workItemDAO.getIdListByTaskId(taskId);
        String actId = map.get("ACTID");

        //??????????????????
        ActivityBean currentActivityBean = new ActivityBean();
        for (ActivityBean actBean : insBean.getActivityBeanList()) {
            if (actBean.getId().equals(actId)) {
                currentActivityBean = actBean;
                break;
            }
        }

        // ???????????????????????????
        List<ActivityExecutorBean> configExecutors = activityConfigExecutorServiceFacade.getActivityConfigExecutorList(currentActivityBean, insBean);
        if (configExecutors != null && configExecutors.size() > 0) {
            currentActivityBean.setActivityConfigExecutorList(configExecutors);
        } else {
            HashMap<String, String> tokens = new HashMap<String, String>();
            tokens.put("code", currentActivityBean.getCode());
            tokens.put("name", currentActivityBean.getName());
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0031", tokens));
        }

        // ???????????????????????????????????????
        HashMap<String, List<String>> hisExecutorsMap = workItemDAO.getHasSendExecutors(insBean.getInstanceId(), actId);
        List<String> assignUsers = hisExecutorsMap.get("assign");
        List<String> executeUsers = hisExecutorsMap.get("execute");

        // ???????????????????????????
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
        if (bean.isQueryArch()) {// ???????????????
            returnList = workItemDAO.getAllArchWorkItemListByInstanceId(bean.getInstanceId());
        } else {// ???????????????
            returnList = workItemDAO.getAllRunWorkItemListByInstanceId(bean.getInstanceId());
        }
        return returnList;
    }

    /*===============	end		???????????? 	end 	================*/

}
