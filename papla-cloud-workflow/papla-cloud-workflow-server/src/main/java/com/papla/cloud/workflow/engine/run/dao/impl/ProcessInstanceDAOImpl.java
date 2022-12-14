package com.papla.cloud.workflow.engine.run.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.papla.cloud.workflow.util.PlatformConstants;
import com.papla.cloud.workflow.util.PlatformUtil;
import com.papla.cloud.workflow.util.XipUtil;
import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.mapper.ApplicationMapper;
import com.papla.cloud.workflow.engine.mapper.EntityBizStatusEnumMapper;
import com.papla.cloud.workflow.engine.mapper.EntityMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActivityMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployConditionGroupMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployConditionMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployPropertyMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsActivityStateMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsEntityAttrValueMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsTransitionStateMapper;
import com.papla.cloud.workflow.engine.mapper.RunInstanceMapper;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.modal.BizStatusEnumBean;
import com.papla.cloud.workflow.engine.modal.ConditionBean;
import com.papla.cloud.workflow.engine.modal.ConditionGroupBean;
import com.papla.cloud.workflow.engine.modal.DeployPropertyBean;
import com.papla.cloud.workflow.engine.modal.EntityBean;
import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.PubValSetBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;
import com.papla.cloud.workflow.engine.run.dao.ProcessInstanceDAO;
import com.papla.cloud.workflow.engine.run.dao.WorkItemDAO;

import javax.annotation.Resource;

/**
 * @author linpeng
 * @ClassName: ProcessInstanceDAOImpl
 * @Description: ??????????????????????????????????????????
 * @date 2015???4???23??? ??????1:43:16
 */
@Repository("processInstanceDAO")
@RequiredArgsConstructor
public class ProcessInstanceDAOImpl implements ProcessInstanceDAO {

    private final ApplicationMapper applicationMapper;                                    // ????????????Mapper


    /*---------------------------------------????????????-------------------------------------------*/
    private final EntityMapper entityMapper;                                                // ????????????Mapper

    private final EntityBizStatusEnumMapper entityBizStatusEnumMapper;                    // ????????????

    /*=======================================????????????===========================================*/
    private final ProcessDeployMapper processDeployMapper;                                //????????????Mapper
    private final ProcessDeployActivityMapper processDeployActivityMapper;                //??????????????????Mapper
    private final ProcessDeployPropertyMapper processDeployPropertyMapper;                //??????????????????Mapper
    private final ProcessDeployConditionGroupMapper processDeployConditionGroupMapper;    // ???????????????????????????Mapper
    private final ProcessDeployConditionMapper processDeployConditionMapper;                // ??????????????????????????????Mapper

    /*=======================================????????????===========================================*/
    private final RunInstanceMapper runInstanceMapper;                                    // ????????????Mapper
    private final RunInsActivityStateMapper runInsActivityStateMapper;                                // ????????????????????????Mapper
    private final RunInsEntityAttrValueMapper runInsEntityAttrValueMapper;                // ????????????????????????Mapper
    private final RunInsTransitionStateMapper runInsTransitionStateMapper;                // ????????????????????????Mapper
    private final ActivityDAO activityDAO;
    private final WorkItemDAO workItemDAO;

    @Resource
    private ProcessEngineCommonService processEngineCommonService;

    // ???????????????
    private static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    @Override
    public String getDeployIdByProcessCode(String processCode) throws Exception {
        return processDeployMapper.getProcessDeployIdByProcessCode(processCode);
    }

