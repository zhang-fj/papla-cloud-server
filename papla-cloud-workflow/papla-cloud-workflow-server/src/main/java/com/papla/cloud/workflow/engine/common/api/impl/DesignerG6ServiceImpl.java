package com.papla.cloud.workflow.engine.common.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papla.cloud.workflow.engine.common.api.ExpressionService;
import com.papla.cloud.workflow.engine.common.api.G6Service;
import com.papla.cloud.workflow.engine.common.enums.ActivityPropertyEnum;
import com.papla.cloud.workflow.engine.common.enums.ProcessPropertyEnum;
import com.papla.cloud.workflow.engine.common.enums.TransitionPropertyEnum;
import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActivityPropertyMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployPropertyMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsActivityStateMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsEntityAttrValueMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsTransitionStateMapper;
import com.papla.cloud.workflow.engine.mapper.RunInstanceMapper;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityPropertyBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.modal.ConditionGroupBean;
import com.papla.cloud.workflow.engine.modal.DeployPropertyBean;
import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.PVEnumBean;
import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.PropertyBean;
import com.papla.cloud.workflow.engine.modal.TransStateBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.util.XipUtil;

/**
 * @author linpeng
 * @ClassName: DesignerXMLServiceImpl
 * @Description: 流程平台设计器G6文件处理实现类
 * @date 2015年4月28日 下午1:32:34
 */
@Service("G6Service")
public class DesignerG6ServiceImpl implements G6Service {

    @Resource
    private ExpressionService expressionService;
    @Resource
    private RunInstanceMapper runInstanceMapper;
    @Resource
    private ProcessDeployActivityPropertyMapper processDeployActivityPropertyMapper;
    @Resource
    private RunInsEntityAttrValueMapper runInsEntityAttrValueMapper;
    @Resource
    private RunInsTransitionStateMapper runInsTransitionStateMapper;
    @Resource
    private ProcessDeployPropertyMapper processDeployPropertyMapper;
    @Resource
    private RunInsActivityStateMapper runInsActivityStateMapper;

    /**
     * 解析JSON文件
     *
     * @param G6Process g6数据
     * @throws DocumentException
     **/
    @Override
    public ProcessDeployBean parseG6(String processId, String deployId, Map<String, Object> G6Process) throws Exception {
        return parseG6(processId, deployId, G6Process, "update", "", "", "", "", "");
    }

    ;

    /**
     * createProcessProperty 构建流程PropertyBean
     *
     * @param code
     * @param name
     * @param G6Process
     * @param processId
     **/
    public DeployPropertyBean createProcessProperty(String code, String name, Map<String, Object> G6Process, String processId, String deployId) {
        String userId = "";
        return new DeployPropertyBean(code, name, (String) G6Process.get(code), processId, deployId, userId, new Date());
    }

    /**
     * createActivityProperty: 构建节点PropertyBean
     *
     * @param code
     * @param name
     * @param value
     * @param actId
     * @author asus
     */
    public ActivityPropertyBean createActivityProperty(String code, String name, String value, String actId, String deployId) {
        String userId = "";
        //对于值为N/Y的属性，如果没有设置，默认为N
        return new ActivityPropertyBean(code, name, value, actId, deployId, userId, new Date());
    }

    /**
     * createActivityPvEnumBean:
     *
     * @param actId        节点id
     * @param propertyCode 属性编码
     * @param executorType 执行人类型
     * @param values       静态执行人信息，执行人之间以逗号分隔
     * @return
     * @author asus
     */
    public List<PVEnumBean> createActivityPvEnumBean(String actId, String deployId, String propertyCode, String executorType, String values, String userId) {
        List<PVEnumBean> pVEnumBeans = new ArrayList<PVEnumBean>();

        if (!"".equals(values)) {
            for (int i = 0; i < values.split(",").length; i++) {
                String value = values.split(",")[i];
                PVEnumBean pVEnumBean = new PVEnumBean(UUID.randomUUID().toString(), actId, deployId, propertyCode, executorType, (String) value, userId, new Date());
                pVEnumBeans.add(pVEnumBean);
            }
        }
        return pVEnumBeans;
    }

