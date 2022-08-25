package com.papla.cloud.workflow.engine.run.engine.core.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papla.cloud.workflow.engine.common.api.ExpressionService;
import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.*;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;
import com.papla.cloud.workflow.engine.run.dao.ProcessInstanceDAO;
import com.papla.cloud.workflow.engine.run.dao.TransitionDAO;
import com.papla.cloud.workflow.engine.run.engine.core.ProcessEngine;
import com.papla.cloud.workflow.engine.run.engine.core.common.EngineCommonHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.EngineEvent;
import com.papla.cloud.workflow.engine.run.engine.core.task.WorkItemService;
import com.papla.cloud.workflow.util.AppContext;
import com.papla.cloud.workflow.util.PlatformConstants;
import com.papla.cloud.workflow.util.PlatformUtil;
import com.papla.cloud.workflow.util.XipUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: AbstractActivityHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity
 * @Description: 节点运行方法标准抽象类
 * @date 2021年8月29日 上午11:46:34
 */
public abstract class AbstractActivityHandler {

    // 日志记录器
    public Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    // 节点状态处理类
    protected ActivityStateHandler activityStateHandler;

    // 节点Bean
    protected ActivityBean activityBean;

    public ActivityBean getActivityBean() {
        return activityBean;
    }

    public void setActivityBean(ActivityBean activityBean) {
        this.activityBean = activityBean;
    }

    public ActivityStateHandler getActivityStateHandler() {
        return activityStateHandler;
    }

    public void setActivityStateHandler(ActivityStateHandler activityStateHandler) {
        this.activityStateHandler = activityStateHandler;
    }


    // ====================== 取得容器中对象信息 begin =======================

    /**
     * @return ActivityDAO    返回类型
     * @Title: getActivityDAO
     * @Description: 节点DAO处理类
     */
    public ActivityDAO getActivityDAO() {
        return (ActivityDAO) AppContext.getApplicationContext().getBean("activityDAO");
    }

    /**
     * @return TransitionDAO    返回类型
     * @Title: getTransitionDAO
     * @Description: 连线DAO处理类
     */
    public TransitionDAO getTransitionDAO() {
        return (TransitionDAO) AppContext.getApplicationContext().getBean("transitionDAO");
    }

    /**
     * @return ProcessEngineCommonService    返回类型
     * @Title: getProcessEngineCommonService
     * @Description: 引擎公共服务类
     */
    public ProcessEngineCommonService getProcessEngineCommonService() {
        return (ProcessEngineCommonService) AppContext.getApplicationContext().getBean("processEngineCommonService");
    }

    /**
     * @return EngineCommonHandler    返回类型
     * @Title: getEngineCommonHandler
     * @Description: 引擎通用事件处理类
     */
    public EngineCommonHandler getEngineCommonHandler() {
        return (EngineCommonHandler) AppContext.getApplicationContext().getBean("engineCommonHandler");
    }

    /**
     * @return EngineEvent    返回类型
     * @Title: getEngineEvent
     * @Description: 引擎事件处理类
     */
    public EngineEvent getEngineEvent() {
        return (EngineEvent) AppContext.getApplicationContext().getBean("engineEvent");
    }

    /**
     * @return WorkItemService    返回类型
     * @Title: getWorkItemService
     * @Description: 待办处理服务类
     */
    public WorkItemService getWorkItemService() {
        return (WorkItemService) AppContext.getApplicationContext().getBean("workItemService");
    }

    /**
     * @return ProcessInstanceDAO    返回类型
     * @Title: getProcessInstanceDAO
     * @Description: 获取实例DAO处理类
     */
    public ProcessInstanceDAO getProcessInstanceDAO() {
        return (ProcessInstanceDAO) AppContext.getApplicationContext().getBean("processInstanceDAO");
    }

    /**
     * @return ExpressionService    返回类型
     * @Title: getExpressionService
     * @Description: 表达式解析器
     */
    public ExpressionService getExpressionService() {
        return (ExpressionService) AppContext.getApplicationContext().getBean("expressionService");
    }

    /**
     * @return ProcessEngine    返回类型
     * @Title: getProcessEngine
     * @Description: 获取流程引擎实例
     */
    public ProcessEngine getProcessEngine() {
        return (ProcessEngine) AppContext.getApplicationContext().getBean("processEngine");
    }


    // ============= 取得容器中对象信息 end ========================

