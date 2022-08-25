package com.papla.cloud.workflow.engine.run.engine.core.instance.impl;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.papla.cloud.workflow.util.PlatformConstants;
import com.papla.cloud.workflow.util.PlatformUtil;
import com.papla.cloud.workflow.util.XipUtil;
import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.dao.ProcessInstanceDAO;
import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.arch.InstanceArchiveService;
import com.papla.cloud.workflow.engine.run.engine.core.common.EngineCommonHandler;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;
import com.papla.cloud.workflow.engine.run.engine.core.controller.EngineEvent;
import com.papla.cloud.workflow.engine.run.engine.core.controller.EngineEventObserver;
import com.papla.cloud.workflow.engine.run.engine.core.instance.ExecutionService;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ExecutionServiceImpl.java
 * @Package com.papla.cloud.wf.run.engine.core.instance.impl
 * @Description: 运行时【流程实例】处理服务实现类
 * @date 2021年8月25日 下午4:12:23
 */
@Service("executionService")
public class ExecutionServiceImpl extends EngineEventObserver implements ExecutionService {

    @Resource
    private EngineEvent engineEvent;
    @Resource
    private ProcessInstanceDAO processInstanceDAO;
    @Resource
    private ProcessEngineCommonService processEngineCommonService;
    @Resource
    private EngineCommonHandler engineCommonHandler;
    @Resource
    private InstanceArchiveService instanceArchiveService;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doStartProcess(ActionEvent actionEvent) throws Exception {

        // 流程实例Bean
        ProcessInstanceBean bean = actionEvent.getBean();

        // 生成流程实例ID和实例编码
        String instanceId = UUID.randomUUID().toString();
        String instanceCode = null;

        try {
            instanceCode = processEngineCommonService.createProcessInstanceCode();
        } catch (Exception e) {
            throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0048", null));
        }

        // 流程实例Bean赋值
        bean.setInstanceId(instanceId);
        bean.setInstanceCode(instanceCode);

        // 判断是否自动失效规避策略（如果流程中存在环路则自动失效规避策略）
        boolean flag = processEngineCommonService.isDisableAvoidStrategy(bean.getDeployId());
        bean.setExistsLoop(flag);
        bean.setAutoDisabledAvoidStrategy(flag ? "Y" : "N");

        // 创建流程实例
        try {
            bean = processInstanceDAO.createProcessInstance(bean);
        } catch (Exception e) {
            throw new Exception("Workflow started failure：" + e.getMessage());
        }

        // 调度节点处理
        try {
            // 获取当前节点（新建实例时即开始节点）
            bean.setCurrentActivityHandler(engineCommonHandler.getStartActivityHandler(bean.getInstanceId()));

            ActionEvent event = new ActionEvent(bean, "doStartActivity");
            engineEvent.notifyObserver(event);

        } catch (Exception e) {
            throw new Exception("Workflow started failure：" + e.getMessage());
        }

    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doStartInstance(ActionEvent actionEvent) throws Exception {

        // 流程实例Bean
        ProcessInstanceBean bean = actionEvent.getBean();

        // 判断是否自动失效规避策略（如果流程中存在环路则自动失效规避策略）
        boolean flag = processEngineCommonService.isDisableAvoidStrategy(bean.getProcessId());
        bean.setExistsLoop(flag);
        bean.setAutoDisabledAvoidStrategy(flag ? "Y" : "N");

        // 初始化流程实例
        try {
            bean = processInstanceDAO.initProcessInstance(bean);
        } catch (Exception e) {
            throw new Exception("Restarted Instance failure: " + e.getMessage());
        }

        // 调度节点处理
        try {

            // 获取当前节点（新建实例时即开始节点）
            bean.setCurrentActivityHandler(engineCommonHandler.getStartActivityHandler(bean.getInstanceId()));
            ActionEvent event = new ActionEvent(bean, "doStartActivity");
            engineEvent.notifyObserver(event);

        } catch (Exception e) {
            throw new Exception("Restarted Instance failure: " + e.getMessage());
        }

    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doRunProcess(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();

            // 更新流程实例信息
            processInstanceDAO.updateInstanceBusiness(bean);

            // 处理流程实例状态
            bean.getProcessInstanceHandler().toRun(bean);

            // 调度节点运行
            ActionEvent event = new ActionEvent(bean, "sigal");
            engineEvent.notifyObserver(event);

        } catch (Exception e) {
            throw e;
        }
    }

    ;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doCancelProcess(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();

            String insStatus = bean.getInstanceState();

            // 处理流程实例状态
            bean.getProcessInstanceHandler().toCancel(bean);

            //调度节点处理
            ActionEvent event = new ActionEvent(bean, "doCancelActivity");
            engineEvent.notifyObserver(event);

            if (!WorkFlowConstants.INS_START_STATE.equals(insStatus)) {
                // 调用撤回函数
                Map<String, String> hashmap = bean.getPropertyMap();
                String funcType = (String) hashmap.get(WorkFlowConstants.P_REVOKE_TYPE);    // 撤回函数类型
                String funcValue = (String) hashmap.get(WorkFlowConstants.P_REVOKE_FUNC);    // 撤回函数
                if (funcValue != null && !"".equals(funcValue)) {
                    processEngineCommonService.invokeFunc(bean, funcType, funcValue);
                }

                /*===============================================
                 * 更新流程实例对应的业务状态
                 *==============================================*/

                // 取得业务状态
                Map<String, String> hahsmap = bean.getPropertyMap();
                String bizStatus = (String) hahsmap.get(WorkFlowConstants.P_BIZ_STATUS);    // 撤回时业务状态
                bean.setCurrentBizState(bizStatus);

                // 执行更新处理
                if (bizStatus != null && !"".equals(bizStatus) && !"null".equals(bizStatus)) {
                    processInstanceDAO.updateBizState(bean);
                }
            }

        } catch (Exception e) {
            throw e;
        }
    }

