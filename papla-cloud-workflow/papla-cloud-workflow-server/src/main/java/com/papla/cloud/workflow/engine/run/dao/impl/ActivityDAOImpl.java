package com.papla.cloud.workflow.engine.run.dao.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.papla.cloud.workflow.engine.run.engine.core.activity.impl.StartActivityHandler;
import org.springframework.stereotype.Repository;

//import com.papla.cloud.workflow.platform.modal.XipPubOrgs;
import com.papla.cloud.workflow.util.PlatformUtil;
import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActPVEnumMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActivityMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActivityPropertyMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployConditionGroupMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployConditionMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployTransitionMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsActQueueMapper;
import com.papla.cloud.workflow.engine.mapper.RunInsActivityStateMapper;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityPropertyBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.modal.ConditionBean;
import com.papla.cloud.workflow.engine.modal.ConditionGroupBean;
import com.papla.cloud.workflow.engine.modal.DynaNodeQueueBean;
import com.papla.cloud.workflow.engine.modal.PVEnumBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;

/**
 * @author linpeng
 * @ClassName: ActivityDAOImpl
 * @Description: 实例节点处理接口实现类
 * @date 2015年4月20日 下午2:42:57
 */
@Repository("activityDAO")
public class ActivityDAOImpl implements ActivityDAO {


    /*=======================================流程部署===========================================*/
    @Resource
    private ProcessDeployActivityMapper processDeployActivityMapper;                    // 流程部署节点Mapper

    @Resource
    private ProcessDeployTransitionMapper processDeployTransitionMapper;                // 流程部署连线Mapper

    @Resource
    private ProcessDeployActPVEnumMapper processDeployActPVEnumMapper;                  // 流程部署节点属性枚举Mapper

    @Resource
    private ProcessDeployConditionGroupMapper processDeployConditionGroupMapper;        // 流程部署属性条件组Mapper

    @Resource
    private ProcessDeployConditionMapper processDeployConditionMapper;                  // 流程部署属性条件单元Mapper

    @Resource
    private ProcessDeployActivityPropertyMapper processDeployActivityPropertyMapper;    // 流程实例节点属性Mapper

    /*=======================================流程实例===========================================*/

    @Resource
    private RunInsActQueueMapper runInsActQueueMapper;                                  // 流程部署节点Mapper

    @Resource
    private RunInsActivityStateMapper runInsActivityStateMapper;                        // 流程实例节点Mapper

    /*
     * (非 Javadoc)
     * <p>Title: getInsActivityList</p>
     * <p>Description: 根据实例ID , 获取流程实例下节点信息  </p>
     * @param instanceId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#getInsActivityList(java.lang.String)
     */
    @Override
    public List<ActivityBean> getInsActivityList(String instanceId) throws Exception {

        // 1.实例节点基础信息
        List<ActivityBean> insList = processDeployActivityMapper.getInstanceActivityBeans(instanceId);
        if (insList != null && insList.size() > 0) {
            for (int i = 0; i < insList.size(); i++) {
                ActivityBean activityBean = insList.get(i);
                activityBean = this.getActivityBean(instanceId, activityBean, false);
                insList.set(i, activityBean);
            }
        }
        return insList;
    }

    ;

    /*
     * (非 Javadoc)
     * <p>Title: getStartActivityHandler</p>
     * <p>Description: 获取实例的开始节点信息 </p>
     * @param instanceId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#getStartActivityHandler(java.lang.String)
     */
    @Override
    public StartActivityHandler getStartActivityHandler(String instanceId) throws Exception {

        StartActivityHandler startActivityHandler = new StartActivityHandler();

        try {
            // 1.查询开始节点
            ActivityBean activityBean = this.getStartActivityBean(instanceId);

            // 2.初始化节点信息
            activityBean = this.getActivityBean(instanceId, activityBean, false);

            // 3.封装开始节点处理类
            startActivityHandler.setActivityBean(activityBean);

        } catch (Exception e) {
            throw e;
        }

        return startActivityHandler;
    }

