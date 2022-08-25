package com.papla.cloud.workflow.engine.run.engine.test.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.papla.cloud.workflow.engine.run.engine.core.handler.impl.ActivityConfigExecutorServiceFacade;
import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.ExpressionService;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ConditionGroupBean;
import com.papla.cloud.workflow.engine.modal.EntityAttrBean;
import com.papla.cloud.workflow.engine.modal.ActivityPropertyBean;
import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.ProcessBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.run.engine.core.common.EngineCommonHandler;
import com.papla.cloud.workflow.engine.run.engine.test.ProcessTestService;
import com.papla.cloud.workflow.util.XipUtil;

/**
 * @author linpeng
 * @ClassName: ProcessTestServiceImpl
 * @Description: 流程测试服务接口实现类
 * @date 2015年4月16日 下午2:05:41
 */
@Service("processTestService")
public class ProcessTestServiceImpl implements ProcessTestService {
    @Resource
    private ExpressionService expressionService;
    @Resource
    private EngineCommonHandler engineCommonHandler;
    @Resource
    private ActivityConfigExecutorServiceFacade activityConfigExecutorServiceFacade;


    /*
     * (非 Javadoc)
     * <p>Title: getExecutionActPathAndExecutors</p>
     * <p>Description: 取得流程节点执行路径及节点执行人信息 </p>
     * @param activityBean
     * @param processBean
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.run.engine.core.handler.ActivityExecutionPathService#getExecutionActPathAndExecutors(com.papla.cloud.wf.modal.ActivityBean, com.papla.cloud.wf.modal.ProcessBean)
     */
    public List<ActivityBean> getExecutionActPathAndExecutors(ActivityBean activityBean, ProcessBean processBean) throws Exception {
        // 定义list数组 , 存储节点执行路径
        List<ActivityBean> path = new ArrayList<ActivityBean>();

        // 将当前节点加入执行路径
        path.add(activityBean);

        // 取得当前节点的下一个节点
        this.getNextActivity(activityBean, processBean, path);

        // 返回执行路径
        return path;
    }

    /**
     * @param activityBean
     * @param processBean
     * @return ActivityBean    返回类型
     * @Title: getNextActivity
     * @Description: 取得下一个节点
     */
    private void getNextActivity(ActivityBean activityBean, ProcessBean processBean, List<ActivityBean> path) throws Exception {
        // 取得实例的所有的节点信息
        List<ActivityBean> defineActivityList = processBean.getActivityBeanList();

        // 取得当前节点迁出线信息
        List<TransitionBean> lineList = activityBean.getTransitionList();

        if (lineList != null && lineList.size() > 0) {

            // 取得节点的下一节点序号
            int toActId = this.getToActIdForActivity(activityBean, "Y");

            // 校验节点的迁出线中是否存在类型为 "Y"时连线
            HashMap<String, String> tokens = new HashMap<String, String>();
            tokens.put("actName", activityBean.getName());

            if (toActId == -1 && lineList.size() > 1)
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0021", tokens));

            // 根据节点序号获取节点
            ActivityBean nextActivityBean = this.geActivityById(toActId, defineActivityList);

            // 计算节点执行人
            if (nextActivityBean != null) {
                if (nextActivityBean.getActType().equals(WorkFlowConstants.NOTICE_NODE)
                        || nextActivityBean.getActType().equals(WorkFlowConstants.COUNTERSIGN_NODE)
                        || nextActivityBean.getActType().equals(WorkFlowConstants.RESPONSE_NODE)) {

                    // 处理通知、会签和抄送节点执行人
                    ProcessInstanceBean insBean = this.initInstanceBean(nextActivityBean, processBean);

                    List<ActivityExecutorBean> activityConfigExecutorList = this.getActivityExecutors(nextActivityBean, insBean);
                    nextActivityBean.setActivityConfigExecutorList(activityConfigExecutorList);

                    // 记录节点路径
                    path.add(nextActivityBean);

                    // 递归找下一个节点执行人
                    getNextActivity(nextActivityBean, processBean, path);


                } else if (nextActivityBean.getActType().equals(WorkFlowConstants.SWITCH_NODE)) {

                    // 记录节点路径
                    path.add(nextActivityBean);

                    // 处理判断节点并计算下一个节点
                    ActivityBean afterActivityBean = this.judgeBranchActivityPath(nextActivityBean, processBean, path);

                    // 记录节点路径
                    path.add(afterActivityBean);

                    // 递归找下一个节点执行人
                    getNextActivity(afterActivityBean, processBean, path);

                } else {
                    // 记录节点路径
                    path.add(nextActivityBean);

                    // 递归找下一个节点执行人
                    getNextActivity(nextActivityBean, processBean, path);
                }
            }
        }
    }