    /**
     * parseG6: 将G6Process转换成ProcessBean对象，需要注意的是，如果是点击复制时调用此方法，节点执行人相关的信息将不予以复制，并将已存在的数据进行清空
     * @param processId
     * @param G6Process
     * @param operationType
     * @return
     * @throws JsonProcessingException
     * @author zhangfj
     */
    public ProcessDeployBean parseG6(String processId, String deployId, Map<String, Object> G6Process, String operationType, String entityId, String processCategory, String orgId, String newProcessCode, String newProcessName) throws JsonProcessingException {
        String userId = "";
        try {
            userId = CurrentUserUtil.getCurrentUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProcessDeployBean processDeployBean = new ProcessDeployBean();

        processDeployBean.setProcessId(processId);
        processDeployBean.setDeployId(deployId);
        processDeployBean.setProcessName((String) G6Process.get(ProcessPropertyEnum.P_NAME.getCode()));
        processDeployBean.setProcessDesc((String) G6Process.get(ProcessPropertyEnum.P_DESC.getCode()));
        processDeployBean.setUpdateBy(userId);
        processDeployBean.setUpdateDt(new Date());
        processDeployBean.setEnabled("Y");

        if ("copy".equals(operationType)) {
            if (G6Process.get(ProcessPropertyEnum.P_ENTITY_ID.getCode()) != null) {
                G6Process.put(ProcessPropertyEnum.P_ENTITY_ID.getCode(), entityId);
            }

            if (G6Process.get(ProcessPropertyEnum.P_CATEGORY.getCode()) != null) {
                G6Process.put(ProcessPropertyEnum.P_CATEGORY.getCode(), processCategory);
            }

            if (G6Process.get(ProcessPropertyEnum.P_ORG_ID.getCode()) != null) {
                G6Process.put(ProcessPropertyEnum.P_ORG_ID.getCode(), processCategory);
            }

            G6Process.put(ProcessPropertyEnum.P_CODE.getCode(), newProcessCode);
            G6Process.put(ProcessPropertyEnum.P_NAME.getCode(), newProcessName);

        }

        //对应流程属性表
        List<DeployPropertyBean> processProperties = new ArrayList<DeployPropertyBean>();

        List<ConditionGroupBean> processConditionGroupList = new ArrayList<ConditionGroupBean>();

        for (ProcessPropertyEnum processPropertyEnum : ProcessPropertyEnum.values()) {

            //创建流程属性
            if (!processPropertyEnum.isEntityProperty()) {

                String code = processPropertyEnum.getCode();
                String value = (String) G6Process.get(code);

                if (!processPropertyEnum.isEntityProperty() && (processPropertyEnum.isRequired() || StringUtils.isNotBlank(value))) {

                    if (StringUtils.isBlank(value)) {
                        value = processPropertyEnum.getDefaultValue();
                    }

                    G6Process.put(processPropertyEnum.getCode(), value);

                    processProperties.add(createProcessProperty(code, XipUtil.getMessage(processPropertyEnum.getName(), null), G6Process, processId, deployId));

                    // 添加流程条件
                    if (code.equals(ProcessPropertyEnum.P_PAD_FORM_TYPE.getCode())) {
                        if ("R".equals(value))
                            processConditionGroupList.addAll(expressionService.paraseExpressionToDB("P", processId, ProcessPropertyEnum.P_PAD_ROUTE_FORM.getCode(), (String) G6Process.get(ProcessPropertyEnum.P_PAD_ROUTE_FORM.getCode())));
                    } else if (code.equals(ProcessPropertyEnum.P_M_FORM_TYPE.getCode())) {
                        if ("R".equals(value))
                            processConditionGroupList.addAll(expressionService.paraseExpressionToDB("P", processId, ProcessPropertyEnum.P_M_ROUTE_FORM.getCode(), (String) G6Process.get(ProcessPropertyEnum.P_M_ROUTE_FORM.getCode())));
                    } else if (code.equals(ProcessPropertyEnum.P_FORM_TYPE.getCode())) {
                        if ("R".equals(value))
                            processConditionGroupList.addAll(expressionService.paraseExpressionToDB("P", processId, ProcessPropertyEnum.P_ROUTE_FORM.getCode(), (String) G6Process.get(ProcessPropertyEnum.P_ROUTE_FORM.getCode())));
                    } else if (code.equals(ProcessPropertyEnum.P_BOUND_CONDITION.getCode())) {
                        if (StringUtils.isNotBlank(value)) {
                            String processCondtion = "[{\"boundId\":\"\",\"conditions\":\"" + value + "\"}]";
                            processConditionGroupList.addAll(expressionService.paraseExpressionToDB("P", processId, code, processCondtion));
                        }
                    }
                }
            }
        }

        processDeployBean.setConditionGroupList(processConditionGroupList);
        processDeployBean.setDeployPropertyList(processProperties);

        List<TransitionBean> transitionBeanList = new ArrayList<TransitionBean>();
        List<ActivityBean> activityBeanList = new ArrayList<ActivityBean>();

        List<Map<String, Object>> lines = (List<Map<String, Object>>) G6Process.get("edges");
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) G6Process.get("nodes");

        //遍历连线
        if (lines != null) {
            for (Map<String, Object> line : lines) {

                Map<String, Object> data = (Map<String, Object>) line.get("data");

                Integer designerNum = (Integer) line.get("index");
                String uuid = (String) data.get("id");
                if ("".equals(uuid) || uuid == null || "copy".equals(operationType)) {
                    uuid = UUID.randomUUID().toString();
                    data.put("id", uuid);
                }

                //连线
                TransitionBean transitionBean = new TransitionBean();

                transitionBean.setId(uuid);
                transitionBean.setProcessId(processId);
                transitionBean.setDeployId(deployId);

                if (nodes != null) {
                    for (Map<String, Object> node : nodes) {
                        if (node.get("id").equals(line.get("source"))) {
                            transitionBean.setFromActId((Integer) node.get("index"));
                        }
                        if (node.get("id").equals(line.get("target"))) {
                            transitionBean.setToActId((Integer) node.get("index"));
                        }
                    }
                }
                String tcode = (String) data.get(TransitionPropertyEnum.T_CODE.getCode());
                if (StringUtils.isBlank(tcode)) {
                    tcode = "T" + StringUtils.leftPad("" + designerNum, 3, "0");
                    data.put(TransitionPropertyEnum.T_CODE.getCode(), tcode);
                }
                transitionBean.setCode(tcode);
                transitionBean.setFuncType((String) data.get(TransitionPropertyEnum.T_FUNC_TYPE.getCode()));
                transitionBean.setFuncName((String) data.get(TransitionPropertyEnum.T_FUNC_NAME.getCode()));

                if (StringUtils.isBlank((String) data.get(TransitionPropertyEnum.T_TYPE.getCode()))) {
                    data.put(TransitionPropertyEnum.T_TYPE.getCode(), TransitionPropertyEnum.T_TYPE.getDefaultValue());
                    transitionBean.setType(TransitionPropertyEnum.T_TYPE.getDefaultValue());
                } else {
                    transitionBean.setType((String) data.get(TransitionPropertyEnum.T_TYPE.getCode()));
                }

                transitionBean.setLineNum(designerNum + "");
                transitionBean.setBizStatus((String) data.get(TransitionPropertyEnum.T_BIZ_STATUS.getCode()) == null ? "" : (String) data.get(TransitionPropertyEnum.T_BIZ_STATUS.getCode()));

                transitionBean.setCreateBy(userId);
                transitionBean.setCreateDt(new Date());

                transitionBean.setName((String) data.get("name"));
                transitionBeanList.add(transitionBean);
            }
        }
        //遍历节点
        if (nodes != null) {
            for (Map<String, Object> node : nodes) {

                Map<String, Object> data = (Map<String, Object>) node.get("data");

                Integer designerNum = (Integer) node.get("index");
                String activityType = (String) node.get("activityType");
                String uuid = (String) data.get("id");
                if ("".equals(uuid) || uuid == null || "copy".equals(operationType)) {
                    uuid = UUID.randomUUID().toString();
                    data.put("id", uuid);
                }

                //节点
                ActivityBean activityBean = new ActivityBean();
                activityBean.setId(uuid);
                activityBean.setProcessId(processId);
                activityBean.setDeployId(deployId);
                activityBean.setActType(activityType);
                activityBean.setNum(designerNum);
                activityBean.setCreateBy(userId);
                activityBean.setCreateDt(new Date());
                List<ActivityPropertyBean> actPropertyList = new ArrayList<ActivityPropertyBean>();
                List<ConditionGroupBean> conditionGroupList = new ArrayList<ConditionGroupBean>();

                List<PVEnumBean> pVEnumBeans = new ArrayList<PVEnumBean>();

                //遍历节点属性

                if (data != null) {
                    String acode = (String) data.get(ActivityPropertyEnum.A_CODE.getCode());
                    if (StringUtils.isBlank(acode)) {
                        acode = "A" + StringUtils.leftPad("" + designerNum, 3, "0");
                        data.put(ActivityPropertyEnum.A_CODE.getCode(), acode);
                    }
                    activityBean.setCode(acode);
                    activityBean.setName((String) data.get("name"));
                    activityBean.setActDesc((String) data.get(ActivityPropertyEnum.A_DESC.getCode()));

                    for (ActivityPropertyEnum activityPropertyEnum : ActivityPropertyEnum.values()) {

                        if (!activityPropertyEnum.isEntityProperty()) {
                            String code = activityPropertyEnum.getCode();
                            String value = (String) data.get(code);
                            String name = (String) activityPropertyEnum.getName();

                            // 如果是必须存在的流程属性并且属性值不存在时，设置默认值
                            if (activityPropertyEnum.isRequired() || StringUtils.isNotBlank(value)) {

                                if (StringUtils.isBlank(value)) {
                                    value = activityPropertyEnum.getDefaultValue();
                                }
                                if (ActivityPropertyEnum.A_EXECUTOR_TYPE.getCode().equals(code)
                                        || ActivityPropertyEnum.A_STATIC_USER.getCode().equals(code)
                                        || ActivityPropertyEnum.A_STATIC_POSTION.getCode().equals(code)
                                        || ActivityPropertyEnum.A_ROUTE_USER.getCode().equals(code)
                                        || ActivityPropertyEnum.A_ROUTE_POSTION.getCode().equals(code)
                                        || ActivityPropertyEnum.A_DEPT_FILTER.getCode().equals(code)
                                        || ActivityPropertyEnum.A_EXEC_VAR.getCode().equals(code)
                                        || ActivityPropertyEnum.A_EXEC_PROC.getCode().equals(code)
                                        || ActivityPropertyEnum.A_EXEC_WEB.getCode().equals(code)
                                        || ActivityPropertyEnum.A_CONFIG_SQL.getCode().equals(code)
                                        || ActivityPropertyEnum.A_MORE_SQL.getCode().equals(code)
                                        || ActivityPropertyEnum.A_MORE_JAVA.getCode().equals(code)) {
                                    if ("copy".equals(operationType)) {
                                        if (ActivityPropertyEnum.A_ROUTE_USER.getCode().equals(code) || ActivityPropertyEnum.A_ROUTE_POSTION.getCode().equals(code)) {
                                            conditionGroupList.addAll(expressionService.paraseExpressionToDB("A", activityBean.getId(), code, ""));
                                        }
                                        value = "";
                                    } else {

                                        String executorType = (String) data.get(ActivityPropertyEnum.A_EXECUTOR_TYPE.getCode());

                                        if (code.equals(ActivityPropertyEnum.A_STATIC_USER.getCode())) {
                                            if ("SU".equals(executorType)) {
                                                pVEnumBeans.addAll(createActivityPvEnumBean(activityBean.getId(), deployId, code, "U", value, userId));
                                            } else {
                                                value = "";
                                            }
                                        } else if (code.equals(ActivityPropertyEnum.A_STATIC_POSTION.getCode())) {
                                            if ("SP".equals(executorType)) {
                                                pVEnumBeans.addAll(createActivityPvEnumBean(activityBean.getId(), deployId, code, "P", value, userId));
                                            } else {
                                                value = "";
                                            }
                                        } else if (code.equals(ActivityPropertyEnum.A_ROUTE_USER.getCode())) {
                                            if (!"RU".equals(executorType)) {
                                                value = "";
                                            }
                                            conditionGroupList.addAll(expressionService.paraseExpressionToDB("A", activityBean.getId(), code, value));
                                        } else if (code.equals(ActivityPropertyEnum.A_ROUTE_POSTION.getCode())) {
                                            if (!"RP".equals(executorType)) {
                                                value = "";
                                            }
                                            conditionGroupList.addAll(expressionService.paraseExpressionToDB("A", activityBean.getId(), code, value));
                                        } else if (code.equals(ActivityPropertyEnum.A_DEPT_FILTER.getCode())) {
                                            if (!"GRANT".equals(executorType)) {
                                                value = "";
                                            }
                                        } else if (code.equals(ActivityPropertyEnum.A_EXEC_VAR.getCode())) {
                                            if (!"VAR".equals(executorType)) {
                                                value = "";
                                            }
                                        } else if (code.equals(ActivityPropertyEnum.A_EXEC_PROC.getCode())) {
                                            if (!"PROC".equals(executorType)) {
                                                value = "";
                                            }
                                        } else if (code.equals(ActivityPropertyEnum.A_EXEC_WEB.getCode())) {
                                            if (!"WEB".equals(executorType)) {
                                                value = "";
                                            }
                                        } else if (code.equals(ActivityPropertyEnum.A_MORE_JAVA.getCode())) {
                                            if (!"JAVA".equals(executorType)) {
                                                value = "";
                                            }
                                        } else if (code.equals(ActivityPropertyEnum.A_CONFIG_SQL.getCode())) {
                                            if (!"SQL".equals(executorType)) {
                                                value = "";
                                            }
                                        } else if (code.equals(ActivityPropertyEnum.A_MORE_SQL.getCode())) {
                                            if (!"SQL".equals(executorType)) {
                                                value = "";
                                            }
                                        }
                                    }
                                }

                                String formType = null;

                                if (code.equals(ActivityPropertyEnum.A_BOUND_CONDITON.getCode())) {
                                    String condtion = "[{\"boundId\":\"\",\"conditions\":\"" + value + "\"}]";
                                    conditionGroupList.addAll(expressionService.paraseExpressionToDB("A", activityBean.getId(), code, condtion));
                                } else if (code.equals(ActivityPropertyEnum.A_STATIC_FORM.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_FORM_TYPE.getCode());
                                    if (!"S".equals(formType)) {
                                        value = "";
                                    }
                                } else if (code.equals(ActivityPropertyEnum.A_VAR_FORM.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_VAR_FORM.getCode());
                                    if (!"V".equals(formType)) {
                                        value = "";
                                    }
                                } else if (code.equals(ActivityPropertyEnum.A_ROUTE_FORM.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_ROUTE_FORM.getCode());
                                    if (!"R".equals(formType)) {
                                        value = "";
                                    }
                                    conditionGroupList.addAll(expressionService.paraseExpressionToDB("A", activityBean.getId(), code, value));
                                } else if (code.equals(ActivityPropertyEnum.A_PAD_STATIC_FORM.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_PAD_STATIC_FORM.getCode());
                                    if (!"S".equals(formType)) {
                                        value = "";
                                    }
                                } else if (code.equals(ActivityPropertyEnum.A_PAD_VAR_FORM.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_PAD_VAR_FORM.getCode());
                                    if (!"V".equals(formType)) {
                                        value = "";
                                    }
                                } else if (code.equals(ActivityPropertyEnum.A_PAD_ROUTE_FORM.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_PAD_ROUTE_FORM.getCode());
                                    if (!"R".equals(formType)) {
                                        value = "";
                                    }
                                    conditionGroupList.addAll(expressionService.paraseExpressionToDB("A", activityBean.getId(), code, ""));
                                } else if (code.equals(ActivityPropertyEnum.A_M_STATIC_FORM.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_M_STATIC_FORM.getCode());
                                    if (!"S".equals(formType)) {
                                        value = "";
                                    }
                                } else if (code.equals(ActivityPropertyEnum.A_M_VAR_FORM.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_M_VAR_FORM.getCode());
                                    if (!"V".equals(formType)) {
                                        value = "";
                                    }
                                } else if (code.equals(ActivityPropertyEnum.A_M_ROUTE_FORM.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_M_ROUTE_FORM.getCode());
                                    if (!"R".equals(formType)) {
                                        value = "";
                                    }
                                    conditionGroupList.addAll(expressionService.paraseExpressionToDB("A", activityBean.getId(), code, value));
                                } else if (code.equals(ActivityPropertyEnum.A_STATIC_USER.getCode())) {
                                    formType = (String) data.get(ActivityPropertyEnum.A_M_ROUTE_FORM.getCode());
                                    if (!"R".equals(formType)) {
                                        value = "";
                                    }
                                    conditionGroupList.addAll(expressionService.paraseExpressionToDB("A", activityBean.getId(), code, value));
                                }
                                data.put(code, value);
                                if (StringUtils.isNotBlank(name)) {
                                    actPropertyList.add(createActivityProperty(code, XipUtil.getMessage(name, null), value, activityBean.getId(), deployId));
                                }
                            }
                        }
                    }
                }
                activityBean.setActPropertyList(actPropertyList);
                activityBean.setConditionGroupList(conditionGroupList);
                activityBean.setActPropEnumList(pVEnumBeans);
                activityBeanList.add(activityBean);
            }
        }

        processDeployBean.setActivityBeanList(activityBeanList);
        processDeployBean.setTransitionBeanList(transitionBeanList);

        ObjectMapper mapper = new ObjectMapper();
        processDeployBean.setProcessJson(mapper.writeValueAsString(G6Process));
        return processDeployBean;
    }