    /*
     * (非 Javadoc)
     * <p>Title: getStartActivityBean</p>
     * <p>Description: 获取流程实例开始节点信息 </p>
     * @param instanceId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#getStartActivityBean(java.lang.String)
     */
    @Override
    public ActivityBean getStartActivityBean(String instanceId) throws Exception {
        return processDeployActivityMapper.getStartActivityBean(instanceId);
    }

    /*
     * (非 Javadoc)
     * <p>Title: getLastActivityState</p>
     * <p>Description: 查询流程实例中某个节点的最新状态 </p>
     * @param instanceId
     * @param actId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#getLastActivityState(java.lang.String, java.lang.String)
     */
    @Override
    public ActivityStateBean getLastActivityState(String instanceId, String actId) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", instanceId);
        params.put("actId", actId);
        return runInsActivityStateMapper.getLastActivityState(params);
    }

    /*
     * (非 Javadoc)
     * <p>Title: getActivityBean</p>
     * <p>Description: 获取节点配置信息 </p>
     * @param instanceId
     * @param activityBean
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#getActivityBean(java.lang.String, com.papla.cloud.wf.modal.ActivityBean)
     */
    @Override
    public ActivityBean getActivityBean(String instanceId, ActivityBean activityBean, boolean isArched) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deployId", activityBean.getDeployId());
        params.put("actId", activityBean.getId());

        // 2.实例节点属性信息
        List<ActivityPropertyBean> actPropertyList = processDeployActivityPropertyMapper.getActivityPropertyList(params);
        //转化为Map
        Map<String, String> propertytMap = PropertyHandlerUtil.propertyListToMap(actPropertyList);

        // 3.实例节点属性枚举值信息
        List<PVEnumBean> pveNumList = processDeployActPVEnumMapper.getActivityPVEnumList(params);

        // 4.实例节点迁出线集合信息
        params.clear();
        params.put("deployId", activityBean.getDeployId());
        params.put("fromActId", activityBean.getNum());
        List<TransitionBean> insTranList = processDeployTransitionMapper.getTransitionList(params);

        // 5.实例节点属性路由值信息
        HashMap<String, List<ConditionGroupBean>> actExpMap = new HashMap<String, List<ConditionGroupBean>>();

        // 路由表单
        if (WorkFlowConstants.FORM_TYPE_R.equals(propertytMap.get(WorkFlowConstants.A_FORM_TYPE))) {
            params.clear();
            params.put("parentId", activityBean.getId());
            params.put("parentType", "A");
            params.put("propertyCode", WorkFlowConstants.A_ROUTE_FORM);
            List<ConditionGroupBean> groupList = null;
            groupList = processDeployConditionGroupMapper.getConditionGroupList(params);

            for (int j = 0; j < groupList.size(); j++) {
                ConditionGroupBean cg = groupList.get(j);
                params.clear();
                params.put("groupId", cg.getGroupId());
                params.put("dbType", PlatformUtil.getDbType().toLowerCase());
                List<ConditionBean> insPropList = processDeployConditionMapper.getConditonBeanList(params);
                cg.setConditionList(insPropList);
                groupList.set(j, cg);
            }
            actExpMap.put(WorkFlowConstants.A_ROUTE_FORM, groupList);
        }

        if (WorkFlowConstants.EXECUTOR_TYPE_RU.equals(propertytMap.get(WorkFlowConstants.A_EXECUTOR_TYPE))) {    // 路由用户
            params.clear();
            params.put("parentId", activityBean.getId());
            params.put("parentType", "A");
            params.put("propertyCode", WorkFlowConstants.A_ROUTE_USER);
            List<ConditionGroupBean> conditionGroupList = null;
            conditionGroupList = processDeployConditionGroupMapper.getConditionGroupList(params);

            for (int j = 0; j < conditionGroupList.size(); j++) {
                ConditionGroupBean cgb = conditionGroupList.get(j);
                params.clear();
                params.put("groupId", cgb.getGroupId());
                params.put("dbType", PlatformUtil.getDbType().toLowerCase());
                List<ConditionBean> conditionList = processDeployConditionMapper.getConditonBeanList(params);
                cgb.setConditionList(conditionList);
                conditionGroupList.set(j, cgb);
            }
            actExpMap.put(WorkFlowConstants.A_ROUTE_USER, conditionGroupList);

        } else if (WorkFlowConstants.EXECUTOR_TYPE_RP.equals(propertytMap.get(WorkFlowConstants.A_EXECUTOR_TYPE))) { // 路由岗位
            params.clear();
            params.put("parentId", activityBean.getId());
            params.put("parentType", "A");
            params.put("propertyCode", WorkFlowConstants.A_ROUTE_POSTION);
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
            actExpMap.put(WorkFlowConstants.A_ROUTE_POSTION, conditionGroupList);
        }

        // 查询绑定条件
        params.clear();
        params.put("parentId", activityBean.getId());
        params.put("parentType", "A");
        params.put("propertyCode", WorkFlowConstants.A_BOUND_CONDITON);
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
        actExpMap.put(WorkFlowConstants.A_BOUND_CONDITON, conditionGroupList);

        activityBean.setActPropertyBeanList(actPropertyList);
        activityBean.setPropertyMap(propertytMap);
        activityBean.setTransitionList(insTranList);
        activityBean.setActPropEnumList(pveNumList);
        activityBean.setActExpMap(actExpMap);

        // 查询流程动态通知节点的审批队列信息
        if (WorkFlowConstants.NOTICE_NODE.equals(activityBean.getActType())
                && "Y".equals(propertytMap.get(WorkFlowConstants.A_IS_DYNAMIC_NODE))) {

            Map<String, Object> queueMap = new HashMap<String, Object>();
            queueMap.put("insId", instanceId);
            queueMap.put("actCode", activityBean.getCode());
            queueMap.put("isArched", isArched ? "Y" : "N");

            activityBean.setQueues(runInsActQueueMapper.getInsDynaActQueue(queueMap));
        }

        return activityBean;
    }

    /*
     * (非 Javadoc)
     * <p>Title: getActivityBean</p>
     * <p>Description: 取得实例下节点基本信息 </p>
     * @param instanceId
     * @param actId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#getActivityBean(java.lang.String, java.lang.String)
     */
    @Override
    public ActivityBean getActivityBean(String instanceId, String actId) throws Exception {
        // 取得节点基本信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", instanceId);
        params.put("actId", actId);
        ActivityBean activityBean = processDeployActivityMapper.getInstanceActivityBean(params);
        // 获取节点信息
        activityBean = this.getActivityBean(instanceId, activityBean, false);

        return activityBean;
    }

    /*
     * (非 Javadoc)
     * <p>Title: getActivityCategory</p>
     * <p>Description: 查询节点类型 </p>
     * @param instanceId
     * @param actId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#getBaseActivityBean(java.lang.String, java.lang.String)
     */
    @Override
    public String getActivityCategory(String instanceId, String actId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceId", instanceId);
        params.put("actId", actId);
        return processDeployActivityMapper.getInstanceActivityCategory(params);
    }

    /*
     * (非 Javadoc)
     * <p>Title: updateInsActState</p>
     * <p>Description: 更新单个节点状态 </p>
     * @param hashmap
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#updateInsActState(java.util.HashMap)
     */
    @Override
    public void updateInsActState(ActivityStateBean activityStateBean) throws Exception {
        try {
            runInsActivityStateMapper.updateActivityState(activityStateBean);
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: cancelInsActState</p>
     * <p>Description: 撤销流程实例节点, 即将节点状态设置为canceled状态 </p>
     * @param bean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#cancelInsActState(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @Override
    public void cancelInsActState(ProcessInstanceBean bean) throws Exception {
        try {
            // 封装参数
            HashMap<String, Object> hashmap = new HashMap<String, Object>();

            Date date = CurrentUserUtil.getCurrentDate();

            hashmap.put("insId", bean.getInstanceId());
            hashmap.put("actState", WorkFlowConstants.NODE_CANCELED_STATE);
            hashmap.put("endDate", date);
            hashmap.put("lastUpdateDate", date);
            hashmap.put("lastUpdatedBy", CurrentUserUtil.getCurrentUserId());

            // 批量更新流程实例节点状态信息
            runInsActivityStateMapper.batchUpdateActivityState(hashmap);

        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: deleteInsActState</p>
     * <p>Description: 删除实例节点状态信息 </p>
     * @param bean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#deleteInsActState(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @Override
    public void deleteInsActState(ProcessInstanceBean bean) throws Exception {
        try {
            // 删除流程实例节点状态信息
            runInsActivityStateMapper.deleteActivityStateByInstanceId(bean.getInstanceId());
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: insertInsActState</p>
     * <p>Description: 创建节点状态信息 </p>
     * @param activityBean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#insertInsActState(com.papla.cloud.wf.modal.ActivityBean)
     */
    @Override
    public void insertInsActState(ActivityStateBean activityStateBean) throws Exception {
        // 最大序号
        activityStateBean.setOrderBy(this.getNextActivityOrder(activityStateBean.getInstanceId()));
        // 执行新增
        runInsActivityStateMapper.insertActivityState(activityStateBean);
    }

    /*
     * (非 Javadoc)
     * <p>Title: getNextActivityOrder</p>
     * <p>Description: 获取下一个节点序号 </p>
     * @param instanceId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#getNextActivityOrder(java.lang.String)
     */
    @Override
    public synchronized int getNextActivityOrder(String instanceId) throws Exception {
        int nextOrder = 0;

        try {
            nextOrder = runInsActivityStateMapper.getMaxOrderBy(instanceId);
            nextOrder += 1;

        } catch (Exception e) {
            nextOrder = 0;
        }

        return nextOrder;
    }

    /*
     * (非 Javadoc)
     * <p>Title: getDynaActivities</p>
     * <p>Description: 获取流程实例下的动态节点信息  </p>
     * @param instanceId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#getDynaActivities(java.lang.String)
     */
    @Override
    public List<ActivityBean> getDynaActivities(String instanceId) throws Exception {

        List<ActivityBean> acts = runInsActQueueMapper.getDynaActivities(instanceId);

        if (acts != null && acts.size() > 0) {
            for (ActivityBean act : acts) {
                act.setPropertyMap(PropertyHandlerUtil.propertyListToMap(act.getActPropertyBeanList()));
            }
        }

        return acts;
    }

    /*
     * (非 Javadoc)
     * <p>Title: delDynaActivityQueue</p>
     * <p>Description: 删除动态通知节点的审批队列信息 </p>
     * @param insCode
     * @param actId
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#delDynaActivityQueue(java.lang.String, java.lang.String)
     */
    @Override
    public void delDynaActivityQueue(String insCode) throws Exception {
        runInsActQueueMapper.delDynaActivityQueue(insCode);
    }

    /*
     * (非 Javadoc)
     * <p>Title: updDynaActQueueStatus</p>
     * <p>Description: 修改通知节点的动态审批队列状态 </p>
     * @param queueBean
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#updDynaActQueueStatus(com.papla.cloud.wf.modal.DynaNodeQueueBean)
     */
    @Override
    public void updDynaActQueueStatus(DynaNodeQueueBean queueBean, String newStatus) throws Exception {
        queueBean.setUpdateDt(CurrentUserUtil.getCurrentDate());
        queueBean.setUpdateBy(CurrentUserUtil.getCurrentUserId());

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("bean", queueBean);
        map.put("newStatus", newStatus);

        runInsActQueueMapper.updDynaActQueueStatus(map);
    }

    /*
     * (非 Javadoc)
     * <p>Title: batchUpDynaActQueueStatus</p>
     * <p>Description: 批量更新回退路径中动态通知节点的审批队列状态 </p>
     * @param insCode
     * @param actCodeList
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.ActivityDAO#batchUpDynaActQueueStatus(java.lang.String, java.util.List)
     */
    @Override
    public void batchUpDynaActQueueStatus(String insCode, List<String> actCodeList) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("insCode", insCode);
        map.put("list", actCodeList);
        map.put("userId", CurrentUserUtil.getCurrentUserId());
        map.put("cdate", CurrentUserUtil.getCurrentDate());

        runInsActQueueMapper.batchUpDynaActQueueStatus(map);
    }
}
