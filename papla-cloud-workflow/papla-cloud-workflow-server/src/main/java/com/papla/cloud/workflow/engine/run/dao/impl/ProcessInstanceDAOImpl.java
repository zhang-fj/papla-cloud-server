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
 * @Description: 流程实例数据操作接口层实现类
 * @date 2015年4月23日 下午1:43:16
 */
@Repository("processInstanceDAO")
@RequiredArgsConstructor
public class ProcessInstanceDAOImpl implements ProcessInstanceDAO {

    private final ApplicationMapper applicationMapper;                                    // 应用信息Mapper


    /*---------------------------------------流程实体-------------------------------------------*/
    private final EntityMapper entityMapper;                                                // 业务实体Mapper

    private final EntityBizStatusEnumMapper entityBizStatusEnumMapper;                    // 业务状态

    /*=======================================流程部署===========================================*/
    private final ProcessDeployMapper processDeployMapper;                                //流程部署Mapper
    private final ProcessDeployActivityMapper processDeployActivityMapper;                //流程部署节点Mapper
    private final ProcessDeployPropertyMapper processDeployPropertyMapper;                //流程部署属性Mapper
    private final ProcessDeployConditionGroupMapper processDeployConditionGroupMapper;    // 流程部署属性条件组Mapper
    private final ProcessDeployConditionMapper processDeployConditionMapper;                // 流程部署属性条件单元Mapper

    /*=======================================流程运行===========================================*/
    private final RunInstanceMapper runInstanceMapper;                                    // 流程实例Mapper
    private final RunInsActivityStateMapper runInsActivityStateMapper;                                // 流程实例节点状态Mapper
    private final RunInsEntityAttrValueMapper runInsEntityAttrValueMapper;                // 流程实例实体属性Mapper
    private final RunInsTransitionStateMapper runInsTransitionStateMapper;                // 流程实例连线状态Mapper
    private final ActivityDAO activityDAO;
    private final WorkItemDAO workItemDAO;

    @Resource
    private ProcessEngineCommonService processEngineCommonService;

