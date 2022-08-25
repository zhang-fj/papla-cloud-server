package com.papla.cloud.workflow.engine.run.engine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.StartActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.common.EngineCommonHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;
import com.papla.cloud.workflow.engine.run.engine.core.controller.EngineEvent;
import com.papla.cloud.workflow.engine.run.engine.core.handler.ActivityExecutionPathService;
import com.papla.cloud.workflow.engine.run.engine.core.instance.ProcessInstanceHandler;
import com.papla.cloud.workflow.util.XipUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.dao.WorkItemDAO;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessEngineImpl.java
 * @Package com.papla.cloud.wf.run.engine.core
 * @Description: 流程引擎接口实现类
 * @date 2021年8月25日 下午4:11:22
 */
@Service("processEngine")
public class ProcessEngineImpl implements ProcessEngine {

    @Autowired
    private EngineEvent engineEvent;
    @Autowired
    private EngineCommonHandler engineCommonHandler;
    @Autowired
    private ActivityExecutionPathService activityExecutionPathService;
    @Autowired
    private WorkItemDAO workItemDAO;

    // 日志记录器
    private Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Map<String, String> startProcess(ProcessInstanceBean bean) throws Exception {

        Map<String, String> result = new HashMap<String, String>();

        try {

            ActionEvent actionEvent = new ActionEvent(bean, "doStartProcess");
            engineEvent.notifyObserver(actionEvent);

            // 封装创建实例成功的参数信息
            result.put("flag", "0");
            result.put("instanceCode", bean.getInstanceCode());

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
        return result;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Map<String, String> restartInstance(ProcessInstanceBean bean) throws Exception {

        Map<String, String> result = new HashMap<String, String>();

        try {
            /*====================================================
             * 如果流程实例状态为【running / canceled】状态时, 则可以执行重启流程实例
             * 适用业务场景为：
             * 	1、撤回流程后, 须重新启动流程实例
             * 	2、驳回流程后, 须重新启动流程实例
             *  3、驳回和重启并行运行
             * ==================================================*/
            if (WorkFlowConstants.INS_CANCELED_STATE.equals(bean.getInstanceState())
                    || WorkFlowConstants.INS_START_STATE.equals(bean.getInstanceState())
                    || WorkFlowConstants.INS_RUNNING_STATE.equals(bean.getInstanceState())) {

                // 引擎开始调度执行 ,启动流程
                ActionEvent actionEvent = new ActionEvent(bean, "doStartInstance");
                engineEvent.notifyObserver(actionEvent);
            }

            // 返回参数
            result.put("flag", "0");
            result.put("instanceCode", bean.getInstanceCode());

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
        return result;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Map<String, Object> submitProcess(ProcessInstanceBean bean) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            // 获取开始节点信息
            StartActivityHandler startActivityHandler = engineCommonHandler.getStartActivityHandler(bean.getInstanceId());
            bean.setCurrentActivityHandler(startActivityHandler);

            // 设置实例信息处理类
            ProcessInstanceHandler processInstanceHandler = engineCommonHandler.getProcessInstanceHandler(bean);
            bean.setProcessInstanceHandler(processInstanceHandler);

            // 计算执行路径
            if (bean.getProcessExecPath() == null) {
                // 取得当前节点对象
                ActivityBean activityBean = startActivityHandler.getActivityBean();
                // 设置当前节点迁出线的编码
                activityBean.setChooseLineCode(bean.getLineCode());
                // 取得执行路径
                List<ActivityBean> processExecPath = activityExecutionPathService.getNextExecutionActivityPath(activityBean, bean);
                bean.setProcessExecPath(processExecPath);
            }

            // 运行流程
            ActionEvent actionEvent = new ActionEvent(bean, "doRunProcess");
            engineEvent.notifyObserver(actionEvent);

            // 返回值
            result.put("flag", "0");
            result.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0039", null));
            result.put("instanceCode", bean.getInstanceCode());

        } catch (Exception e) {
            log.error(e);
            throw e;
        }
        return result;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Map<String, String> revokeProcess(ProcessInstanceBean bean) throws Exception {
        Map<String, String> hashmap = new HashMap<String, String>();
        try {
            // 判断流程运行中及已完成的节点中是否存在不允许撤办的节点
            List<ActivityStateBean> activityStateList = bean.getActStateList();
            List<ActivityBean> actList = bean.getActivityBeanList();
            if (activityStateList != null && actList != null) {
                // 取得流程实例中完成节点的ID列表
                List<String> actIdList = new ArrayList<String>();
                for (ActivityStateBean activityStateBean : activityStateList) {
                    if (activityStateBean.getActState().equals(WorkFlowConstants.NODE_COMPLETED_STATE)
                            || activityStateBean.getActState().equals(WorkFlowConstants.NODE_RUNNING_STATE)) {
                        String actId = activityStateBean.getActId();
                        actIdList.add(actId);
                    }
                    ;
                }
                // 取得条件
                for (ActivityBean actBean : actList) {
                    // 是否允许允许撤办
                    String allowBackFlag = PropertyHandlerUtil.findActivityPropertyValue(actBean, WorkFlowConstants.A_IS_ALLOW_REVOKE);
                    if ("N".equals(allowBackFlag) && actIdList.contains(actBean.getId())) {

                        HashMap<String, String> tokens = new HashMap<String, String>();
                        tokens.put("actName", actBean.getName());

                        hashmap.put("flag", "1");
                        hashmap.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0041", tokens));
                        return hashmap;
                    }
                }
            }

            // 设置实例信息处理类
            ProcessInstanceHandler processInstanceHandler = engineCommonHandler.getProcessInstanceHandler(bean);
            bean.setProcessInstanceHandler(processInstanceHandler);

            // 引擎开始调度执行
            ActionEvent actionEvent = new ActionEvent(bean, "doCancelProcess");
            engineEvent.notifyObserver(actionEvent);

            // 封装参数
            hashmap.put("flag", "0");
            hashmap.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0040", null));

        } catch (Exception e) {
            throw e;
        }
        return hashmap;
    }

    /*
     * (非 Javadoc)
     * <p>Title: rejectProcess</p>
     * <p>Description: 驳回流程 </p>
     * @param bean
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.run.engine.core.ProcessEngine#rejectProcess(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public Map<String, String> rejectProcess(ProcessInstanceBean bean) throws Exception {
        Map<String, String> hashmap = new HashMap<String, String>();
        try {
            // 驳回流程
            ActionEvent actionEvent = new ActionEvent(bean, "doRejectProcess");
            engineEvent.notifyObserver(actionEvent);

            // 重启流程实例
            bean.setReject(true);
            this.restartInstance(bean);

            // 封装参数
            hashmap.put("flag", "0");
            hashmap.put("msg", XipUtil.getMessage("XIP_WF_SERVICE_0042", null));

        } catch (Exception e) {
            throw e;
        }

        return hashmap;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public HashMap<String, String> stopProcess(ProcessInstanceBean bean) throws Exception {
        HashMap<String, String> hashmap = new HashMap<String, String>();
        return hashmap;
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public Map<String, Object> doProcessSubsequentPath(ProcessInstanceBean processInstanceBean) throws Exception {
        Map<String, Object> hashmap = new HashMap<String, Object>();

        try {
            // 取得本次执行路径的起始节点
            ActivityBean beginActivityBean = new ActivityBean();

            if ("Y".equals(processInstanceBean.getIsTimeout())) {
                // 超时待办处理
                beginActivityBean = processInstanceBean.getCurrentActivityHandler().getActivityBean();
            } else {
                // 非超时待办处理
                if (processInstanceBean.getCurrentTaskId() == null) {
                    // 如果未设置待办Id,则从开始节点计算(使用场景为： 提交流程处理)
                    List<ActivityBean> actList = processInstanceBean.getActivityBeanList();
                    for (ActivityBean actBean : actList) {
                        if (actBean.getActType().equals(WorkFlowConstants.START_NODE)) {
                            beginActivityBean = actBean;
                            break;
                        }
                    }
                } else {
                    // 存在待办Id(使用场景为：完成待办处理)
                    HashMap<String, String> map = workItemDAO.getIdListByTaskId(processInstanceBean.getCurrentTaskId());
                    String actId = map.get("ACTID");

                    List<ActivityBean> actList = processInstanceBean.getActivityBeanList();
                    for (ActivityBean actBean : actList) {
                        if (actBean.getId().equals(actId)) {
                            beginActivityBean = actBean;
                            break;
                        }
                    }
                }
            }

            // 设置当前选择线的编码
            String moveOutLineCode = processInstanceBean.getLineCode();
            beginActivityBean.setChooseLineCode(moveOutLineCode);
            if (WorkFlowConstants.TASK_BTN_SIGN_REJECT.equals(moveOutLineCode)) { // 默认驳回线
                String result = PropertyHandlerUtil.findPropertyValue(beginActivityBean.getActPropertyBeanList(), WorkFlowConstants.A_REJECT_CST_NAME);
                if (result == null || "".equals(result)) {
                    result = XipUtil.getMessage("XIP_WF_SERVICE_0037", null);  // 驳回
                }
                processInstanceBean.setCurrentResult(result);
            }

            // 计算从开始节点向下的执行路径
            List<ActivityBean> activityPath = activityExecutionPathService.getNextExecutionActivityPath(beginActivityBean, processInstanceBean);

            // 获取执行路径中的末节点
            ActivityBean lastActivityBean = activityPath.get(activityPath.size() - 1);

            // 根据本路径中末节点的属性配置信息  , 判断本路径中末节点是否需要手动选人 . Y-手动选人 , N-非手动选人
            String isAssginExecutor = "N";
            if (activityPath != null && activityPath.size() > 1) {
                // 执行路径存在多个节点时, 需判断末尾节点是否需要手动选人
                isAssginExecutor = PropertyHandlerUtil.findActivityPropertyValue(lastActivityBean, WorkFlowConstants.A_IS_ASSGIN_EXECUTOR);

                // 节点的配置执行人信息
                List<ActivityExecutorBean> cfgActExecutors =  lastActivityBean.getActivityConfigExecutorList();

                // 节点最终执行人
                List<ActivityExecutorBean> finalActExecutors = lastActivityBean.getActivityExecutorList();

                // 判断节点执行人配置是否正确
                String actType = lastActivityBean.getActType();
                if (actType.equals(WorkFlowConstants.COUNTERSIGN_NODE) || actType.equals(WorkFlowConstants.NOTICE_NODE)) {

                    if (finalActExecutors.isEmpty() && cfgActExecutors.isEmpty()) {
                        HashMap<String, String> tokens = new HashMap<String, String>();
                        tokens.put("actName", lastActivityBean.getName());
                        throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0036", tokens));

                    } else {
                        if (finalActExecutors != null && finalActExecutors.size() > 0) {
                            for (int i = 0; i < finalActExecutors.size(); i++) {
                                ActivityExecutorBean bean1 = finalActExecutors.get(i);
                                for (int j = 0; j < cfgActExecutors.size(); j++) {
                                    ActivityExecutorBean bean2 = cfgActExecutors.get(j);
                                    if (bean1.getUserId().equals(bean2.getUserId())) {
                                        bean1.setOrgId(bean2.getOrgId());
                                        bean1.setOrgName(bean2.getOrgName());
                                        bean1.setActName(lastActivityBean.getName());
                                        finalActExecutors.set(i, bean1);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    // 判断节点是否需要手动选人
                    if ("N".equals(isAssginExecutor)) {
                        hashmap.put("flag", "1");                                // 节点是否手动选人标志 : 0-手动选人  ,1-非手动选人 , 2-错误标志
                        hashmap.put("execPath", activityPath);                    // 执行路径
                        hashmap.put("executors", finalActExecutors);            // 节点待选人执行人信息

                    } else {
                        hashmap.put("flag", "0");                                // 节点是否手动选人标志 : 0-手动选人  ,1-非手动选人 , 2-错误标志
                        hashmap.put("execPath", activityPath);                    // 执行路径
                        hashmap.put("executors", finalActExecutors);            // 节点待选人执行人信息
                        hashmap.put("actType", lastActivityBean.getActType());    // 节点类型
                        hashmap.put("actBean", lastActivityBean);                // 当前节点
                    }

                } else {
                    hashmap.put("flag", "1");                                // 节点是否手动选人标志 : 0-手动选人  ,1-非手动选人 , 2-错误标志
                    hashmap.put("execPath", activityPath);                    // 执行路径
                    hashmap.put("actType", lastActivityBean.getActType());    // 节点类型
                }

            } else {
                hashmap.put("flag", "1");                                // 节点是否手动选人标志 : 0-手动选人  ,1-非手动选人 , 2-错误标志
                hashmap.put("execPath", activityPath);                    // 执行路径
                hashmap.put("actType", lastActivityBean.getActType());    // 节点类型
            }
        } catch (Exception e) {
            throw e;
        }
        return hashmap;
    }

}
