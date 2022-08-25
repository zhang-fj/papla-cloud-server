package com.papla.cloud.workflow.engine.run.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.papla.cloud.workflow.engine.run.engine.core.task.WorkTaskService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.common.util.WorkflowServiceUtil;
import com.papla.cloud.workflow.engine.dao.base.WorkFlowUserDAO;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityExecutorDao;
import com.papla.cloud.workflow.engine.run.dao.ProcessInstanceDAO;
import com.papla.cloud.workflow.engine.run.dao.TransitionDAO;
import com.papla.cloud.workflow.engine.run.engine.core.ProcessEngine;
import com.papla.cloud.workflow.engine.run.service.ProcessRunService;
import com.papla.cloud.workflow.util.XipUtil;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessRunServiceImpl.java
 * @Package com.papla.cloud.wf.run.service.impl
 * @Description: 流程运行后台数据处理服务接口
 * @date 2021年8月25日 下午6:43:19
 */
@Service("processRunService")
public class ProcessRunServiceImpl implements ProcessRunService {

    private static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private ProcessInstanceDAO processInstanceDAO;

    @Resource
    private WorkTaskService workTaskService;

    @Resource
    private ActivityExecutorDao activityExecutorDao;

    @Resource
    private TransitionDAO transitionDAO;

    @Resource
    private ProcessEngineCommonService processEngineCommonService;

    @Override
    public String getDeployIdByProcessCode(String processCode) throws Exception {
        return processInstanceDAO.getDeployIdByProcessCode(processCode);
    }