    // 日志记录器
    private static final Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);


    @Override
    public String getDeployIdByProcessCode(String processCode) throws Exception {
        return processDeployMapper.getProcessDeployIdByProcessCode(processCode);
    }

    @Override
    public ProcessInstanceBean createProcessInstance(ProcessInstanceBean bean) throws Exception {
        try {
            // 获取当前时间和当前登录人
            Date date = CurrentUserUtil.getCurrentDate();

            String currentUserId = CurrentUserUtil.getCurrentUserId();
            // 设置创建时间、创建人、更新时间、更新人
            bean.setWhoForInsert(currentUserId);

            // 设置当前用户
            bean.setCurrentUserId(currentUserId);
            bean.setCurrentDate(date);
            // 设置实例为启动状态
            bean.setInstanceState(WorkFlowConstants.INS_START_STATE);
            // 设置实例开始时间
            bean.setBeginDate(date);

            // 取得起草的业务状态信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("entityId", bean.getEntityId());
            params.put("category", WorkFlowConstants.BIZ_STATUS_A);
            List<BizStatusEnumBean> bizStatusList = entityBizStatusEnumMapper.getEntityBizStatusList(params);

            if (bizStatusList != null && bizStatusList.size() > 0) {
                bean.setCurrentBizState(bizStatusList.get(0).getEnumId());
            }

            // 1.创建流程实例 : [PAPLA_WF_RUN_INSTANCE]
            try {
                runInstanceMapper.saveInstance(bean);
            } catch (Exception e) {
                throw new Exception("method：【runInstanceMapper.saveInstance】" + e.getMessage());
            }

            // 保存内置业务实体属性
            try {
                List<InstanceEntityAttrBean> attrList = this.setInsInnerEntityAttr(bean.getProcessName(), bean.getInstanceCode(), bean.getInstanceId(), bean.getEntityId(), bean.getBusinessId(), date, currentUserId, false);
                runInsEntityAttrValueMapper.saveInnerEntityAttrValue(attrList);
            } catch (Exception e) {
                throw new Exception("method：【runInsEntityAttrValueMapper.saveInnerEntityAttr】" + e.getMessage());
            }

            // 2.启动流程节点 : [XIP_WF_INS_ACT_STATE]
            try {
                if (!bean.isExistsLoop()) {
                    runInsActivityStateMapper.saveActivityState(bean);
                }
            } catch (Exception e) {
                throw new Exception("method：【RunInsActStateMapper.saveActState】" + e.getMessage());
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
            // 当前时间和当前用户ID
            Date date = CurrentUserUtil.getCurrentDate();
            String currentUserId = CurrentUserUtil.getCurrentUserId();

            // 设置实例更新时间，更新人
            bean.setWhoForUpdate(currentUserId);

            // 设置当前用户，当前时间
            bean.setCurrentUserId(currentUserId);
            bean.setCurrentDate(date);

            // 设置实例状态为启动状态
            bean.setInstanceState(WorkFlowConstants.INS_START_STATE);

            // 设置实例开始时间
            bean.setBeginDate(date);


            //获取当前节点是否绑定业务表单
            Map<String, String> map = new HashMap<String, String>();
            map.put("deployId", bean.getDeployId());
            map.put("category", WorkFlowConstants.START_NODE);
            map.put("propCode", WorkFlowConstants.A_FORM);
            String isBoundForm = processDeployActivityMapper.isBoundForm(map);

            // 如果为流程驱动模式, 则需将流程实例对应的业务状态 设置为起草状态
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

            // 清除当前流程实例配置信息
            this.clearInstanceConfigInfo(bean.getInstanceId());


            // 1.更新流程实例 基本信息
            try {
                runInstanceMapper.modifyInsBaseInfo(bean);
            } catch (Exception e) {
                throw new Exception("method：【runInstanceMapper.modifyInsBaseInfo】" + e.getMessage());
            }

            // 保存内置业务实体属性
            try {
                List<InstanceEntityAttrBean> attrList = this.setInsInnerEntityAttr(bean.getProcessName(), bean.getInstanceCode(), bean.getInstanceId(), bean.getEntityId(), bean.getBusinessId(), date, currentUserId, true);
                runInsEntityAttrValueMapper.saveInnerEntityAttrValue(attrList);
            } catch (Exception e) {
                throw new Exception("method：【runInsEntityAttrValueMapper.saveInnerEntityAttr】" + e.getMessage());
            }

            // 4.启动流程节点
            try {
                if (!bean.isExistsLoop()) {
                    runInsActivityStateMapper.saveActivityState(bean);
                }
            } catch (Exception e) {
                throw new Exception("method：【RunInsActStateMapper.saveActState】" + e.getMessage());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return bean;
    }

    /**
     * @param insId
     * @return void    返回类型
     * @throws Exception
     * @Title: clearInstanceConfigInfo
     * @Description: 清除当前流程实例配置信息
     */
    private void clearInstanceConfigInfo(String insId) throws Exception {
        try {

            // 1. 删除流程连线状态
            runInsTransitionStateMapper.clearInsTransStateByInsId(insId);

            // 2. 删除流程节点状态
            runInsActivityStateMapper.deleteActivityStateByInstanceId(insId);

            // 3. 删除业务实体属性
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
     * @return List<InstanceEntityAttrBean>    返回类型
     * @throws Exception
     * @Title: setInsInnerEntityAttr
     * @Description: 获取流程实例内置实体属性
     */
    private List<InstanceEntityAttrBean> setInsInnerEntityAttr(String processName, String instanceCode,
                                                               String instanceId, String entityId, String businessId, Date date, String userId, boolean isInit) throws Exception {

        List<InstanceEntityAttrBean> attrBeanList = new ArrayList<InstanceEntityAttrBean>();

        // 获取内置属性值集
        List<PubValSetBean> beans = runInsEntityAttrValueMapper.getInnerEntityAttrs(PlatformConstants.WF_INNER_ATTR_VS);

        if (beans != null && beans.size() > 0) {
            for (PubValSetBean bean : beans) {
                // 属性编码
                String attrCode = bean.getDtlCode();

                // 去除设置提交人 , 设置提交人姓名, 设置提交时间
                if ((attrCode.equals(WorkFlowConstants.E_SUBMITTER) && isInit)
                        || (attrCode.equals(WorkFlowConstants.E_SUBMITTER_NAME) && isInit)
                        || (attrCode.equals(WorkFlowConstants.E_SUBMIT_DATE) && isInit)) {
                    continue;
                }

                // 创建实例实体属性信息
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

                // 流程引擎对内置属性的赋值处理（参数值由引擎自动生成）
                if (!isInit) {
                    if (attrCode.equals(WorkFlowConstants.E_SUBMITTER)) {    // 设置提交人
                        attrBean.setAttrResult(CurrentUserUtil.getCurrentUserId());
                    }
                    if (attrCode.equals(WorkFlowConstants.E_SUBMITTER_NAME)) { //  设置提交人姓名
                        attrBean.setAttrResult(CurrentUserUtil.getEmpName());
                    }
                    if (attrCode.equals(WorkFlowConstants.E_SUBMIT_DATE)) {    // 设置提交时间
                        attrBean.setAttrResult(CurrentUserUtil.getCurrentDateStr());
                    }
                    if (attrCode.equals(WorkFlowConstants.E_INSTANCE_CODE)) {    // 设置流程实例编码
                        attrBean.setAttrResult(instanceCode);
                    }
                    if (attrCode.equals(WorkFlowConstants.E_PROCESS_NAME)) {    // 设置流程名称
                        attrBean.setAttrResult(processName);
                    }
                    if (attrCode.equals(WorkFlowConstants.E_BUSINESS_ID)) {    // 设置流程名称
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
     * (非 Javadoc)
     * <p>Title: saveInsEntityAttrValue</p>
     * <p>Description: 保存流程实例实体属性值 </p>
     * @param entityAttrValueList
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ProcessInstanceDAO#saveInsEntityAttrValue(java.util.List)
     */
    public void saveInsEntityAttrValue(List<InstanceEntityAttrBean> entityAttrValueList) throws Exception {
        try {
            // 取得当前日期和当前用户Id
            Date date = CurrentUserUtil.getCurrentDate();
            String userId = CurrentUserUtil.getCurrentUserId();

            // 循环更新流程实例实体属性
            if (entityAttrValueList != null && entityAttrValueList.size() > 0) {
                for (InstanceEntityAttrBean bean : entityAttrValueList) {
                    // 设置最后更新日期和最后更新人
                    bean.setUpdateDt(date);
                    bean.setUpdateBy(userId);
                    // 执行更新处理
                    runInsEntityAttrValueMapper.updateInsEntityAttrValue(bean);
                }
            }
        } catch (Exception e) {
            log.error("【runInsEntityAttrValueMapper.updateInsEntityAttrValue】 >> " + e.getMessage());
            throw e;
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: updateInstanceState</p>
     * <p>Description: 更新流程实例状态 </p>
     * @param bean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ProcessInstanceDAO#updateInstanceState(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public void updateInstanceState(ProcessInstanceBean bean) throws Exception {
        try {

            // 封装参数
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
     * (非 Javadoc)
     * <p>Title: updateInstance</p>
     * <p>Description: 更新流程实例信息 </p>
     * @param bean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ProcessInstanceDAO#updateInstance(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public void updateInstanceBusiness(ProcessInstanceBean bean) throws Exception {

        try {
            // 封装参数
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
     * (非 Javadoc)
     * <p>Title: updateBizState</p>
     * <p>Description: 更新流程实例业务状态, 并更新业务系统的状态信息 </p>
     * @param bean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ProcessInstanceDAO#updateBizState(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public void updateBizState(ProcessInstanceBean bean) throws Exception {

        if (bean.getCurrentBizState() == null) return;

        try {

            Map<String, Object> params = new HashMap<String, Object>();
            /*======================================
             * 更新流程实例业务状态信息
             * ====================================*/
            HashMap<String, Object> hashmap = new HashMap<String, Object>();
            hashmap.put("instanceId", bean.getInstanceId());
            hashmap.put("bizStatus", bean.getCurrentBizState());
            hashmap.put("updateDt", CurrentUserUtil.getCurrentDate());
            hashmap.put("updateBy", CurrentUserUtil.getCurrentUserId());

            runInstanceMapper.updateBizState(hashmap);


            /*======================================
             * 执行回调处理，更新业务系统中数据信息
             * ====================================*/

            // 取得应用信息
            if (bean.getAppBean() == null) {
                bean.setAppBean(applicationMapper.getApplicationByProcId(bean.getProcessId()));
            }
            // 取得业务实体信息
            EntityBean entityBean = bean.getEntityBean();
            if (entityBean == null) {
                entityBean = entityMapper.getEntityByInstanceId(bean.getInstanceId());
                bean.setEntityBean(entityBean);
            }
            // 取得流程实例实体属性信息
            if (bean.getAttrList() == null) {
                params.clear();
                params.put("instanceId", bean.getInstanceId());
                bean.setAttrList(runInsEntityAttrValueMapper.getInstanceEntityAttrValueList(params));
            }
            // 设置业务状态信息
            if (bean.getBizStatusList() == null) {
                params.clear();
                params.put("entityId", entityBean.getEntityId());
                bean.setBizStatusList(entityBizStatusEnumMapper.getEntityBizStatusList(params));
            }

            // 设置当前业务状态信息
            List<InstanceEntityAttrBean> attrList = bean.getAttrList();
            for (int i = 0; i < attrList.size(); i++) {
                InstanceEntityAttrBean attrBean = attrList.get(i);

                // 状态分类ID
                if (WorkFlowConstants.E_CURRENT_BIZ_STATUS.equals(attrBean.getAttrCode())) {
                    for (BizStatusEnumBean bizBean : bean.getBizStatusList()) {
                        if (bizBean.getEnumId().equals(bean.getCurrentBizState())) {
                            attrBean.setAttrResult(bizBean.getStatusCode());
                            attrList.set(i, attrBean);
                            break;
                        }
                    }
                }
                // 状态分类编码
                if (WorkFlowConstants.E_CURRENT_BIZ_STATUS_CAT.equals(attrBean.getAttrCode())) {
                    for (BizStatusEnumBean bizBean : bean.getBizStatusList()) {
                        if (bizBean.getEnumId().equals(bean.getCurrentBizState())) {
                            attrBean.setAttrResult(bizBean.getStatusCateGory());
                            attrList.set(i, attrBean);
                            break;
                        }
                    }
                }
                // 状态分类描述
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

            // 执行回调函数
            String funcType = entityBean.getFuncType();    // 回调函数类型：PROC-存储过程, WEB - WebService服务, JAVA- JAVA方法
            String funcValue = entityBean.getFuncValue();    // 回调函数

            if (WorkFlowConstants.FUNC_TYPE_PROC.equals(funcType) || WorkFlowConstants.FUNC_TYPE_WEB.equals(funcType) || WorkFlowConstants.FUNC_TYPE_JAVA.equals(funcType)) {
                // 执行回调处理
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
        // 初始化处理
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
        // 初始化处理
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

        // 1.获取流程实例信息
        bean = getRunBaseInstanceBeanById(instanceId);

        // 2.获取实例 - 实体属性信息
        params.clear();
        params.put("instanceId", bean.getInstanceId());
        bean.setAttrList(runInsEntityAttrValueMapper.getInstanceEntityAttrValueList(params));


        // 3.获取流程实例属性信息
        List<DeployPropertyBean> propertyList = processDeployPropertyMapper.getPropertyListByDeployId(bean.getDeployId());
        bean.setDeployPropertyList(propertyList);
        // 转化为Map对象
        bean.setPropertyMap(PropertyHandlerUtil.propertyListToMap(propertyList));

        // 4.获取流程实例节点及其属性信息
        List<ActivityBean> actList = activityDAO.getInsActivityList(instanceId);
        bean.setActivityBeanList(actList);

        // 5.流程实例节点状态信息
        List<ActivityStateBean> actStateList = runInsActivityStateMapper.getActivityStateList(params);
        bean.setActStateList(actStateList);

        // 6.流程实例连线状态信息
        bean.setTransStateList(runInsTransitionStateMapper.getTransStateList(params));

        // 7.流程实例属性中所涉及表达式信息
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

        // 8.流程实例-待办信息
        List<WorkItemBean> workItemList = workItemDAO.getRunWorkItemListByInstanceId(instanceId);
        bean.setWorkItemList(workItemList);

        // 9.获取流程对应的应用信息
        bean.setAppBean(applicationMapper.getApplicationByProcId(bean.getProcessId()));

        // 10.获取流程对应的业务信息
        bean.setEntityBean(entityMapper.getEntityByInstanceId(instanceId));

        // 11.获取业务状态信息
        params.clear();
        params.put("entityId", bean.getEntityId());
        bean.setBizStatusList(entityBizStatusEnumMapper.getEntityBizStatusList(params));

        // 返回流程实例Bean
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
                // 如果未指定实体属性数据类型则默认为字符型
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
