package com.papla.cloud.workflow.engine.run.engine.core.task.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.papla.cloud.workflow.util.PlatformConstants;
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
import com.papla.cloud.workflow.engine.modal.HolidayRuleBean;
import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TaskNoticeBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.dao.WorkItemDAO;
import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.common.EngineCommonHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;
import com.papla.cloud.workflow.engine.run.engine.core.controller.EngineEvent;
import com.papla.cloud.workflow.engine.run.engine.core.controller.EngineEventObserver;
import com.papla.cloud.workflow.engine.run.engine.core.task.WorkItemService;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: WorkItemServiceImpl.java
 * @Package com.papla.cloud.wf.run.engine.core.task.impl
 * @Description: ????????????????????????????????????????????????
 * @date 2021???8???29??? ??????1:44:52
 */
@Service("workItemService")
public class WorkItemServiceImpl extends EngineEventObserver implements WorkItemService {

    @Resource
    private EngineCommonHandler engineCommonHandler;
    @Resource
    private ProcessEngineCommonService processEngineCommonService;
    @Resource
    private WorkItemDAO workItemDAO;
    @Resource
    private WorkFlowUserDAO workFlowUserDAO;

    /**
     * @param bean
     * @param activityBean
     * @return WorkItemBean
     * @throws Exception
     * @Title createWorkItem
     * @Description TODO    ?????????????????????????????????????????????????????????????????????
     */
    private WorkItemBean createWorkItem(ProcessInstanceBean bean, ActivityBean activityBean) throws Exception {

        WorkItemBean workItemBean = new WorkItemBean();

        // ??????????????????
        Date currentDate = CurrentUserUtil.getCurrentDate();
        workItemBean.setTaskId(UUID.randomUUID().toString());
        workItemBean.setProcessId(bean.getProcessId());                //??????ID
        workItemBean.setDeployId(bean.getDeployId());                //????????????ID
        workItemBean.setProcessCode(bean.getProcessCode());            //????????????
        workItemBean.setProcessVersion(bean.getProcessVersion());    //????????????
        workItemBean.setInstanceId(bean.getInstanceId());            //????????????ID
        workItemBean.setInstanceCode(bean.getInstanceCode());        //????????????
        workItemBean.setActId(activityBean.getId());                //??????ID
        workItemBean.setActCode(activityBean.getCode());            //????????????
        workItemBean.setBeginDate(currentDate);                        //????????????
        workItemBean.setCreateDt(currentDate);                        //????????????

        //?????????
        String currentUserId = CurrentUserUtil.getCurrentUserId();
        workItemBean.setCreateBy(currentUserId);

        // ???????????????
        if ("Y".equals(bean.getIsTimeout())) currentUserId = bean.getCurrentUserId();
        workItemBean.setFromUser(currentUserId);

        // ????????????????????????
        String isCustomTask = PropertyHandlerUtil.findActivityPropertyValue(activityBean, WorkFlowConstants.A_IS_CUSTOM_TASK);
        workItemBean.setIsCustomTask("Y".equals(isCustomTask) ? "Y" : "N");

        // ?????????????????????
        String days = PropertyHandlerUtil.findActivityPropertyValue(activityBean, WorkFlowConstants.A_TIMEOUT_DAYS);
        workItemBean.setIsTimeoutTask(StringUtils.isNotBlank(days) ? "Y" : "N");


        String actName = activityBean.getName();
        String taskTitle = PropertyHandlerUtil.findActivityPropertyValue(activityBean, WorkFlowConstants.A_TITLE);

        DynaNodeQueueBean queueBean = activityBean.getQueueBean();    // ??????????????????
        if (queueBean != null) {
            actName = queueBean.getNodeName();
            taskTitle = queueBean.getTaskTitle();
        }

        // ????????????????????????
        workItemBean.setActName(actName);
        // ??????????????????
        workItemBean.setTaskTitle(processEngineCommonService.replaceVariable(taskTitle, bean.getAttrList(), activityBean));

        return workItemBean;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void createSubmitterWorkItem(ProcessInstanceBean bean) throws Exception {

        // ???????????????
        String currentUserId = CurrentUserUtil.getCurrentUserId();
        String submitter = CurrentUserUtil.getCurrentUserId();
        String submitterName = null;
        String empName = workFlowUserDAO.getEmpNameByUserId(currentUserId);

        // ????????????????????????
        AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
        ActivityBean act = activityHandler.getActivityBean();

        // ???????????????????????????
        WorkItemBean workItemBean = createWorkItem(bean, act);

        // ???????????????, ?????????????????????
        if (bean.isReject()) {
            // ??????????????????????????????
            List<InstanceEntityAttrBean> properytBeanList = bean.getAttrList();
            submitter = PropertyHandlerUtil.findEntityPropertyValue(properytBeanList, WorkFlowConstants.E_SUBMITTER);
            submitterName = workFlowUserDAO.getEmpNameByUserId(submitter);
            workItemBean.setTaskTitle("???" + submitterName + "?????????" + empName + "?????????" + bean.getInstanceTitle() + "?????????????????????");     // ??????
        } else {
            workItemBean.setTaskTitle("???" + empName + "??????" + bean.getInstanceTitle() + "?????????????????????");     // ??????
        }

        workItemBean.setFromUser(currentUserId);    // ???????????????
        workItemBean.setAssignUser(submitter);        // ???????????????
        workItemBean.setExecuteUser(submitter);        // ???????????????
        workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_OPEN);    // ????????????
        workItemBean.setCreateType(WorkFlowConstants.TASK_CREATE_START);    // ????????????
        workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0027", null));  // ????????????
        workItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0049", null)); // ????????????

        try {
            // ??????????????????
            workItemDAO.createWorkItem(workItemBean);
            // ???????????????????????????Bean
            List<WorkItemBean> workItemList = bean.getWorkItemList();
            if (workItemList == null) {
                workItemList = new ArrayList<WorkItemBean>();
            }
            workItemList.add(workItemBean);
            bean.setWorkItemList(workItemList);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }

    }