    /* ==================================	start	节点运行标准方法	start	===================================== */

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title doStart
     * @Description TODO    启动流程节点
     */
    public abstract void doStart(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title doRun
     * @Description TODO    运行流程节点
     */
    public abstract void doRun(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title doCancel
     * @Description TODO    撤消流程节点
     */
    public void doCancel(ProcessInstanceBean bean) throws Exception {
    }

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title doTerminate
     * @Description TODO    终止流程节点
     */
    public abstract void doTerminate(ProcessInstanceBean bean) throws Exception;

    /**
     * @param bean
     * @return void
     * @throws Exception
     * @Title doComplete    完成流程节点
     * @Description TODO
     */
    public abstract void doComplete(ProcessInstanceBean bean) throws Exception;


    /* ==================================	end	节点运行标准方法	end	===================================== */


    /**
     * @param bean
     * @return ProcessInstanceBean    返回类型
     * @throws Exception
     * @Title: setExcePath
     * @Description: 设置流程执行路径
     */
    public ProcessInstanceBean setExcePath(ProcessInstanceBean bean) throws Exception {
        try {

            // 执行路径
            List<ActivityBean> processExecPath = bean.getProcessExecPath();

            // 历史执行路径
            List<ActivityBean> oldProcessExecPath = bean.getOldProcessExcePath();
            if (oldProcessExecPath == null) {
                oldProcessExecPath = new ArrayList<ActivityBean>();
            }

            oldProcessExecPath.add(processExecPath.get(0));

            // 删除执行路径的第一个节点
            processExecPath.remove(0);

            bean.setOldProcessExcePath(oldProcessExecPath);
            bean.setProcessExecPath(processExecPath);

        } catch (Exception e) {
            throw e;
        }
        return bean;
    }

    /**
     * @param bean
     * @param funcType
     * @param funcName
     * @return void    返回类型
     * @throws Exception
     * @Title: processActivityFunc
     * @Description: 处理节点函数
     */
    public void processActivityFunc(ProcessInstanceBean bean, ActivityBean activityBean, String funcType, String funcName) throws Exception {
        try {
            String realFuncType = PropertyHandlerUtil.findPropertyValue(activityBean.getPropertyMap(), funcType);
            String realFuncName = PropertyHandlerUtil.findPropertyValue(activityBean.getPropertyMap(), funcName);
            if (StringUtils.isNotBlank(realFuncType) && StringUtils.isNotBlank(realFuncName)) {
                this.invokeFunc(bean, funcType, funcName);
            }
        } catch (Exception e) {
            String actName = activityBean.getName();
            HashMap<String, String> tokens = new HashMap<String, String>();
            tokens.put("actName", actName);
            String msg = XipUtil.getMessage("XIP_WF_SERVICE_0023", tokens) + e.getMessage();
            throw new Exception(msg);
        }
    }

    /**
     * @param bean
     * @param transBean
     * @return void    返回类型
     * @throws Exception
     * @Title: processTransition
     * @Description: 处理节点连线函数
     */
    public void processTransition(ProcessInstanceBean bean, TransitionBean transBean) throws Exception {
        try {
            // 记录连线状态
            transBean.setInstanceId(bean.getInstanceId());                // 实例Id
            transBean.setState(WorkFlowConstants.TRANS_STATE_PASS);        // 连线状态:Y
            transBean.setCreateDt(CurrentUserUtil.getCurrentDate());
            transBean.setCreateBy(CurrentUserUtil.getCurrentUserId() == null ? bean.getCurrentUserId() : CurrentUserUtil.getCurrentUserId());

            this.getTransitionDAO().insertTransitionState(transBean);

            // 调用连线上的函数
            String funcType = transBean.getFuncType();                    // 函数类型
            String funcName = transBean.getFuncName();                    // 函数名称
            if (funcName != null && !"".equals(funcName)) {                // 调用函数
                try {
                    this.invokeFunc(bean, funcType, funcName);
                } catch (Exception e) {
                    HashMap<String, String> tokens = new HashMap<String, String>();
                    tokens.put("code", transBean.getCode());
                    tokens.put("name", transBean.getName());
                    String msg = XipUtil.getMessage("XIP_WF_SERVICE_0024", tokens) + e.getMessage();
                    throw new Exception(msg);
                }
            }

            // 更新流程实例对应的业务状态信息
            if (transBean.getBizStatus() != null && !"".equals(transBean.getBizStatus()) && !"null".equals(transBean.getBizStatus())) {
                bean.setCurrentBizState(transBean.getBizStatus());        // 当前业务状态
                this.getProcessInstanceDAO().updateBizState(bean);        // 执行更新处理
            }

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param bean
     * @param funcType
     * @param funcName
     * @return void    返回类型
     * @throws Exception
     * @Title: invokeFunc
     * @Description: 调用函数
     */
    private void invokeFunc(ProcessInstanceBean bean, String funcType, String funcValue) throws Exception {
        try {
            getProcessEngineCommonService().invokeFunc(bean, funcType, funcValue);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param bean
     * @return WorkItemBean    返回类型
     * @throws Exception
     * @Title: getNewWorkItemBean
     * @Description: 获取新待办信息
     */
    public WorkItemBean getNewWorkItemBean(ProcessInstanceBean bean, String lineCategory) throws Exception {
        // 取得当前待办
        WorkItemBean newWorkItemBean = new WorkItemBean();
        try {
            String currentTaskId = bean.getCurrentTaskId();
            List<WorkItemBean> workItemList = bean.getWorkItemList();
            for (WorkItemBean itemBean : workItemList) {
                if (currentTaskId.equals(itemBean.getTaskId())) {
                    newWorkItemBean = itemBean;
                    break;
                }
            }
            // 设置当前待办
            newWorkItemBean = this.setNewWorkItemBean(newWorkItemBean, bean);
            newWorkItemBean.setClosedLineType(lineCategory);

        } catch (Exception e) {
            throw e;
        }
        return newWorkItemBean;
    }

    /**
     * @param itemBean
     * @param bean
     * @return WorkItemBean    返回类型
     * @throws Exception
     * @Title: setNewWorkItemBean
     * @Description: 设置待办信息
     */
    public WorkItemBean setNewWorkItemBean(WorkItemBean itemBean, ProcessInstanceBean bean) throws Exception {
        try {
            itemBean.setTaskState(WorkFlowConstants.TASK_STATE_CLOSED);    // 关闭待办
            itemBean.setCloseCause(WorkFlowConstants.TASK_CLOSE_NORMAL);    // 关闭原因

            // 会签超时待办处理20170224
            String currentUserId = (CurrentUserUtil.getCurrentUserId() == null ? bean.getCurrentUserId() : CurrentUserUtil.getCurrentUserId());

            itemBean.setResult(bean.getCurrentResult());
            itemBean.setApproveCommnet(bean.getApproverComment() == null ? bean.getCurrentResult() : bean.getApproverComment());

            itemBean.setEndDate(CurrentUserUtil.getCurrentDate());
            itemBean.setExecuteUser(currentUserId);

            itemBean.setUpdateDt(CurrentUserUtil.getCurrentDate());
            itemBean.setUpdateBy(currentUserId);
        } catch (Exception e) {
            throw e;
        }
        return itemBean;
    }

    public void handlerTaskNotice(List<TaskNoticeBean> notices) {
        // 启动线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 执行提醒
                    handlerTaskNotices(notices);
                } catch (Exception e) {
                    log.error("notice failure: " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * @return void    返回类型
     * @throws Exception
     * @Title: handlerTaskNotice
     * @Description: 执行待办提醒处理
     */
    public void handlerTaskNotices(List<TaskNoticeBean> notices) throws Exception {
        try {
        } catch (Exception e) {
            log.error("Send waiting task failure: " + e.getMessage());
        }
    }

    /**
     * @param insAttrs
     * @param executors
     * @throws Exception
     * @Title: setCurrentNodeApprovers
     * @Description: 设置当前节点的审批人信息（当前停留节点的所有待办的所有者）
     */
    public void setCurrentNodeApprovers(List<InstanceEntityAttrBean> insAttrs, List<ActivityExecutorBean> executors) throws Exception {
        if (executors != null && executors.size() > 0) {
            StringBuffer userIds = new StringBuffer();
            StringBuffer empNames = new StringBuffer();
            // 当前审批人信息
            for (int i = 0; i < executors.size(); i++) {
                ActivityExecutorBean executor = executors.get(i);
                if (i == executors.size() - 1) {
                    userIds.append(executor.getUserId());
                    empNames.append(executor.getEmpName());
                } else {
                    userIds.append(executor.getUserId()).append(";");
                    empNames.append(executor.getEmpName()).append(";");
                }
            }
            // 记录当前节点审批人信息
            if (insAttrs != null && insAttrs.size() > 0) {
                for (InstanceEntityAttrBean attr : insAttrs) {
                    // // 记录当前节点审批人名称
                    if (WorkFlowConstants.E_CURRENT_NODE_APPROVER.equals(attr.getAttrCode())) {
                        attr.setAttrResult(empNames.toString());
                    }
                    // 记录当前节点审批人ID
                    if (WorkFlowConstants.E_CURRENT_NODE_APPROVER_ID.equals(attr.getAttrCode())) {
                        attr.setAttrResult(userIds.toString());
                    }
                }
            }
        }
    }

    /**
     * @param queueBean
     * @throws Exception 设定文件
     * @Title: modifyDynaNodeStatus
     * @Description: 修改通知节点的动态审批队列状态
     */
    public void modifyDynaNodeStatus(DynaNodeQueueBean queueBean, String newStatus) throws Exception {
        this.getActivityDAO().updDynaActQueueStatus(queueBean, newStatus);
    }

}