    /*
     * (非 Javadoc)
     * <p>Title: buildMonitorXml</p>
     * <p>Description: 构建流程监控xml </p>
     * @param instanceId
     * @param xmlStr
     * @param isQueryArch
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.XMLService#buildMonitorXml(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public Map<String, Object> buildMonitorG6(String instanceId, String xmlStr, boolean isQueryArch) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> G6Process = objectMapper.readValue(xmlStr, Map.class);

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("arch", isQueryArch ? "Y" : "N");
        params.put("instanceId", instanceId);

        // 流程实例对象
        ProcessInstanceBean processInstanceBean = runInstanceMapper.getBaseInstanceBean(params);
        // 获取流程属性
        List<DeployPropertyBean> deployPropertyBeans = processDeployPropertyMapper.getPropertyListByDeployId(processInstanceBean.getDeployId());
        // 获取流程实体属性
        List<InstanceEntityAttrBean> instanceEntityAttrBeans = runInsEntityAttrValueMapper.getInstanceEntityAttrValueList(params);
        // 获取流程连线状态
        List<TransStateBean> transStateBeans = runInsTransitionStateMapper.getTransStateList(params);
        // 获取流程节点状态
        List<ActivityStateBean> activityStateBeans = runInsActivityStateMapper.getActivityStateList(params);

        //流程实例业务状态
        String processBizStatus = "";
        if (deployPropertyBeans != null) {
            DeployPropertyBean processBizStatusBean = getInsProperty(deployPropertyBeans, WorkFlowConstants.P_BIZ_STATUS);
            if (processBizStatusBean != null) {
                processBizStatus = processBizStatusBean.getPropertyValue();
            }
        }
        G6Process.put(ProcessPropertyEnum.P_BIZ_STATUS.getCode(), processBizStatus);

        //流程流转信息
        Map<String, String> requiredAttr = new HashMap<String, String>();
        G6Process.put("requiredAttr", requiredAttr);
        requiredAttr.put("state", processInstanceBean.getInstanceState());
        requiredAttr.put("startTime", processInstanceBean.getBeginDate() == null ? "" : formatDatetoString(processInstanceBean.getBeginDate()));
        requiredAttr.put("endTime", processInstanceBean.getEndDate() == null ? "" : formatDatetoString(processInstanceBean.getEndDate()));

        //实体属性
        Map<String, String> entityInnerAttr = new HashMap<String, String>();
        G6Process.put("entityInnerAttr", entityInnerAttr);
        if (instanceEntityAttrBeans != null) {
            for (InstanceEntityAttrBean instanceEntityAttrBean : instanceEntityAttrBeans) {
                String attrCode = instanceEntityAttrBean.getAttrCode();
                String attrValue = "";
                if (instanceEntityAttrBean.getAttrResult() != null) {
                    attrValue = instanceEntityAttrBean.getAttrResult();
                }
                entityInnerAttr.put(attrCode, attrValue);
            }
        }

        //流程实例动态属性
        Map<String, Object> dynamicAttr = new HashMap<String, Object>();
        G6Process.put("dynamicAttr", dynamicAttr);
        dynamicAttr.put("instanceId", processInstanceBean.getInstanceId());
        dynamicAttr.put("instanceCode", processInstanceBean.getInstanceCode());
        dynamicAttr.put("instanceTitle", processInstanceBean.getInstanceTitle());
        dynamicAttr.put("processVersion", processInstanceBean.getProcessVersion());
        dynamicAttr.put("businessId", processInstanceBean.getBusinessId());
        dynamicAttr.put("businessCode", processInstanceBean.getBusinessCode());
        dynamicAttr.put("businessName", processInstanceBean.getBusinessName());
        dynamicAttr.put("appId", processInstanceBean.getAppId());
        dynamicAttr.put("appCode", processInstanceBean.getAppCode());
        dynamicAttr.put("entityId", processInstanceBean.getEntityId());
        dynamicAttr.put("entityCode", processInstanceBean.getEntityCode());

        DeployPropertyBean insFormTypeBean = getInsProperty(deployPropertyBeans, ProcessPropertyEnum.P_FORM_TYPE.getCode());
        if (insFormTypeBean == null) {
            dynamicAttr.put("formType", null);
        } else {
            dynamicAttr.put("formType", insFormTypeBean.getPropertyValue());
            DeployPropertyBean insFormIdBean = getInsProperty(deployPropertyBeans, insFormTypeBean.getPropertyValue());
            if (insFormIdBean == null) {
                dynamicAttr.put("formId", "");
            } else {
                dynamicAttr.put("formId", insFormIdBean.getPropertyValue());
            }
        }

        DeployPropertyBean insSubmitterBean = getInsProperty(deployPropertyBeans, ProcessPropertyEnum.P_SUBMITTER_VAR.getCode());
        if (insSubmitterBean == null) {
            dynamicAttr.put("submitter", "");
        } else {
            dynamicAttr.put("submitter", insSubmitterBean.getPropertyValue());
        }

        List<Map<String, Object>> lines = (List<Map<String, Object>>) G6Process.get("edges");
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) G6Process.get("nodes");

        if (lines != null) {
            for (Map<String, Object> line : lines) {
                Map<String, Object> data = (Map<String, Object>) line.get("data");

                String id = (String) data.get("id");
                TransStateBean transStateBean = getTransStatusBean(transStateBeans, processInstanceBean.getInstanceId(), id);

                if (transStateBean != null) {

                    Map<String, Object> lineRequiredAttr = new HashMap<String, Object>();

                    data.put("requiredAttr", lineRequiredAttr);

                    Map<String, String> startTime = new HashMap<String, String>();
                    startTime.put("code", "startTime");
                    startTime.put("name", XipUtil.getMessage("XIP_WF_COMMON_0044", null));
                    startTime.put("value", transStateBean.getCreationDate() == null ? "" : formatDatetoString(transStateBean.getCreationDate()));
                    lineRequiredAttr.put("startTime", startTime);

                    Map<String, String> endTime = new HashMap<String, String>();
                    endTime.put("code", "endTime");
                    endTime.put("name", XipUtil.getMessage("XIP_WF_COMMON_0045", null));
                    endTime.put("value", transStateBean.getCreationDate() == null ? "" : formatDatetoString(transStateBean.getCreationDate()));
                    lineRequiredAttr.put("endTime", endTime);

                    Map<String, String> state = new HashMap<String, String>();
                    state.put("code", "state");
                    state.put("name", XipUtil.getMessage("XIP_WF_COMMON_0046", null));
                    state.put("value", transStateBean.getTransitionState());
                    lineRequiredAttr.put("state", state);

                }
            }
        }

        if (nodes != null) {
            for (Map<String, Object> node : nodes) {
                Map<String, Object> data = (Map<String, Object>) node.get("data");

                String id = (String) data.get("id");
                ActivityStateBean activityStateBean = getActivityStatusBean(activityStateBeans, processInstanceBean.getInstanceId(), id);
                if (activityStateBean != null) {
                    Map<String, Object> nodeRequiredAttr = new HashMap<String, Object>();

                    data.put("requiredAttr", nodeRequiredAttr);

                    Map<String, String> startTime = new HashMap<String, String>();
                    startTime.put("code", "startTime");
                    startTime.put("name", XipUtil.getMessage("XIP_WF_COMMON_0044", null));
                    startTime.put("value", activityStateBean.getBeginDate() == null ? "" : formatDatetoString(activityStateBean.getBeginDate()));
                    nodeRequiredAttr.put("startTime", startTime);

                    Map<String, String> endTime = new HashMap<String, String>();
                    endTime.put("code", "endTime");
                    endTime.put("name", XipUtil.getMessage("XIP_WF_COMMON_0045", null));
                    endTime.put("value", activityStateBean.getEndDate() == null ? "" : formatDatetoString(activityStateBean.getEndDate()));
                    nodeRequiredAttr.put("endTime", endTime);

                    Map<String, String> state = new HashMap<String, String>();
                    state.put("code", "state");
                    state.put("name", XipUtil.getMessage("XIP_WF_COMMON_0046", null));
                    if (WorkFlowConstants.NODE_START_STATE.equals(activityStateBean.getActState())) {
                        state.put("desc", XipUtil.getMessage("XIP_WF_COMMON_0049", null));
                    } else if (WorkFlowConstants.NODE_RUNNING_STATE.equals(activityStateBean.getActState())) {
                        state.put("desc", XipUtil.getMessage("XIP_WF_COMMON_0050", null));
                    } else if (WorkFlowConstants.NODE_COMPLETED_STATE.equals(activityStateBean.getActState())) {
                        state.put("desc", XipUtil.getMessage("XIP_WF_COMMON_0051", null));
                    } else if (WorkFlowConstants.NODE_CANCELED_STATE.equals(activityStateBean.getActState())) {
                        state.put("desc", XipUtil.getMessage("XIP_WF_COMMON_0052", null));
                    } else if (WorkFlowConstants.NODE_TERMINATE_STATE.equals(activityStateBean.getActState())) {
                        state.put("desc", XipUtil.getMessage("XIP_WF_COMMON_0053", null));
                    }
                    state.put("value", activityStateBean.getActState());
                    nodeRequiredAttr.put("state", state);

                    String activityType = (String) data.get("activityType");

                    if (WorkFlowConstants.NOTICE_NODE.equals(activityType) ||
                            WorkFlowConstants.COUNTERSIGN_NODE.equals(activityType) ||
                            WorkFlowConstants.RESPONSE_NODE.equals(activityType) ||
                            "CopyEmail".equals(activityType) ||
                            "Sign".equals(activityType) ||
                            "Func".equals(activityType)) {

                        Map<String, Object> nodeDynamicAttr = new HashMap<String, Object>();
                        data.put("dynamicAttr", nodeDynamicAttr);

                        Map<String, String> formType = new HashMap<String, String>();
                        formType.put("code", "formType");
                        formType.put("name", XipUtil.getMessage("XIP_WF_COMMON_0054", null));
                        ActivityPropertyBean actFormType = getActivityPropertyBean(processInstanceBean.getInstanceId(), id, WorkFlowConstants.A_FORM_TYPE);
                        if (actFormType != null && StringUtils.isNotBlank(actFormType.getPropertyValue())) {
                            formType.put("value", actFormType.getPropertyValue());
                            Map<String, String> formId = new HashMap<String, String>();
                            formId.put("code", "formId");
                            formId.put("name", XipUtil.getMessage("XIP_WF_COMMON_0055", null));
                            ActivityPropertyBean actFormIdBean = getActivityPropertyBean(processInstanceBean.getInstanceId(), id, actFormType.getPropertyValue());
                            if (actFormIdBean != null && StringUtils.isNotBlank(actFormIdBean.getPropertyValue())) {
                                formId.put("value", actFormIdBean.getPropertyValue());
                            } else {
                                formId.put("value", "");
                            }
                            nodeDynamicAttr.put("formId", formId);
                        } else {
                            formType.put("value", "");
                        }
                        nodeDynamicAttr.put("formType", formType);

                        Map<String, String> executorType = new HashMap<String, String>();
                        executorType.put("code", "executorType");
                        executorType.put("name", XipUtil.getMessage("XIP_WF_COMMON_0056", null));
                        ActivityPropertyBean actExecutorTypeBean = getActivityPropertyBean(processInstanceBean.getInstanceId(), id, WorkFlowConstants.A_EXECUTOR_TYPE);
                        if (actExecutorTypeBean != null && StringUtils.isNotBlank(actExecutorTypeBean.getPropertyValue())) {
                            executorType.put("value", actExecutorTypeBean.getPropertyValue());
                            Map<String, String> executor = new HashMap<String, String>();
                            executor.put("code", "executor");
                            executor.put("name", XipUtil.getMessage("XIP_WF_COMMON_0057", null));
                            ActivityPropertyBean actExecutorBean = getActivityPropertyBean(processInstanceBean.getInstanceId(), id, actExecutorTypeBean.getPropertyValue());
                            if (actExecutorBean != null && StringUtils.isNotBlank(actExecutorBean.getPropertyValue())) {
                                executor.put("value", actExecutorBean.getPropertyValue());
                            } else {
                                executor.put("value", "");
                            }
                            nodeDynamicAttr.put("executor", executor);
                        } else {
                            executorType.put("value", "");
                        }
                        nodeDynamicAttr.put("executorType", executorType);

                        Map<String, String> title = new HashMap<String, String>();
                        title.put("code", "title");
                        title.put("name", XipUtil.getMessage("XIP_WF_COMMON_0058", null));
                        ActivityPropertyBean actTitleBean = getActivityPropertyBean(processInstanceBean.getInstanceId(), id, WorkFlowConstants.A_TITLE);
                        if (actTitleBean != null && StringUtils.isNotBlank(actTitleBean.getPropertyValue())) {
                            title.put("value", actTitleBean.getPropertyValue());
                        } else {
                            title.put("value", "");
                        }
                        nodeDynamicAttr.put("title", executorType);
                    }
                }
            }
        }
        return G6Process;
    }

    public String formatDatetoString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(date);
        return str;
    }

    public DeployPropertyBean getInsProperty(List<DeployPropertyBean> insPropertyList, String propertyCode) {
        if (insPropertyList != null) {
            for (DeployPropertyBean DeployPropertyBean : insPropertyList) {
                if (propertyCode.equals(DeployPropertyBean.getPropertyCode())) {
                    return DeployPropertyBean;
                }
            }
        }
        return null;
    }

    public TransStateBean getTransStatusBean(List<TransStateBean> transStateBeans, String instanceId, String transitionId) {
        if (transStateBeans != null) {
            for (TransStateBean transStateBean : transStateBeans) {
                if (instanceId.equals(transStateBean.getInstanceId()) && transitionId.equals(transStateBean.getTransitionId())) {
                    return transStateBean;
                }
            }
        }
        return null;
    }

    public ActivityStateBean getActivityStatusBean(List<ActivityStateBean> activityStateBeans, String instanceId, String actId) {
        if (activityStateBeans != null) {
            for (ActivityStateBean activityStateBean : activityStateBeans) {
                if (instanceId.equals(activityStateBean.getInstanceId()) && actId.equals(activityStateBean.getActId())) {
                    return activityStateBean;
                }
            }
        }
        return null;
    }

    /**
     * getActivityPropertyBean: 获取流程实例节点属性
     *
     * @param instanceId
     * @param actId
     * @param propertyCode
     * @return
     * @throws Exception
     * @author asus
     */
    public ActivityPropertyBean getActivityPropertyBean(String instanceId, String actId, String propertyCode) throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("instanceId", instanceId);
        param.put("actId", actId);
        param.put("propertyCode", propertyCode);

        ActivityPropertyBean ActivityPropertyBean = processDeployActivityPropertyMapper.getInstanceSingleActivityPropertyBean(param);

        return ActivityPropertyBean;
    }
}