    ;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public List<TaskNoticeBean> createCopyToWorkItem(ProcessInstanceBean bean, List<ActivityExecutorBean> excutors) throws Exception {

        List<TaskNoticeBean> taskNoticeList = new ArrayList<TaskNoticeBean>();

        try {
            // ??????????????????
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();

            String isNotice = PropertyHandlerUtil.findPropertyValue(activityBean.getPropertyMap(), WorkFlowConstants.A_IS_NOTICE); // ????????????
            String submitterName = PropertyHandlerUtil.findEntityPropertyValue(bean.getAttrList(), WorkFlowConstants.E_SUBMITTER_NAME);
            String submitter = PropertyHandlerUtil.findEntityPropertyValue(bean.getAttrList(), WorkFlowConstants.E_SUBMITTER);

            String currentUserId = CurrentUserUtil.getCurrentUserId();
            Date creationDate = CurrentUserUtil.getCurrentDate();

            WorkItemBean workItemBean = createWorkItem(bean, activityBean);

            // ??????????????????
            String content = workItemDAO.getApproveHisTasks(bean.getInstanceId());

            // ????????????
            HashMap<String, Object> nMap = new HashMap<String, Object>();
            nMap.put("id", workItemBean.getTaskId());
            nMap.put("titlte", workItemBean.getTaskTitle());
            nMap.put("content", content);
            nMap.put("srcId", bean.getInstanceCode().concat("%").concat(activityBean.getId()));
            nMap.put("srcCat", "XIP_WF_".concat(activityBean.getName()));
            nMap.put("createdBy", workItemBean.getCreateBy());
            nMap.put("creationDate", workItemBean.getCreateDt());

            workItemDAO.createNotice(nMap);

            // ???????????????????????????
            for (ActivityExecutorBean executor : excutors) {

                HashMap<String, Object> unMap = new HashMap<String, Object>();
                unMap.put("userNoticeId", UUID.randomUUID().toString());
                unMap.put("noticeId", workItemBean.getTaskId());
                unMap.put("userId", executor.getUserId());
                unMap.put("status", PlatformConstants.NOTICE_STATUS_OPEN);
                unMap.put("createdBy", currentUserId);
                unMap.put("creationDate", creationDate);
                workItemDAO.createUserNotice(unMap);

                workItemBean.setAssignUser(executor.getUserId());        // ???????????????
                workItemBean.setExecuteUser(executor.getUserId());        // ???????????????
                workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);
                workItemBean.setEndDate(creationDate);
                workItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0050", null));
                workItemBean.setCreateType(WorkFlowConstants.TASK_CREATE_COPYTO);    // ????????????

                workItemDAO.createWorkItem(workItemBean);

                // ??????????????????????????????????????????????????????
                if ("Y".equals(isNotice)) {
                    taskNoticeList.add(
                            new TaskNoticeBean(workItemBean.getTaskId(), workItemBean.getExecuteUser(), workItemBean.getTaskTitle(), submitter, submitterName)
                    );
                }

            }
        } catch (Exception e) {
            throw e;
        }
        return taskNoticeList;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void createAvoidWorkItem(ProcessInstanceBean bean, List<ActivityExecutorBean> excutors) throws Exception {
        try {

            // ??????????????????
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();

            // ??????????????????
            String actType = activityBean.getActType();
            String rule = null;

            if (WorkFlowConstants.COUNTERSIGN_NODE.equals(actType)) {
                rule = (String) activityBean.getPropertyMap().get(WorkFlowConstants.A_COUNTERSIGN_RULE);
            }

            // ??????????????????
            WorkItemBean workItemBean = createWorkItem(bean, activityBean);

            // ??????????????????
            for (ActivityExecutorBean executor : excutors) {

                workItemBean.setTaskId(UUID.randomUUID().toString());            //????????????ID
                workItemBean.setAssignUser(executor.getUserId());                    // ???????????????
                workItemBean.setExecuteUser(executor.getUserId());                    // ???????????????
                workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);        // ????????????
                workItemBean.setCloseCause(WorkFlowConstants.TASK_CLOSE_AVOID);    // ????????????:?????????????????????
                workItemBean.setCreateType(WorkFlowConstants.TASK_CREATE_AVOID);    // ????????????:????????????
                workItemBean.setEndDate(workItemBean.getBeginDate());                // ????????????
                workItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0051", null));
                workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0053", null));

                if (WorkFlowConstants.JOIN_ONE_PASS.equals(rule) || WorkFlowConstants.JOIN_FIRST_PASS.equals(rule)) {
                    // ??????????????? ???????????? : ???????????????????????????????????????????????????, ?????????????????????
                    int idx = excutors.indexOf(executor);
                    //  ????????????????????????????????????
                    if (idx == 0) workItemBean.setApproveCommnet(XipUtil.getMessage("XIP_WF_SERVICE_0053", null));
                } else {
                    workItemBean.setApproveCommnet(XipUtil.getMessage("XIP_WF_SERVICE_0053", null));// ????????????
                }
                // ????????????????????????
                workItemDAO.createWorkItem(workItemBean);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public List<TaskNoticeBean> createNormalWorkItem(ProcessInstanceBean bean, List<ActivityExecutorBean> executors) throws Exception {

        List<TaskNoticeBean> taskNoticeList = new ArrayList<TaskNoticeBean>();

        try {

            // ??????????????????
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();

            String isNotice = PropertyHandlerUtil.findPropertyValue(activityBean.getPropertyMap(), WorkFlowConstants.A_IS_NOTICE); // ????????????
            String submitterName = PropertyHandlerUtil.findEntityPropertyValue(bean.getAttrList(), WorkFlowConstants.E_SUBMITTER_NAME);
            String submitter = PropertyHandlerUtil.findEntityPropertyValue(bean.getAttrList(), WorkFlowConstants.E_SUBMITTER);

            Date currentDate = CurrentUserUtil.getCurrentDate();

            // ??????????????????
            HashMap<String, HolidayRuleBean> ruleMap = workItemDAO.getHolidayRule(executors, CurrentUserUtil.getCurrentYearAndMonth(currentDate));

            // ??????????????????
            for (ActivityExecutorBean executor : executors) {

                WorkItemBean workItemBean = createWorkItem(bean, activityBean);

                // ???????????????
                workItemBean.setAssignUser(executor.getUserId());
                workItemBean.setExecuteUser(executor.getUserId());    // ???????????????

                // ???????????????????????????
                List<String> proxyUsers = new ArrayList<String>();//this.getProxyUsers(bean, activityBean, executor) ;
                if (proxyUsers.size() > 1) {
                    throw new Exception("??????????????????????????? : ???" + workItemBean.getActName() + "????????????????????????" + executor.getEmpName() + "????????????????????????????????????????????????????????????!");
                }
                ;

                if (proxyUsers.size() == 1) {
                    // ?????????????????????????????????????????????????????????
                    workItemBean.setAssignUser(proxyUsers.get(0));
                    workItemBean.setExecuteUser(proxyUsers.get(0));
                } else {
                    if (ruleMap != null) { // ??????????????????????????????
                        HolidayRuleBean rule = ruleMap.get(executor.getUserId());
                        if (rule != null) {
                            String proxyUserId = rule.getProxyUserId();
                            workItemBean.setExecuteUser(proxyUserId == null ? executor.getUserId() : proxyUserId);    // ????????????
                            workItemBean.setAuthSrc("XIP_WF_USER_HOLIDAY");        // ????????????
                            workItemBean.setAuthSrcId(rule.getPrivilegeId());    // ??????ID
                            workItemBean.setTaskOwner(executor.getUserId());    // ???????????????

                            // ??????????????????????????????????????????????????????
                            if ("Y".equals(isNotice)) {
                                taskNoticeList.add(
                                        new TaskNoticeBean(workItemBean.getTaskId(), proxyUserId, workItemBean.getTaskTitle(), submitter, submitterName)
                                );
                            }

                        }
                    } else { //????????????????????????????????????
                        workItemBean.setExecuteUser(executor.getUserId());
                    }
                }

                //	??????????????????????????????????????????????????????
                if ("Y".equals(isNotice)) {
                    taskNoticeList.add(
                            new TaskNoticeBean(
                                    workItemBean.getTaskId(), workItemBean.getExecuteUser(), workItemBean.getTaskTitle(), submitter, submitterName)
                    );
                }

                workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_OPEN);        // ????????????
                workItemBean.setCreateType(WorkFlowConstants.TASK_CREATE_NORMAL);    // ????????????:????????????
                workItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0052", null)); //

                // ??????????????????
                workItemDAO.createWorkItem(workItemBean);
            }
        } catch (Exception e) {
            throw e;
        }

        return taskNoticeList;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public void doCancelWorkItem(ActionEvent actionEvent) throws Exception {
        try {
            // ????????????Bean
            ProcessInstanceBean bean = actionEvent.getBean();
            // ??????????????????
            workItemDAO.closeCancelTask(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public void doRejectWorkItem(ActionEvent actionEvent) throws Exception {
        try {
            // ????????????Bean
            ProcessInstanceBean bean = actionEvent.getBean();

            // ????????????ID
            String currentTaskId = bean.getCurrentTaskId();

            // ??????????????????
            WorkItemBean currentWorkItem = null;
            List<WorkItemBean> workItemList = bean.getWorkItemList();
            for (WorkItemBean itemBean : workItemList) {
                if (itemBean.getTaskId().equals(currentTaskId)) {
                    currentWorkItem = itemBean;
                    break;
                }
            }

            // ???????????????
            String currentUserId = CurrentUserUtil.getCurrentUserId();

            // ??????????????????????????????
            if (currentUserId == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));
            }

            // ????????????????????????????????????
            if (currentWorkItem != null && WorkFlowConstants.TASK_STATE_CLOSED.equals(currentWorkItem.getTaskState())) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
            }

            // ???????????????????????????????????????
            if (!currentWorkItem.getAssignUser().equals(currentUserId) && !currentWorkItem.getExecuteUser().equals(currentUserId)) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0065", null));
            }

            // ??????????????????
            ActivityBean currentActBean = null;
            List<ActivityBean> acts = bean.getActivityBeanList();
            for (ActivityBean actBean : acts) {
                if (actBean.getId().equals(currentWorkItem.getActId())) {
                    currentActBean = actBean;
                    // ??????????????????
                    bean.setCurrentActivityHandler(engineCommonHandler.setCurrentActivityHandler(actBean).getCurrentActivityHandler());
                    break;
                }
            }

            /* ============================================
             * ??????????????????????????????
             * ===========================================*/

            List<ActivityPropertyBean> properytBeanList = currentActBean.getActPropertyBeanList();

            // ???????????????????????????
            String result = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_REJECT_CST_NAME);

            // ?????????, ??????????????????????????????
            String bizStatus = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_BIZ_REJECT_STATUS);
            bean.setCurrentBizState(bizStatus);

            // ??????????????????
            currentWorkItem.setExecuteUser(currentUserId);
            currentWorkItem.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);    // ??????
            currentWorkItem.setEndDate(CurrentUserUtil.getCurrentDate());
            currentWorkItem.setResult(result);
            currentWorkItem.setApproveCommnet(bean.getApproverComment());
            currentWorkItem.setCloseCause(WorkFlowConstants.TASK_CLOSE_REJECT);    // ????????????
            currentWorkItem.setUpdateDt(CurrentUserUtil.getCurrentDate());
            currentWorkItem.setUpdateBy(currentUserId);
            currentWorkItem.setClosedLineType(bean.getLineCategory());