    /**
     * @param bean
     * @param lineCode
     * @return int    返回类型
     * @Title: getToActIdForActivity
     * @Description: 取得节点的下一节点序号
     */
    private int getToActIdForActivity(ActivityBean bean, String lineCode) throws Exception {
        // 连线迁入点序号
        int toActId = -1;
        // 迁出线集合
        List<TransitionBean> transList = bean.getTransitionList();
        if (transList != null && transList.size() > 0) {    // 节点存在迁出线

            if (transList.size() == 1) { // 节点只有一条迁出线
                toActId = transList.get(0).getToActId();

            } else {    // 节点存在多条迁出线时, 查找指定属性的线信息
                for (TransitionBean tb : bean.getTransitionList()) {

                    //提交时lineCode为空,category存在
                    if (lineCode.equals(tb.getCategory())) {
                        toActId = tb.getToActId();
                        break;

                    } else {
                    }
                }
            }
        } else { // 节点不存在迁出线 （如结束节点）
            toActId = -1;
        }

        // 返回连线迁入节点序号
        return toActId;
    }

    /**
     * @param toActId
     * @param activityList
     * @return ActivityBean    返回类型
     * @Title: geActivityById
     * @Description: 根据节点序号获取节点
     */
    private ActivityBean geActivityById(int toActId, List<ActivityBean> activityList) throws Exception {
        if (toActId == -1) return null;

        // 根据节点序号, 获取节点
        if (activityList != null && activityList.size() > 0) {
            for (ActivityBean ab : activityList) {
                if (toActId == ab.getNum()) return ab;
            }
        }
        return null;
    }

    /**
     * @param nextActivityBean
     * @param insBean
     * @return List<ActivityExecutorBean>    返回类型
     * @throws Exception
     * @Title: getActivityExecutors
     * @Description: 计算节点执行人
     */
    private List<ActivityExecutorBean> getActivityExecutors(ActivityBean activityBean, ProcessInstanceBean insBean) throws Exception {
        List<ActivityExecutorBean> executors = new ArrayList<ActivityExecutorBean>();

        // 节点属性存储转换
        List<ActivityPropertyBean> actPropertyList = activityBean.getActPropertyList();
        if (actPropertyList != null && actPropertyList.size() > 0) {
            List<ActivityPropertyBean> actPropertyBeanList = new ArrayList<ActivityPropertyBean>();
            for (ActivityPropertyBean propBean : actPropertyList) {
                ActivityPropertyBean pb = new ActivityPropertyBean();
                pb.setActId(propBean.getActId());
                pb.setPropertyCode(propBean.getProcessCode());
                pb.setPropertyName(propBean.getProcessName());
                pb.setPropertyValue(propBean.getPropertyValue());
                actPropertyBeanList.add(pb);
            }
            activityBean.setActPropertyBeanList(actPropertyBeanList);
        }

        try {
            executors = activityConfigExecutorServiceFacade.getActivityConfigExecutorList(activityBean, insBean);
        } catch (Exception e) {
            throw e;
        }

        return executors;
    }