    @Override
    public ProcessInstanceBean createProcessInstance(ProcessInstanceBean bean) throws Exception {
        try {
            // ????????????????????????????????????
            Date date = CurrentUserUtil.getCurrentDate();

            String currentUserId = CurrentUserUtil.getCurrentUserId();
            // ?????????????????????????????????????????????????????????
            bean.setWhoForInsert(currentUserId);

            // ??????????????????
            bean.setCurrentUserId(currentUserId);
            bean.setCurrentDate(date);
            // ???????????????????????????
            bean.setInstanceState(WorkFlowConstants.INS_START_STATE);
            // ????????????????????????
            bean.setBeginDate(date);

            // ?????????????????????????????????
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("entityId", bean.getEntityId());
            params.put("category", WorkFlowConstants.BIZ_STATUS_A);
            List<BizStatusEnumBean> bizStatusList = entityBizStatusEnumMapper.getEntityBizStatusList(params);

            if (bizStatusList != null && bizStatusList.size() > 0) {
                bean.setCurrentBizState(bizStatusList.get(0).getEnumId());
            }

            // 1.?????????????????? : [PAPLA_WF_RUN_INSTANCE]
            try {
                runInstanceMapper.saveInstance(bean);
            } catch (Exception e) {
                throw new Exception("method??????runInstanceMapper.saveInstance???" + e.getMessage());
            }

            // ??????????????????????????????
            try {
                List<InstanceEntityAttrBean> attrList = this.setInsInnerEntityAttr(bean.getProcessName(), bean.getInstanceCode(), bean.getInstanceId(), bean.getEntityId(), bean.getBusinessId(), date, currentUserId, false);
                runInsEntityAttrValueMapper.saveInnerEntityAttrValue(attrList);
            } catch (Exception e) {
                throw new Exception("method??????runInsEntityAttrValueMapper.saveInnerEntityAttr???" + e.getMessage());
            }

            // 2.?????????????????? : [XIP_WF_INS_ACT_STATE]
            try {
                if (!bean.isExistsLoop()) {
                    runInsActivityStateMapper.saveActivityState(bean);
                }
            } catch (Exception e) {
                throw new Exception("method??????RunInsActStateMapper.saveActState???" + e.getMessage());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return bean;
    }

    @Override
    public ProcessInstanceBean initProcessInstance(ProcessInstanceBean bean) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            // ???????????????????????????ID
            Date date = CurrentUserUtil.getCurrentDate();
            String currentUserId = CurrentUserUtil.getCurrentUserId();

            // ????????????????????????????????????
            bean.setWhoForUpdate(currentUserId);

            // ?????????????????????????????????
            bean.setCurrentUserId(currentUserId);
            bean.setCurrentDate(date);

            // ?????????????????????????????????
            bean.setInstanceState(WorkFlowConstants.INS_START_STATE);

            // ????????????????????????
            bean.setBeginDate(date);


            //??????????????????????????????????????????
            Map<String, String> map = new HashMap<String, String>();
            map.put("deployId", bean.getDeployId());
            map.put("category", WorkFlowConstants.START_NODE);
            map.put("propCode", WorkFlowConstants.A_FORM);
            String isBoundForm = processDeployActivityMapper.isBoundForm(map);

            // ???????????????????????????, ?????????????????????????????????????????? ?????????????????????
            if (isBoundForm != null && !"".equals(isBoundForm)) {
                params.clear();
                params.put("entityId", bean.getEntityId());
                params.put("category", WorkFlowConstants.BIZ_STATUS_A);
                List<BizStatusEnumBean> bizStatusList = entityBizStatusEnumMapper.getEntityBizStatusList(params);

                bean.setBizStatusList(bizStatusList);

                if (bizStatusList != null && bizStatusList.size() > 0) {
                    bean.setCurrentBizState(bizStatusList.get(0).getEnumId());
                }
            }

            // ????????????????????????????????????
            this.clearInstanceConfigInfo(bean.getInstanceId());


            // 1.?????????????????? ????????????
            try {
                runInstanceMapper.modifyInsBaseInfo(bean);
            } catch (Exception e) {
                throw new Exception("method??????runInstanceMapper.modifyInsBaseInfo???" + e.getMessage());
            }

            // ??????????????????????????????
            try {
                List<InstanceEntityAttrBean> attrList = this.setInsInnerEntityAttr(bean.getProcessName(), bean.getInstanceCode(), bean.getInstanceId(), bean.getEntityId(), bean.getBusinessId(), date, currentUserId, true);
                runInsEntityAttrValueMapper.saveInnerEntityAttrValue(attrList);
            } catch (Exception e) {
                throw new Exception("method??????runInsEntityAttrValueMapper.saveInnerEntityAttr???" + e.getMessage());
            }

            // 4.??????????????????
            try {
                if (!bean.isExistsLoop()) {
                    runInsActivityStateMapper.saveActivityState(bean);
                }
            } catch (Exception e) {
                throw new Exception("method??????RunInsActStateMapper.saveActState???" + e.getMessage());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return bean;
    }

    /**
     * @param insId
     * @return void    ????????????
     * @throws Exception
     * @Title: clearInstanceConfigInfo
     * @Description: ????????????????????????????????????
     */
    private void clearInstanceConfigInfo(String insId) throws Exception {
        try {

            // 1. ????????????????????????
            runInsTransitionStateMapper.clearInsTransStateByInsId(insId);

            // 2. ????????????????????????
            runInsActivityStateMapper.deleteActivityStateByInstanceId(insId);

            // 3. ????????????????????????
            runInsEntityAttrValueMapper.clearEntityAttrByInsId(insId);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param processName
     * @param instanceCode
     * @param instanceId
     * @param entityId
     * @param date
     * @param userId
     * @param isInit
     * @return List<InstanceEntityAttrBean>    ????????????
     * @throws Exception
     * @Title: setInsInnerEntityAttr
     * @Description: ????????????????????????????????????
     */
    private List<InstanceEntityAttrBean> setInsInnerEntityAttr(String processName, String instanceCode,
                                                               String instanceId, String entityId, String businessId, Date date, String userId, boolean isInit) throws Exception {

        List<InstanceEntityAttrBean> attrBeanList = new ArrayList<InstanceEntityAttrBean>();

        // ????????????????????????
        List<PubValSetBean> beans = runInsEntityAttrValueMapper.getInnerEntityAttrs(PlatformConstants.WF_INNER_ATTR_VS);

        if (beans != null && beans.size() > 0) {
            for (PubValSetBean bean : beans) {
                // ????????????
                String attrCode = bean.getDtlCode();

                // ????????????????????? , ?????????????????????, ??????????????????
                if ((attrCode.equals(WorkFlowConstants.E_SUBMITTER) && isInit)
                        || (attrCode.equals(WorkFlowConstants.E_SUBMITTER_NAME) && isInit)
                        || (attrCode.equals(WorkFlowConstants.E_SUBMIT_DATE) && isInit)) {
                    continue;
                }

                // ??????????????????????????????
                InstanceEntityAttrBean attrBean = new InstanceEntityAttrBean();

                attrBean.setInstanceId(instanceId);
                attrBean.setEntityId(entityId);
                attrBean.setAttrId(UUID.randomUUID().toString());
                attrBean.setAttrCode(attrCode);
                attrBean.setAttrName(bean.getDtlName());
                attrBean.setAttrCategory("static");
                attrBean.setCreateDt(date);
                attrBean.setCreateBy(userId);
                attrBean.setUpdateDt(date);
                attrBean.setUpdateBy(userId);
                attrBean.setSysInnerVar("Y");

                // ??????????????????????????????????????????????????????????????????????????????
                if (!isInit) {
                    if (attrCode.equals(WorkFlowConstants.E_SUBMITTER)) {    // ???????????????
                        attrBean.setAttrResult(CurrentUserUtil.getCurrentUserId());
                    }
                    if (attrCode.equals(WorkFlowConstants.E_SUBMITTER_NAME)) { //  ?????????????????????
                        attrBean.setAttrResult(CurrentUserUtil.getEmpName());
                    }
                    if (attrCode.equals(WorkFlowConstants.E_SUBMIT_DATE)) {    // ??????????????????
                        attrBean.setAttrResult(CurrentUserUtil.getCurrentDateStr());
                    }
                    if (attrCode.equals(WorkFlowConstants.E_INSTANCE_CODE)) {    // ????????????????????????
                        attrBean.setAttrResult(instanceCode);
                    }
                    if (attrCode.equals(WorkFlowConstants.E_PROCESS_NAME)) {    // ??????????????????
                        attrBean.setAttrResult(processName);
                    }
                    if (attrCode.equals(WorkFlowConstants.E_BUSINESS_ID)) {    // ??????????????????
                        attrBean.setAttrResult(businessId);
                    }
                }
                attrBeanList.add(attrBean);
            }
        } else {
            throw new Exception(XipUtil.getMessage("XIP_WF_DAO_0005", null));
        }

        return attrBeanList;
    }

    /*
     * (??? Javadoc)
     * <p>Title: saveInsEntityAttrValue</p>
     * <p>Description: ????????????????????????????????? </p>
     * @param entityAttrValueList
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ProcessInstanceDAO#saveInsEntityAttrValue(java.util.List)
     */
    public void saveInsEntityAttrValue(List<InstanceEntityAttrBean> entityAttrValueList) throws Exception {
        try {
            // ?????????????????????????????????Id
            Date date = CurrentUserUtil.getCurrentDate();
            String userId = CurrentUserUtil.getCurrentUserId();

            // ????????????????????????????????????
            if (entityAttrValueList != null && entityAttrValueList.size() > 0) {
                for (InstanceEntityAttrBean bean : entityAttrValueList) {
                    // ??????????????????????????????????????????
                    bean.setUpdateDt(date);
                    bean.setUpdateBy(userId);
                    // ??????????????????
                    runInsEntityAttrValueMapper.updateInsEntityAttrValue(bean);
                }
            }
        } catch (Exception e) {
            log.error("???runInsEntityAttrValueMapper.updateInsEntityAttrValue??? >> " + e.getMessage());
            throw e;
        }
    }

    /*
     * (??? Javadoc)
     * <p>Title: updateInstanceState</p>
     * <p>Description: ???????????????????????? </p>
     * @param bean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ProcessInstanceDAO#updateInstanceState(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public void updateInstanceState(ProcessInstanceBean bean) throws Exception {
        try {

            // ????????????
            HashMap<String, Object> hashmap = new HashMap<String, Object>();
            hashmap.put("instanceId", bean.getInstanceId());
            hashmap.put("instanceState", bean.getInstanceState());
            hashmap.put("endDate", bean.getEndDate());
            hashmap.put("updateDt", CurrentUserUtil.getCurrentDate());
            hashmap.put("updateBy", CurrentUserUtil.getCurrentUserId());
            runInstanceMapper.updateInstanceState(hashmap);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /*
     * (??? Javadoc)
     * <p>Title: updateInstance</p>
     * <p>Description: ???????????????????????? </p>
     * @param bean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ProcessInstanceDAO#updateInstance(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public void updateInstanceBusiness(ProcessInstanceBean bean) throws Exception {

        try {
            // ????????????
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("instanceId", bean.getInstanceId());
            params.put("businessId", bean.getBusinessId());
            params.put("businessCode", bean.getBusinessCode());
            params.put("businessName", bean.getBusinessName());
            params.put("updateDt", CurrentUserUtil.getCurrentDate());
            params.put("updateBy", CurrentUserUtil.getCurrentUserId());

            runInstanceMapper.updateInstanceBusiness(params);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /*
     * (??? Javadoc)
     * <p>Title: updateBizState</p>
     * <p>Description: ??????????????????????????????, ???????????????????????????????????? </p>
     * @param bean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ProcessInstanceDAO#updateBizState(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public void updateBizState(ProcessInstanceBean bean) throws Exception {

        if (bean.getCurrentBizState() == null) return;

        try {

            Map<String, Object> params = new HashMap<String, Object>();
            /*======================================
             * ????????????????????????????????????
             * ====================================*/
            HashMap<String, Object> hashmap = new HashMap<String, Object>();
            hashmap.put("instanceId", bean.getInstanceId());
            hashmap.put("bizStatus", bean.getCurrentBizState());
            hashmap.put("updateDt", CurrentUserUtil.getCurrentDate());
            hashmap.put("updateBy", CurrentUserUtil.getCurrentUserId());

            runInstanceMapper.updateBizState(hashmap);


            /*======================================
             * ??????????????????????????????????????????????????????
             * ====================================*/

            // ??????????????????
            if (bean.getAppBean() == null) {
                bean.setAppBean(applicationMapper.getApplicationByProcId(bean.getProcessId()));
            }
            // ????????????????????????
            EntityBean entityBean = bean.getEntityBean();
            if (entityBean == null) {
                entityBean = entityMapper.getEntityByInstanceId(bean.getInstanceId());
                bean.setEntityBean(entityBean);
            }
            // ????????????????????????????????????
            if (bean.getAttrList() == null) {
                params.clear();
                params.put("instanceId", bean.getInstanceId());
                bean.setAttrList(runInsEntityAttrValueMapper.getInstanceEntityAttrValueList(params));
            }
            // ????????????????????????
            if (bean.getBizStatusList() == null) {
                params.clear();
                params.put("entityId", entityBean.getEntityId());
                bean.setBizStatusList(entityBizStatusEnumMapper.getEntityBizStatusList(params));
            }

            // ??????????????????????????????
            List<InstanceEntityAttrBean> attrList = bean.getAttrList();
            for (int i = 0; i < attrList.size(); i++) {
                InstanceEntityAttrBean attrBean = attrList.get(i);

                // ????????????ID
                if (WorkFlowConstants.E_CURRENT_BIZ_STATUS.equals(attrBean.getAttrCode())) {
                    for (BizStatusEnumBean bizBean : bean.getBizStatusList()) {
                        if (bizBean.getEnumId().equals(bean.getCurrentBizState())) {
                            attrBean.setAttrResult(bizBean.getStatusCode());
                            attrList.set(i, attrBean);
                            break;
                        }
                    }
                }
                // ??????????????????
                if (WorkFlowConstants.E_CURRENT_BIZ_STATUS_CAT.equals(attrBean.getAttrCode())) {
                    for (BizStatusEnumBean bizBean : bean.getBizStatusList()) {
                        if (bizBean.getEnumId().equals(bean.getCurrentBizState())) {
                            attrBean.setAttrResult(bizBean.getStatusCateGory());
                            attrList.set(i, attrBean);
                            break;
                        }
                    }
                }
                // ??????????????????
                if (WorkFlowConstants.E_CURRENT_BIZ_STATUS_DESC.equals(attrBean.getAttrCode())) {
                    for (BizStatusEnumBean bizBean : bean.getBizStatusList()) {
                        if (bizBean.getEnumId().equals(bean.getCurrentBizState())) {
                            attrBean.setAttrResult(bizBean.getStatusDesc());
                            attrList.set(i, attrBean);
                            break;
                        }
                    }
                }
            }

            // ??????????????????
            String funcType = entityBean.getFuncType();    // ?????????????????????PROC-????????????, WEB - WebService??????, JAVA- JAVA??????
            String funcValue = entityBean.getFuncValue();    // ????????????

            if (WorkFlowConstants.FUNC_TYPE_PROC.equals(funcType) || WorkFlowConstants.FUNC_TYPE_WEB.equals(funcType) || WorkFlowConstants.FUNC_TYPE_JAVA.equals(funcType)) {
                // ??????????????????
                if (funcValue != null && !"".equals(funcValue)) {
                    processEngineCommonService.invokeFunc(bean, funcType, funcValue);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ProcessInstanceBean getRunBaseInstanceBeanById(String instanceId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", instanceId);
        return runInstanceMapper.getBaseInstanceBean(params);
    }

    @Override
    public ProcessInstanceBean getProcessInstanceByCode(String instanceCode) throws Exception {
        if (instanceCode == null) throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0046", null));
        // ???????????????
        ProcessInstanceBean bean = getRunBaseInstanceBeanByCode(instanceCode);
        try {
            if (bean == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0046", null));
            } else {
                bean = this.getProcessInstanceById(bean.getInstanceId());
            }
        } catch (Exception e) {
            throw e;
        }
        return bean;
    }

    @Override
    public ProcessInstanceBean getProcessInstanceByTaskId(String taskId) throws Exception {
        if (taskId == null) throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
        // ???????????????
        ProcessInstanceBean bean = getRunBaseInstanceBeanByTaskId(taskId);
        try {
            if (bean == null) {
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0047", null));
            } else {
                bean = this.getProcessInstanceById(bean.getInstanceId());
            }
        } catch (Exception e) {
            throw e;
        }
        return bean;
    }

    @Override
    public ProcessInstanceBean getRunBaseInstanceBeanByCode(String instanceCode) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceCode", instanceCode);
        return runInstanceMapper.getBaseInstanceBean(params);
    }

    @Override
    public ProcessInstanceBean getBaseInstanceBeanByCode(String instanceCode) throws Exception {
        ProcessInstanceBean bean = getRunBaseInstanceBeanByCode(instanceCode);
        if (bean == null) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("arch", "Y");
            params.put("instanceCode", instanceCode);
            bean = runInstanceMapper.getBaseInstanceBean(params);
            bean.setQueryArch(true);
        }
        return bean;
    }

    @Override
    public ProcessInstanceBean getRunBaseInstanceBeanByTaskId(String taskId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("taskId", taskId);
        return runInstanceMapper.getBaseInstanceBean(params);
    }

    @Override
    public ProcessInstanceBean getBaseInstanceBeanByTaskId(String taskId) throws Exception {
        ProcessInstanceBean bean = getRunBaseInstanceBeanByTaskId(taskId);
        if (bean == null) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("arch", "Y");
            params.put("taskId", taskId);
            bean = runInstanceMapper.getBaseInstanceBean(params);
            bean.setQueryArch(true);
        }
        return bean;
    }

    @Override
    public String getRunInstanceCodeById(String instanceId) throws Exception {
        return runInstanceMapper.getRunInstanceCodeById(instanceId);
    }

    public ProcessInstanceBean getProcessInstanceById(String instanceId) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        ProcessInstanceBean bean = new ProcessInstanceBean();

        // 1.????????????????????????
        bean = getRunBaseInstanceBeanById(instanceId);

        // 2.???????????? - ??????????????????
        params.clear();
        params.put("instanceId", bean.getInstanceId());
        bean.setAttrList(runInsEntityAttrValueMapper.getInstanceEntityAttrValueList(params));


        // 3.??????????????????????????????
        List<DeployPropertyBean> propertyList = processDeployPropertyMapper.getPropertyListByDeployId(bean.getDeployId());
        bean.setDeployPropertyList(propertyList);
        // ?????????Map??????
        bean.setPropertyMap(PropertyHandlerUtil.propertyListToMap(propertyList));

        // 4.??????????????????????????????????????????
        List<ActivityBean> actList = activityDAO.getInsActivityList(instanceId);
        bean.setActivityBeanList(actList);

        // 5.??????????????????????????????
        List<ActivityStateBean> actStateList = runInsActivityStateMapper.getActivityStateList(params);
        bean.setActStateList(actStateList);

        // 6.??????????????????????????????
        bean.setTransStateList(runInsTransitionStateMapper.getTransStateList(params));

        // 7.?????????????????????????????????????????????
        HashMap<String, List<ConditionGroupBean>> proExpMap = new HashMap<String, List<ConditionGroupBean>>();
        if (WorkFlowConstants.P_FORM_TYPE.equals(WorkFlowConstants.FORM_TYPE_R)) {
            params.clear();
            params.put("parentId", bean.getDeployId());
            params.put("parentType", "P");
            params.put("propertyCode", WorkFlowConstants.P_ROUTE_FORM);
            List<ConditionGroupBean> groupList = processDeployConditionGroupMapper.getConditionGroupList(params);
            for (int j = 0; j < groupList.size(); j++) {
                ConditionGroupBean cg = groupList.get(j);
                params.clear();
                params.put("groupId", cg.getGroupId());
                params.put("dbType", PlatformUtil.getDbType().toLowerCase());
                List<ConditionBean> insPropList = processDeployConditionMapper.getConditonBeanList(params);
                cg.setConditionList(insPropList);
                groupList.set(j, cg);
            }
            proExpMap.put(WorkFlowConstants.P_ROUTE_FORM, groupList);
        }


        params.clear();
        params.put("parentId", bean.getDeployId());
        params.put("parentType", "P");
        params.put("propertyCode", WorkFlowConstants.P_BOUND_CONDITION);
        List<ConditionGroupBean> conditionGroupList = processDeployConditionGroupMapper.getConditionGroupList(params);
        for (int j = 0; j < conditionGroupList.size(); j++) {
            ConditionGroupBean cgb = conditionGroupList.get(j);
            params.clear();
            params.put("groupId", cgb.getGroupId());
            params.put("dbType", PlatformUtil.getDbType().toLowerCase());
            List<ConditionBean> conditionList = processDeployConditionMapper.getConditonBeanList(params);
            cgb.setConditionList(conditionList);
            conditionGroupList.set(j, cgb);
        }
        proExpMap.put(WorkFlowConstants.P_BOUND_CONDITION, conditionGroupList);

        bean.setProExpMap(proExpMap);

        // 8.????????????-????????????
        List<WorkItemBean> workItemList = workItemDAO.getRunWorkItemListByInstanceId(instanceId);
        bean.setWorkItemList(workItemList);

        // 9.?????????????????????????????????
        bean.setAppBean(applicationMapper.getApplicationByProcId(bean.getProcessId()));

        // 10.?????????????????????????????????
        bean.setEntityBean(entityMapper.getEntityByInstanceId(instanceId));

        // 11.????????????????????????
        params.clear();
        params.put("entityId", bean.getEntityId());
        bean.setBizStatusList(entityBizStatusEnumMapper.getEntityBizStatusList(params));

        // ??????????????????Bean
        return bean;
    }

    ;

    @Override
    public ProcessDeployBean getProcessDeployById(String deployId) throws Exception {
        return processDeployMapper.getProcessDeployById(deployId);
    }

    @Override
    public String getInstanceState(String instanceCode) throws Exception {
        return runInstanceMapper.getInstanceState(instanceCode);
    }

    @Override
    public HashMap<String, String> getInsEntityAttrsDataType(String instanceId) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", instanceId);
        List<InstanceEntityAttrBean> beans = runInsEntityAttrValueMapper.getInstanceEntityAttrValueList(params);
        if (beans != null && beans.size() > 0) {
            for (InstanceEntityAttrBean bean : beans) {
                String key = bean.getAttrCode();
                String value = bean.getAttrDataType();
                // ????????????????????????????????????????????????????????????
                if (value != null && !"".equals(value)) {
                    map.put(key, value);
                } else {
                    map.put(key, "1");
                }
            }
        }
        return map;
    }

}