            this.closeTask(currentWorkItem);


            // ??????????????????OPEN??????
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("currentInstanceId", bean.getInstanceId());
            params.put("currentActId", currentActBean.getId());
            params.put("currentState", WorkFlowConstants.TASK_STATE_OPEN);

            params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
            params.put("endDate", CurrentUserUtil.getCurrentDate());
            params.put("closeCause", WorkFlowConstants.TASK_CLOSE_REJECT);    // ????????????
            params.put("result", result);
            params.put("updateBy", currentUserId);

            workItemDAO.modifyWorkItem(params);


            /* ============================================
             * ?????????, ??????????????????
             * ===========================================*/
            workItemDAO.disableTask(bean.getInstanceId());

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void closeTask(WorkItemBean workItemBean) throws Exception {
        try {
            workItemDAO.modifyWorkItem(workItemBean);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void closeTimeoutTask(ProcessInstanceBean bean, String actType) throws Exception {
        try {

            if (actType.equals(WorkFlowConstants.COUNTERSIGN_NODE)
                    || actType.equals(WorkFlowConstants.NOTICE_NODE)) {    // ?????????????????????????????????

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("currentInstanceId", bean.getInstanceId());
                params.put("currentActId", bean.getCurrentActivityHandler().getActivityBean().getId());
                params.put("currentState", WorkFlowConstants.TASK_STATE_OPEN);

                params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
                params.put("endDate", CurrentUserUtil.getCurrentDate());
                params.put("result", bean.getCurrentResult());
                params.put("closeCause", WorkFlowConstants.TASK_CLOSE_TIMEOUT);
                params.put("updateBy", bean.getCurrentUserId());

                // ??????????????????
                workItemDAO.modifyWorkItem(params);

            }

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void closeCountersignTask(String insId, String actId, String currentTaskId) throws Exception {
        try {
            // ????????????

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("currentInstanceId", insId);
            params.put("currentActId", actId);
            params.put("currentTaskId", currentTaskId);
            params.put("currentState", WorkFlowConstants.TASK_STATE_OPEN);

            params.put("endDate", CurrentUserUtil.getCurrentDate());
            params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
            params.put("closeCause", WorkFlowConstants.TASK_CLOSE_SING);    // ????????????????????????????????? , ?????????????????????

            // ????????????????????????
            workItemDAO.modifyWorkItem(params);
        } catch (Exception e) {
            throw e;
        }
    }

}