    /**
     * @param nextActivityBean
     * @param processBean
     * @param path
     * @return ActivityBean    返回类型
     * @throws Exception
     * @Title: judgeBranchActivityPath
     * @Description: 判断分支节点条件信息, 并计算下一个节点
     */
    private ActivityBean judgeBranchActivityPath(ActivityBean nextActivityBean, ProcessBean processBean, List<ActivityBean> path) throws Exception {
        ActivityBean afterActivityBean = new ActivityBean();

        // 取得判断条件组信息
        ConditionGroupBean cgb = null;
        List<ConditionGroupBean> groups = nextActivityBean.getConditionGroupList();
        if (groups != null && groups.size() > 0) {
            for (ConditionGroupBean groupBean : groups) {
                if (groupBean.getPropertyCode().equals(WorkFlowConstants.A_BOUND_CONDITON)) {
                    cgb = groupBean;
                    break;
                }
            }
        }
        HashMap<String, String> tokens = new HashMap<String, String>();
        tokens.put("actName", nextActivityBean.getName());

        if (cgb == null) throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0022", tokens));

        // 当前流程实例Bean
        ProcessInstanceBean insBean = this.initInstanceBean(nextActivityBean, processBean);
        // 存储判断结果
        boolean switchResult = false;
        try {
            // 设置当前节点
            insBean.setCurrentActivityHandler(engineCommonHandler.setCurrentActivityHandler(nextActivityBean).getCurrentActivityHandler());
            // 判断条件真假
            switchResult = expressionService.judgeByBoundExpression(cgb, insBean);
        } catch (Exception e) {
            throw e;

        } finally {
            // 清空当前节点
            insBean.setCurrentActivityHandler(null);
        }

        // 取的判断节点的下一个节点
        String lineCode = switchResult ? "Y" : "N";
        int toActId = this.getToActIdForActivity(nextActivityBean, lineCode);
        afterActivityBean = this.geActivityById(toActId, processBean.getActivityBeanList());

        // 根据节点类型计算节点执行人类型
        if (afterActivityBean.getActType().equals(WorkFlowConstants.NOTICE_NODE)
                || afterActivityBean.getActType().equals(WorkFlowConstants.COUNTERSIGN_NODE)
                || afterActivityBean.getActType().equals(WorkFlowConstants.RESPONSE_NODE)) {

            afterActivityBean.setActivityConfigExecutorList(this.getActivityExecutors(afterActivityBean, insBean));

        } else if (afterActivityBean.getActType().equals(WorkFlowConstants.SWITCH_NODE)) {

            judgeBranchActivityPath(afterActivityBean, processBean, path);
        }

        return afterActivityBean;
    }

    /**
     * @param activityBean
     * @param processBean
     * @return ProcessInstanceBean    返回类型
     * @throws Exception
     * @Title: initInstanceBean
     * @Description: 初始化流程实例Bean信息
     */
    private ProcessInstanceBean initInstanceBean(ActivityBean activityBean, ProcessBean processBean) throws Exception {
        // 1.设置当前处理节点
        ProcessInstanceBean insBean = engineCommonHandler.setCurrentActivityHandler(activityBean);
        insBean.setTest(true);

        // 2.转换节点信息
        List<ActivityBean> actList = processBean.getActivityBeanList();
        insBean.setActivityBeanList(actList);

        // 3.属性赋值
        List<InstanceEntityAttrBean> attrList = new ArrayList<InstanceEntityAttrBean>();

        Map<String, Object> map = processBean.getEntityAttrMap();
        if (map == null) return insBean;

        List<EntityAttrBean> attrBeanList = processBean.getEntityAttrBeanList();
        for (EntityAttrBean attrBean : attrBeanList) {

            String category = attrBean.getAttrCategory();
            String attrCode = attrBean.getAttrCode();

            InstanceEntityAttrBean insAttrBean = new InstanceEntityAttrBean();
            insAttrBean.setAttrId(attrBean.getId());
            insAttrBean.setAttrCode(attrCode);
            insAttrBean.setAttrName(attrBean.getAttrName());
            insAttrBean.setAttrDesc(attrBean.getAttrDesc());
            insAttrBean.setAttrCategory(category);

            if ("static".equals(category)) { // 静态参数
                insAttrBean.setAttrResult(String.valueOf(map.get(attrCode)));

            } else { // 动态参数
                insAttrBean.setFuncType(attrBean.getFuncType());
                insAttrBean.setFuncValue(attrBean.getFuncValue());
            }
            insAttrBean.setEntityId(attrBean.getEntityId());

            attrList.add(insAttrBean);
        }

        insBean.setAttrList(attrList);

        // 4.设置应用信息
        //insBean.setAppBean(processDefinitionDAO.getApplBeanByProcId(processBean.getId()));

        // 5.返回
        return insBean;
    }
}