    @Override
    public Map<String, String> createInstanceByProcessCode(String processCode, String businessId) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("flag", "1");
        try {
            // 查询流程ID
            String deployId = processInstanceDAO.getDeployIdByProcessCode(processCode);
            if (StringUtils.isEmpty(deployId)) {
                throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0031", null));
            }
            // 根据流程ID，启动流程
            return createInstanceByDeployId(deployId, businessId);
        } catch (Exception e) {
            log.error(e);
            result.put("msg", XipUtil.getMessage(e.getMessage(), null));
        }
        return result;
    }

    ;

    @Override
    public Map<String, String> createInstanceByDeployId(String deployId, String businessId) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("flag", "1");
        try {

            // 查询流程基本信息
            ProcessDeployBean pb = processInstanceDAO.getProcessDeployById(deployId);

            if (pb == null) {
                HashMap<String, String> tokens = new HashMap<String, String>();
                tokens.put("deployId", deployId);
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0076", tokens));
            }

            // 判断流程状态是否为启用状态
            if (!"Y".equals(pb.getEnabled())) {
                HashMap<String, String> tokens = new HashMap<String, String>();
                tokens.put("name", pb.getProcessName());
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0035", tokens));
            }

            ProcessInstanceBean bean = new ProcessInstanceBean();
            bean.setDeployId(deployId);
            bean.setBusinessId(businessId);
            bean.setProcessId(pb.getProcessId());
            bean.setProcessCode(pb.getProcessCode());
            bean.setProcessName(pb.getProcessName());
            bean.setProcessVersion(pb.getProcessVersion());
            bean.setAppId(pb.getAppId());
            bean.setEntityId(pb.getEntityId());

            result = processEngine.startProcess(bean);
        } catch (Exception e) {
            log.error(e);
            result.put("msg", XipUtil.getMessage(e.getMessage(), null));
        }
        return result;
    }

    ;

    @Override
    public Map<String, String> restartInstance(String instanceCode) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("instanceCode", instanceCode);
        result.put("flag", "1");
        try {

            // 获取运行中【流程实例】
            ProcessInstanceBean bean = processInstanceDAO.getRunBaseInstanceBeanByCode(instanceCode);

            if (bean == null) {
                result.put("msg", XipUtil.getMessage("XIP_WF_COMMON_0028", null));
                return result;
            }

            // 判断流程实例状态是否合法
            String state = bean.getInstanceState();
            if (!(state.equals(WorkFlowConstants.INS_START_STATE) || state.equals(WorkFlowConstants.INS_CANCELED_STATE))) {
                HashMap<String, String> tokens = new HashMap<String, String>();
                tokens.put("state", bean.getInstanceState());
                result.put("msg", XipUtil.getMessage("XIP_WF_COMMON_0029", null));
                return result;
            }

            // 获取当前最新版本【流程部署】基本信息
            String deployId = processInstanceDAO.getDeployIdByProcessCode(bean.getProcessCode());
            ProcessDeployBean deployBean = processInstanceDAO.getProcessDeployById(deployId);

            // 复制最新版本【流程部署】基本信息
            bean.setDeployId(deployBean.getDeployId());
            bean.setAppId(deployBean.getAppId());
            bean.setAppCode(deployBean.getAppCode());
            bean.setEntityId(deployBean.getEntityId());
            bean.setEntityCode(deployBean.getEntityCode());
            bean.setProcessId(deployBean.getProcessId());
            bean.setProcessCode(deployBean.getProcessCode());
            bean.setProcessName(deployBean.getProcessName());
            bean.setProcessVersion(deployBean.getProcessVersion());
            bean.setInstanceTitle(deployBean.getProcessName());
            bean.setProcessJson(deployBean.getProcessJson());

            // 重启流程实例
            result = processEngine.restartInstance(bean);
        } catch (Exception e) {
            log.error(e);
            result.put("msg", XipUtil.getMessage(e.getMessage(), null));
        }
        return result;

    }

    ;

    @Override
    public Map<String, Object> submitProcess(String instanceCode, Map<String, String> attrMap, String[] userIdArray) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("instanceCode", instanceCode);
        result.put("flag", "1");

        try {
            // 流程状态
            String state = processInstanceDAO.getInstanceState(instanceCode);
            if (state == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0002", null));

            } else if (state.equals(WorkFlowConstants.INS_RUNNING_STATE) || state.equals(WorkFlowConstants.INS_COMPLETED_STATE)) {
                Map<String, String> tokens = new HashMap<String, String>();
                tokens.put("state", state);
                throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0029", tokens));
            }

            // 初始化流程实例
            ProcessInstanceBean processBean = processInstanceDAO.getProcessInstanceByCode(instanceCode);

            // 流程实例实体赋值
            PropertyHandlerUtil.setInstanceEntityAttrValue(processBean, attrMap);

            // 保存流程实例实体属性值
            processInstanceDAO.saveInsEntityAttrValue(processBean.getAttrList());

            // 如果流程状态为开始状态 2017-02-09
            if (state.equals(WorkFlowConstants.INS_START_STATE) || state.equals(WorkFlowConstants.INS_CANCELED_STATE)) {
                processEngineCommonService.createDynaActivityQueue(processBean);
            }

            // 获取当前节点执行人
            Map<String, Object> map = this.getActivityExecutorList(processBean, userIdArray);

            // 如果是手动选人
            if ("2".equals(map.get("flag"))) {
                return map;
            } else {
                // 设置选择的节点执行人参数
                processBean.setChoosedUserList((List<ActivityExecutorBean>) map.get("executors"));
                return processEngine.submitProcess(processBean);
            }

        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("data", new ArrayList<Map<String, Object>>());
        }

        return result;
    }

    @Override
    public Map<String, Object> startAndSubmitProcess(String processId, String businessId, HashMap<String, String> attrMap) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", "1");

        Map<String, String> itemKeyXml = createInstanceByDeployId(processId, businessId);
        String instanceCode = "";
        try {
            if ("0".equals(itemKeyXml.get("flag"))) {
                instanceCode = itemKeyXml.get("instanceCode");
            } else {
                result.put("msg", itemKeyXml.get("msg"));
                return result;   //创建流程实例失败，返回错误信息
            }
            result = submitProcess(instanceCode, attrMap, new String[]{});
        } catch (Exception e) {
            result.put("msg", e.getMessage());
        }
        return result;
    }

    ;

    @Override
    public Map<String, Object> revokeProcess(String instanceCode) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", "1");

        try {

            String state = processInstanceDAO.getInstanceState(instanceCode);
            if (state == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0002", null));

            } else if (state.equals(WorkFlowConstants.INS_COMPLETED_STATE) || state.equals(WorkFlowConstants.INS_TERMINATE_STATE) || state.equals(WorkFlowConstants.INS_CANCELED_STATE)) {
                HashMap<String, String> tokens = new HashMap<String, String>();
                tokens.put("state", state);
                result.put("msg", XipUtil.getMessage("XIP_WF_COMMON_0034", tokens));
                return result;
            }

            ProcessInstanceBean processBean = processInstanceDAO.getProcessInstanceByCode(instanceCode);
            Map<String, String> hashmap = processEngine.revokeProcess(processBean);

            if ("0".equals(hashmap.get("flag"))) {
                result.put("flag", "0");
                result.put("msg", XipUtil.getMessage("XIP_WF_COMMON_0035", null));
            } else {
                result.put("msg", hashmap.get("msg"));
            }

            return result;

        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", e.getMessage());
            return result;
        }
    }

    ;

    @Override
    public Map<String, Object> rejectProcess(String taskId, String approverComment) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", "1");

        try {
            // 初始化实例Bean
            ProcessInstanceBean bean = processInstanceDAO.getProcessInstanceByTaskId(taskId);

            bean.setCurrentTaskId(taskId);
            bean.setApproverComment(approverComment);

            // 执行撤回处理
            Map<String, String> hashmap = processEngine.rejectProcess(bean);

            // 封装返回信息
            String flag = hashmap.get("flag");
            if ("0".equals(flag)) {
                result.put("flag", "0");
                result.put("msg", XipUtil.getMessage("XIP_WF_COMMON_0043", null));
            } else {
                result.put("msg", hashmap.get("msg"));
            }
        } catch (Exception e) {

            log.error(e.getMessage());
            result.put("msg", "error：" + e.getMessage());

        }

        return result;
    }

    @Override
    public Map<String, Object> completeWorkItem(String taskId, String lineCode, String comment, String[] userIdArray) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", "1");
        result.put("taskId", taskId);

        ProcessInstanceBean processBean = null;
        try {
            // 初始化实例信息
            processBean = processInstanceDAO.getProcessInstanceByTaskId(taskId);
            processBean.setCurrentTaskId(taskId);        // 当前待办ID
            processBean.setLineCode(lineCode);            // 连线编码
            processBean.setApproverComment(comment);    // 审批意见

            // 初始化连线信息
            if (WorkFlowConstants.TASK_BTN_SIGN_REJECT.equals(lineCode)) {
                /*
                 * 会签节点驳回处理  modified by linp 2015-04-21
                 * 如果会签节点只存在一根迁出线(即Y线)时; 则其默认存在一根N线指向开始节点且此线的编码【btnSignReject】,如果审批人点击此执行线流程按驳回处理
                 */
                processBean.setLineCategory("N");        // 连线类型: Y-是,N-否

            } else {

                if (WorkFlowConstants.TASK_BTN_LOOP_AGREE.equals(lineCode)) {

                    processBean.setLineCategory("Y");    // 连线类型: Y-是,N-否
                    processBean.setCurrentResult(XipUtil.getMessage("XIP_WF_SERVICE_0053", null));    // 连线名称：同意

                } else {

                    // 非动态通知节点同意按钮
                    TransitionBean tb = transitionDAO.getTransitionBean(processBean.getInstanceId(), lineCode);
                    if (tb == null) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0037", null));

                    processBean.setChooseMoveOutLineId(tb.getId());    // 连线Id
                    processBean.setLineCategory(tb.getCategory());    // 连线类型: Y-是,N-否
                    processBean.setCurrentResult(tb.getName());        // 连线名称
                }
            }

            Map<String, Object> map = this.getActivityExecutorList(processBean, userIdArray);

            if ("2".equals(map.get("flag"))) {
                return map;
            } else {
                // 设置执行人
                processBean.setChoosedUserList((List<ActivityExecutorBean>) map.get("executors"));
                return workTaskService.completeWorkItem(processBean);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * @param processBean
     * @param userIdArray
     * @return Map<String, Object>
     * @throws Exception
     * @Title getActivityExecutorList
     * @Description TODO    获取【取节点执行人】
     */
    private Map<String, Object> getActivityExecutorList(ProcessInstanceBean processBean, String[] userIdArray) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        // 判断userIdArray中是否已经有人，如没有人返回需要手动选人的字符串，如有人则表示已传递来手动选人的串
        if (userIdArray != null && userIdArray.length > 0) {
            result.put("executors", activityExecutorDao.getActivityExecutorListByUserIds(Stream.of(userIdArray).collect(Collectors.toList())));
        } else {
            Map<String, Object> hashmap = processEngine.doProcessSubsequentPath(processBean);
            List<ActivityExecutorBean> executors = (List<ActivityExecutorBean>) hashmap.get("executors");
            if (executors != null) {
                executors = WorkflowServiceUtil.joinUserOrgs(executors);
            }
            if ("0".equals(hashmap.get("flag"))) { // 手动选人
                String actType = (String) hashmap.get("actType");    // 判断当前节点类型
                // 设定选人框是否可以多选 ; Y-多选, N-单选
                String isMultiselect = actType.equals(WorkFlowConstants.NOTICE_NODE) ? "N" : "Y";
                // 封装手动选人返回参数
                result.put("flag", "2");
                result.put("multiselect", isMultiselect);
                result.put("data", hashmap.get("executors"));
            } else if ("1".equals(hashmap.get("flag"))) {    // 非手动选人
                // 设置执行路径
                List<ActivityBean> processExecPath = (List<ActivityBean>) hashmap.get("execPath");
                processBean.setProcessExecPath(processExecPath);
            }
        }
        return result;
    }

    public Map<String, Object> stopProcess(String instanceCode) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("flag", "1");

        try {
            ProcessInstanceBean processBean = processInstanceDAO.getProcessInstanceByCode(instanceCode);
            Map<String, String> hashmap = processEngine.stopProcess(processBean);
            if ("0".equals(hashmap.get("flag"))) {
                result.put("flag", "0");
                result.put("msg", XipUtil.getMessage("XIP_WF_COMMON_0036", null));
            } else {
                String msg = hashmap.get("msg");
                result.put("msg", msg);
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", "error：" + e.getMessage());
            return result;
        }

    }

    ;

}
