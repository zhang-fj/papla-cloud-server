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
 * @Description: 运行时【流程待办】处理服务实现类
 * @date 2021年8月29日 下午1:44:52
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
     * @Description TODO    根据【流程实例】和【当前节点】创建流程待办任务
     */
    private WorkItemBean createWorkItem(ProcessInstanceBean bean, ActivityBean activityBean) throws Exception {

        WorkItemBean workItemBean = new WorkItemBean();

        // 获取当前时间
        Date currentDate = CurrentUserUtil.getCurrentDate();
        workItemBean.setTaskId(UUID.randomUUID().toString());
        workItemBean.setProcessId(bean.getProcessId());                //流程ID
        workItemBean.setDeployId(bean.getDeployId());                //流程部署ID
        workItemBean.setProcessCode(bean.getProcessCode());            //流程编码
        workItemBean.setProcessVersion(bean.getProcessVersion());    //流程版本
        workItemBean.setInstanceId(bean.getInstanceId());            //流程实例ID
        workItemBean.setInstanceCode(bean.getInstanceCode());        //实例编码
        workItemBean.setActId(activityBean.getId());                //节点ID
        workItemBean.setActCode(activityBean.getCode());            //节点编码
        workItemBean.setBeginDate(currentDate);                        //开始时间
        workItemBean.setCreateDt(currentDate);                        //创建时间

        //创建人
        String currentUserId = CurrentUserUtil.getCurrentUserId();
        workItemBean.setCreateBy(currentUserId);

        // 待办发起人
        if ("Y".equals(bean.getIsTimeout())) currentUserId = bean.getCurrentUserId();
        workItemBean.setFromUser(currentUserId);

        // 是否为客户化待办
        String isCustomTask = PropertyHandlerUtil.findActivityPropertyValue(activityBean, WorkFlowConstants.A_IS_CUSTOM_TASK);
        workItemBean.setIsCustomTask("Y".equals(isCustomTask) ? "Y" : "N");

        // 是否为超时待办
        String days = PropertyHandlerUtil.findActivityPropertyValue(activityBean, WorkFlowConstants.A_TIMEOUT_DAYS);
        workItemBean.setIsTimeoutTask(StringUtils.isNotBlank(days) ? "Y" : "N");


        String actName = activityBean.getName();
        String taskTitle = PropertyHandlerUtil.findActivityPropertyValue(activityBean, WorkFlowConstants.A_TITLE);

        DynaNodeQueueBean queueBean = activityBean.getQueueBean();    // 动态审批队列
        if (queueBean != null) {
            actName = queueBean.getNodeName();
            taskTitle = queueBean.getTaskTitle();
        }

        // 设置待办阶段名称
        workItemBean.setActName(actName);
        // 处理待办标题
        workItemBean.setTaskTitle(processEngineCommonService.replaceVariable(taskTitle, bean.getAttrList(), activityBean));

        return workItemBean;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void createSubmitterWorkItem(ProcessInstanceBean bean) throws Exception {

        // 计算提交人
        String currentUserId = CurrentUserUtil.getCurrentUserId();
        String submitter = CurrentUserUtil.getCurrentUserId();
        String submitterName = null;
        String empName = workFlowUserDAO.getEmpNameByUserId(currentUserId);

        // 取得当前处理节点
        AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
        ActivityBean act = activityHandler.getActivityBean();

        // 封装提交人待办信息
        WorkItemBean workItemBean = createWorkItem(bean, act);

        // 流程驳回时, 设置待办执行人
        if (bean.isReject()) {
            // 取得流程实例的提交人
            List<InstanceEntityAttrBean> properytBeanList = bean.getAttrList();
            submitter = PropertyHandlerUtil.findEntityPropertyValue(properytBeanList, WorkFlowConstants.E_SUBMITTER);
            submitterName = workFlowUserDAO.getEmpNameByUserId(submitter);
            workItemBean.setTaskTitle("请" + submitterName + "完成由" + empName + "驳回的" + bean.getInstanceTitle() + "流程的提交处理");     // 标题
        } else {
            workItemBean.setTaskTitle("请" + empName + "完成" + bean.getInstanceTitle() + "流程的提交处理");     // 标题
        }

        workItemBean.setFromUser(currentUserId);    // 待办发起人
        workItemBean.setAssignUser(submitter);        // 待办所有者
        workItemBean.setExecuteUser(submitter);        // 待办执行人
        workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_OPEN);    // 待办状态
        workItemBean.setCreateType(WorkFlowConstants.TASK_CREATE_START);    // 待办类型
        workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0027", null));  // 处理结果
        workItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0049", null)); // 流程描述

        try {
            // 创建待办信息
            workItemDAO.createWorkItem(workItemBean);
            // 把当前待办加入实例Bean
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
            // 当前处理节点
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();

            String isNotice = PropertyHandlerUtil.findPropertyValue(activityBean.getPropertyMap(), WorkFlowConstants.A_IS_NOTICE); // 是否提醒
            String submitterName = PropertyHandlerUtil.findEntityPropertyValue(bean.getAttrList(), WorkFlowConstants.E_SUBMITTER_NAME);
            String submitter = PropertyHandlerUtil.findEntityPropertyValue(bean.getAttrList(), WorkFlowConstants.E_SUBMITTER);

            String currentUserId = CurrentUserUtil.getCurrentUserId();
            Date creationDate = CurrentUserUtil.getCurrentDate();

            WorkItemBean workItemBean = createWorkItem(bean, activityBean);

            // 查询审批历史
            String content = workItemDAO.getApproveHisTasks(bean.getInstanceId());

            // 创建通知
            HashMap<String, Object> nMap = new HashMap<String, Object>();
            nMap.put("id", workItemBean.getTaskId());
            nMap.put("titlte", workItemBean.getTaskTitle());
            nMap.put("content", content);
            nMap.put("srcId", bean.getInstanceCode().concat("%").concat(activityBean.getId()));
            nMap.put("srcCat", "XIP_WF_".concat(activityBean.getName()));
            nMap.put("createdBy", workItemBean.getCreateBy());
            nMap.put("creationDate", workItemBean.getCreateDt());

            workItemDAO.createNotice(nMap);

            // 将通知发送至执行人
            for (ActivityExecutorBean executor : excutors) {

                HashMap<String, Object> unMap = new HashMap<String, Object>();
                unMap.put("userNoticeId", UUID.randomUUID().toString());
                unMap.put("noticeId", workItemBean.getTaskId());
                unMap.put("userId", executor.getUserId());
                unMap.put("status", PlatformConstants.NOTICE_STATUS_OPEN);
                unMap.put("createdBy", currentUserId);
                unMap.put("creationDate", creationDate);
                workItemDAO.createUserNotice(unMap);

                workItemBean.setAssignUser(executor.getUserId());        // 待办所有人
                workItemBean.setExecuteUser(executor.getUserId());        // 待办执行人
                workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);
                workItemBean.setEndDate(creationDate);
                workItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0050", null));
                workItemBean.setCreateType(WorkFlowConstants.TASK_CREATE_COPYTO);    // 待办类型

                workItemDAO.createWorkItem(workItemBean);

                // 如果当前节点需要提醒，则创建提醒信息
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

            // 当前处理节点
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();

            // 取得会签规则
            String actType = activityBean.getActType();
            String rule = null;

            if (WorkFlowConstants.COUNTERSIGN_NODE.equals(actType)) {
                rule = (String) activityBean.getPropertyMap().get(WorkFlowConstants.A_COUNTERSIGN_RULE);
            }

            // 创建待办任务
            WorkItemBean workItemBean = createWorkItem(bean, activityBean);

            // 封装待办信息
            for (ActivityExecutorBean executor : excutors) {

                workItemBean.setTaskId(UUID.randomUUID().toString());            //待办任务ID
                workItemBean.setAssignUser(executor.getUserId());                    // 待办所有人
                workItemBean.setExecuteUser(executor.getUserId());                    // 待办执行人
                workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);        // 待办状态
                workItemBean.setCloseCause(WorkFlowConstants.TASK_CLOSE_AVOID);    // 关闭原因:发送规避后关闭
                workItemBean.setCreateType(WorkFlowConstants.TASK_CREATE_AVOID);    // 待办类型:规避待办
                workItemBean.setEndDate(workItemBean.getBeginDate());                // 结束时间
                workItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0051", null));
                workItemBean.setResult(XipUtil.getMessage("XIP_WF_SERVICE_0053", null));

                if (WorkFlowConstants.JOIN_ONE_PASS.equals(rule) || WorkFlowConstants.JOIN_FIRST_PASS.equals(rule)) {
                    // 【一票通过 或抢单】 : 发送待办中只要存在一条待办审批通过, 则节点审批通过
                    int idx = excutors.indexOf(executor);
                    //  为第一条待办添加审批意见
                    if (idx == 0) workItemBean.setApproveCommnet(XipUtil.getMessage("XIP_WF_SERVICE_0053", null));
                } else {
                    workItemBean.setApproveCommnet(XipUtil.getMessage("XIP_WF_SERVICE_0053", null));// 审批意见
                }
                // 生成规避待办信息
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

            // 当前处理节点
            AbstractActivityHandler activityHandler = bean.getCurrentActivityHandler();
            ActivityBean activityBean = activityHandler.getActivityBean();

            String isNotice = PropertyHandlerUtil.findPropertyValue(activityBean.getPropertyMap(), WorkFlowConstants.A_IS_NOTICE); // 是否提醒
            String submitterName = PropertyHandlerUtil.findEntityPropertyValue(bean.getAttrList(), WorkFlowConstants.E_SUBMITTER_NAME);
            String submitter = PropertyHandlerUtil.findEntityPropertyValue(bean.getAttrList(), WorkFlowConstants.E_SUBMITTER);

            Date currentDate = CurrentUserUtil.getCurrentDate();

            // 取得假期规则
            HashMap<String, HolidayRuleBean> ruleMap = workItemDAO.getHolidayRule(executors, CurrentUserUtil.getCurrentYearAndMonth(currentDate));

            // 封装待办信息
            for (ActivityExecutorBean executor : executors) {

                WorkItemBean workItemBean = createWorkItem(bean, activityBean);

                // 待办所有人
                workItemBean.setAssignUser(executor.getUserId());
                workItemBean.setExecuteUser(executor.getUserId());    // 待办执行人

                // 获取授权执行人信息
                List<String> proxyUsers = new ArrayList<String>();//this.getProxyUsers(bean, activityBean, executor) ;
                if (proxyUsers.size() > 1) {
                    throw new Exception("指定待办执行人出错 : 【" + workItemBean.getActName() + "】节点的审批人【" + executor.getEmpName() + "】存在多个代理审批人，请检查审批授权信息!");
                }
                ;

                if (proxyUsers.size() == 1) {
                    // 授权时则全部更改待办所有者和待办执行人
                    workItemBean.setAssignUser(proxyUsers.get(0));
                    workItemBean.setExecuteUser(proxyUsers.get(0));
                } else {
                    if (ruleMap != null) { // 按假期规则处理执行人
                        HolidayRuleBean rule = ruleMap.get(executor.getUserId());
                        if (rule != null) {
                            String proxyUserId = rule.getProxyUserId();
                            workItemBean.setExecuteUser(proxyUserId == null ? executor.getUserId() : proxyUserId);    // 代理用户
                            workItemBean.setAuthSrc("XIP_WF_USER_HOLIDAY");        // 假期规则
                            workItemBean.setAuthSrcId(rule.getPrivilegeId());    // 授权ID
                            workItemBean.setTaskOwner(executor.getUserId());    // 待办所有者

                            // 如果当前节点需要提醒，则创建提醒信息
                            if ("Y".equals(isNotice)) {
                                taskNoticeList.add(
                                        new TaskNoticeBean(workItemBean.getTaskId(), proxyUserId, workItemBean.getTaskTitle(), submitter, submitterName)
                                );
                            }

                        }
                    } else { //按普通处理方式处理执行人
                        workItemBean.setExecuteUser(executor.getUserId());
                    }
                }

                //	如果当前节点需要提醒，则创建提醒信息
                if ("Y".equals(isNotice)) {
                    taskNoticeList.add(
                            new TaskNoticeBean(
                                    workItemBean.getTaskId(), workItemBean.getExecuteUser(), workItemBean.getTaskTitle(), submitter, submitterName)
                    );
                }

                workItemBean.setTaskState(WorkFlowConstants.TASK_STATE_OPEN);        // 待办状态
                workItemBean.setCreateType(WorkFlowConstants.TASK_CREATE_NORMAL);    // 待办类型:正常待办
                workItemBean.setDescription(XipUtil.getMessage("XIP_WF_SERVICE_0052", null)); //

                // 生成待办信息
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
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();
            // 撤回待办信息
            workItemDAO.closeCancelTask(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public void doRejectWorkItem(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();

            // 当前待办ID
            String currentTaskId = bean.getCurrentTaskId();

            // 取得当前待办
            WorkItemBean currentWorkItem = null;
            List<WorkItemBean> workItemList = bean.getWorkItemList();
            for (WorkItemBean itemBean : workItemList) {
                if (itemBean.getTaskId().equals(currentTaskId)) {
                    currentWorkItem = itemBean;
                    break;
                }
            }

            // 当期审批人
            String currentUserId = CurrentUserUtil.getCurrentUserId();

            // 判断当前用户登录状态
            if (currentUserId == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0055", null));
            }

            // 判断当前待办是否已经关闭
            if (currentWorkItem != null && WorkFlowConstants.TASK_STATE_CLOSED.equals(currentWorkItem.getTaskState())) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
            }

            // 判断当前审批权限是否被收回
            if (!currentWorkItem.getAssignUser().equals(currentUserId) && !currentWorkItem.getExecuteUser().equals(currentUserId)) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0065", null));
            }

            // 取得当前节点
            ActivityBean currentActBean = null;
            List<ActivityBean> acts = bean.getActivityBeanList();
            for (ActivityBean actBean : acts) {
                if (actBean.getId().equals(currentWorkItem.getActId())) {
                    currentActBean = actBean;
                    // 设置当前节点
                    bean.setCurrentActivityHandler(engineCommonHandler.setCurrentActivityHandler(actBean).getCurrentActivityHandler());
                    break;
                }
            }

            /* ============================================
             * 关闭当前节点待办信息
             * ===========================================*/

            List<ActivityPropertyBean> properytBeanList = currentActBean.getActPropertyBeanList();

            // 取得按钮自定义名称
            String result = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_REJECT_CST_NAME);

            // 驳回时, 设置实例业务状态信息
            String bizStatus = PropertyHandlerUtil.findPropertyValue(properytBeanList, WorkFlowConstants.A_BIZ_REJECT_STATUS);
            bean.setCurrentBizState(bizStatus);

            // 关闭当前待办
            currentWorkItem.setExecuteUser(currentUserId);
            currentWorkItem.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);    // 关闭
            currentWorkItem.setEndDate(CurrentUserUtil.getCurrentDate());
            currentWorkItem.setResult(result);
            currentWorkItem.setApproveCommnet(bean.getApproverComment());
            currentWorkItem.setCloseCause(WorkFlowConstants.TASK_CLOSE_REJECT);    // 驳回关闭
            currentWorkItem.setUpdateDt(CurrentUserUtil.getCurrentDate());
            currentWorkItem.setUpdateBy(currentUserId);
            currentWorkItem.setClosedLineType(bean.getLineCategory());

            this.closeTask(currentWorkItem);


            // 关闭非当前的OPEN待办
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("currentInstanceId", bean.getInstanceId());
            params.put("currentActId", currentActBean.getId());
            params.put("currentState", WorkFlowConstants.TASK_STATE_OPEN);

            params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
            params.put("endDate", CurrentUserUtil.getCurrentDate());
            params.put("closeCause", WorkFlowConstants.TASK_CLOSE_REJECT);    // 驳回关闭
            params.put("result", result);
            params.put("updateBy", currentUserId);

            workItemDAO.modifyWorkItem(params);


            /* ============================================
             * 驳回时, 失效待办信息
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
                    || actType.equals(WorkFlowConstants.NOTICE_NODE)) {    // 处理会签节点的超时待办

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("currentInstanceId", bean.getInstanceId());
                params.put("currentActId", bean.getCurrentActivityHandler().getActivityBean().getId());
                params.put("currentState", WorkFlowConstants.TASK_STATE_OPEN);

                params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
                params.put("endDate", CurrentUserUtil.getCurrentDate());
                params.put("result", bean.getCurrentResult());
                params.put("closeCause", WorkFlowConstants.TASK_CLOSE_TIMEOUT);
                params.put("updateBy", bean.getCurrentUserId());

                // 关闭超时待办
                workItemDAO.modifyWorkItem(params);

            }

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void closeCountersignTask(String insId, String actId, String currentTaskId) throws Exception {
        try {
            // 封装参数

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("currentInstanceId", insId);
            params.put("currentActId", actId);
            params.put("currentTaskId", currentTaskId);
            params.put("currentState", WorkFlowConstants.TASK_STATE_OPEN);

            params.put("endDate", CurrentUserUtil.getCurrentDate());
            params.put("taskState", WorkFlowConstants.TASK_STATE_CLOSED);
            params.put("closeCause", WorkFlowConstants.TASK_CLOSE_SING);    // 关闭原因：符合会签规则 , 待办非审批关闭

            // 关闭会签待办信息
            workItemDAO.modifyWorkItem(params);
        } catch (Exception e) {
            throw e;
        }
    }

}