    ;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doRejectProcess(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();

            // 调度节点处理
            ActionEvent event = new ActionEvent(bean, "doRejectActivity");
            engineEvent.notifyObserver(event);

            // 更新实例业务状态
            if (bean.getCurrentBizState() != null && !"".equals(bean.getCurrentBizState()) && !"null".equals(bean.getCurrentBizState())) {
                processInstanceDAO.updateBizState(bean);
            }

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doTerminateProcess(ActionEvent actionEvent) throws Exception {
    }

    ;


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doCompleteProcess(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();

            // 处理流程实例状态
            bean.getProcessInstanceHandler().toComplete(bean);

            // 新启动线程执行流程归档处理
            String isArch = null;
            try {
                isArch = PlatformUtil.getSystemParamVal(PlatformConstants.IS_ENABLE_ARCH);
            } catch (Exception e1) {
                isArch = null;
            }
            if (isArch == null || "".equals(isArch)) {
                isArch = "Y";
            }

            // 执行归档
            try {
                instanceArchiveService.archiveCompletedInstance(bean.getInstanceId());
            } catch (Exception e) {
                log.error("Archived failure: The method name is 【" + this.getClass().getName() + ".archiveCompletedInstance】, " + e.getMessage());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    ;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void sigal(ActionEvent actionEvent) throws Exception {
        try {

            ProcessInstanceBean bean = actionEvent.getBean();

            // 计算当前节点
            AbstractActivityHandler currentActivityHandler = bean.getCurrentActivityHandler();

            if (currentActivityHandler == null) {
                bean = engineCommonHandler.setCurrentActivityHandler(bean);
            }

            // 处理当前节点事件
            if (bean.getCurrentActivityHandler() != null) {
                ActionEvent event = new ActionEvent(bean, "doRunActivity");
                engineEvent.notifyObserver(event);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
