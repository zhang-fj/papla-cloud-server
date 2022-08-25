package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.ExpressionService;
import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.modal.ConditionGroupBean;
import com.papla.cloud.workflow.engine.modal.DynaNodeQueueBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransStateBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;
import com.papla.cloud.workflow.engine.run.dao.ActivityExecutionPathDao;
import com.papla.cloud.workflow.engine.run.engine.core.common.EngineCommonHandler;
import com.papla.cloud.workflow.engine.run.engine.core.handler.ActivityExecutionPathService;
import com.papla.cloud.workflow.util.XipUtil;


/**
 * @author linpeng
 * @ClassName: ActivityExecutionPathServiceImpl
 * @Description: 节点执行路径服务类
 * @date 2015年4月15日 上午10:40:58
 */
@Service("activityExecutionPathService")
public class ActivityExecutionPathServiceImpl implements ActivityExecutionPathService {

    @Autowired
    private ActivityExecutionPathDao activityExecutionPathDao;
    @Autowired
    private ActivityConfigExecutorServiceFacade activityConfigExecutorServiceFacade;
    @Autowired
    private ActivityExecutorAvoidServiceFacade activityExecutorAvoidServiceFacade;
    @Autowired
    private ExpressionService expressionService;
    @Autowired
    private ProcessEngineCommonService processEngineCommonService;
    @Autowired
    private EngineCommonHandler engineCommonHandler;
    @Autowired
    private ActivityDAO activityDAO;


    /*
     * (非 Javadoc)
     * <p>Title: getExecutePath</p>
     * <p>Description: 取得流程实例当前运行中节点及已完成节点的执行路径 </p>
     * @param instanceId
     * @return
     * @see com.papla.cloud.wf.run.engine.core.handler.ActivityExecutionPathService#getExecutePath(java.lang.String)
     */
    @Override
    public List<ActivityBean> getExecutePath(String instanceId) {
        // 取得已完成节点执行路径
        List<ActivityBean> activityList = activityExecutionPathDao.getExecutePath(instanceId);

        // 循环取得路径中每个节点的实际执行人信息
        for (ActivityBean ab : activityList) {
            ab.setActivityExecutorList(activityExecutionPathDao.getActRealExecutor(ab));
        }
        return activityList;
    }
    /*
     * (非 Javadoc)
     * <p>Title: getNextExecutionActivityPath</p>
     * <p>Description: 取得本节点到下一节点的执行路径 </p>
     * @param activityBean
     * @param processInstanceBean
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.run.engine.core.handler.ActivityExecutionPathService#getNextExecutionActivityPath(com.papla.cloud.wf.modal.ActivityBean, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @Override
    public List<ActivityBean> getNextExecutionActivityPath(ActivityBean activityBean, ProcessInstanceBean insBean) throws Exception {
        List<ActivityBean> path = new ArrayList<ActivityBean>();

        // 设置流程实例ID和编码
        activityBean.setInstanceId(insBean.getInstanceId());
        activityBean.setInstanceCode(insBean.getInstanceCode());

        if (insBean.getBackFromActivityId() != null) {
            // 计算流程回退路径, (此时不计算节点执行人)
            this.getNextExecutionActivityPath(activityBean, insBean, path);

        } else {
            if (WorkFlowConstants.COUNTERSIGN_NODE.equals(activityBean.getActType())) { // 会签节点
                path = this.getNextExecutionActivityPath4Countersign(activityBean, insBean);

            } else { // 通知节点或开始节点
                path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);
            }
        }
        return path;
    }

    /**
     * @param activityBean
     * @param insBean
     * @return List<ActivityBean>    返回类型
     * @throws Exception
     * @Title: getNextExecutionActivityPath4Notice
     * @Description: 规划路径：即从当前通知节点开始取得本节点到下一通知或会签节点的执行路径
     */
    private List<ActivityBean> getNextExecutionActivityPath4Notice(ActivityBean activityBean, ProcessInstanceBean insBean) throws Exception {
        // 定义list数组 , 存储节点执行路径
        List<ActivityBean> path = new ArrayList<ActivityBean>();

        // 取得实例的所有的节点信息
        List<ActivityBean> defineActivityList = insBean.getActivityBeanList();

        // 将当前节点加入执行路径
        path.add(activityBean);

        // 动态通知节点运行中的审批队列 20170213
        if (activityBean.getActType().equals(WorkFlowConstants.NOTICE_NODE)
                && "Y".equals(processEngineCommonService.isDynamicNode(activityBean))) {
            activityBean.setQueueBean(activityExecutionPathDao.getRunningNodeQueue(activityBean));
        }

        // 取得当前节点的下一个节点
        ActivityBean nextActivity = this.getNextActivity(activityBean, defineActivityList);

        // 取得最近一个符合条件的通知或会签节点
        this.getNextExecutionActivityPath(nextActivity, insBean, path);

        // 返回执行路径
        return path;
    }

    /**
     * @param activityBean
     * @param processInstanceBean
     * @return List<ActivityBean>    返回类型
     * @throws Exception
     * @Title: getNextExecutionActivityPath4Countersign
     * @Description: 从会签节点开始取得本节点到下一节点的执行路径
     */
    private List<ActivityBean> getNextExecutionActivityPath4Countersign(ActivityBean activityBean, ProcessInstanceBean insBean) throws Exception {

        // 定义list数组 , 存储节点执行路径
        List<ActivityBean> path = new ArrayList<ActivityBean>();

        // 会签规则
        Map<String, String> propertyMap = activityBean.getPropertyMap();
        String countersignRule = (String) propertyMap.get(WorkFlowConstants.A_COUNTERSIGN_RULE);

        // 会签规则值转换
        int pv = PropertyHandlerUtil.transPropertyValue(countersignRule);

        // 实例ID和节点ID
        String actId = activityBean.getId();
        String insId = insBean.getInstanceId();

        // 会签节点迁出线
        List<TransitionBean> moveOutLines = activityBean.getTransitionList();

        // 计算审批人所选迁出线类型
        if (moveOutLines.size() == 2) {  // 会签双迁出线
            // 计算审批人所选迁出的类型: Y线或N线
            if (pv == 3 || pv == 4) {
                for (TransitionBean transBean : moveOutLines) {
                    if (transBean.getId().equals(insBean.getChooseMoveOutLineId())) {
                        insBean.setLineCategory(transBean.getCategory());
                        break;
                    }
                }
            }
        }

        switch (pv) {
            case 0:    // 抢单

                activityBean.setLastApprover(true);
                if ("Y".equals(insBean.getLineCategory())) { // Y线

                    activityBean.setDoReject(false);
                    path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);

                } else { // N线

                    if (moveOutLines.size() == 1) { // 单迁出线,驳回处理
                        activityBean.setDoReject(true);
                        path.add(activityBean);
                    } else {
                        activityBean.setDoReject(false);
                        path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);
                    }
                }
                break;

            case 1: // 一票通过

                if ("Y".equals(insBean.getLineCategory())) {
                    activityBean.setLastApprover(true);
                    activityBean.setDoReject(false);
                    path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);

                } else {
                    // 判断当前审批人是否为最后一个驳回人
                    boolean isLastApprover = processEngineCommonService.isLastApprover(actId, insBean);
                    activityBean.setLastApprover(isLastApprover);
                    if (isLastApprover) {
                        if (moveOutLines.size() == 1) { // 会签单迁出线,驳回处理
                            activityBean.setDoReject(true);
                            path.add(activityBean);

                        } else {    // 会签双迁出线,驳回处理
                            activityBean.setDoReject(false);
                            path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);
                        }
                    } else {
                        activityBean.setDoReject(false);
                        path.add(activityBean);
                    }
                }
                break;

            case 2:    // 一票否决

                if ("N".equals(insBean.getLineCategory())) {
                    activityBean.setLastApprover(true);
                    if (moveOutLines.size() == 1) { // 会签单迁出线,驳回处理
                        activityBean.setDoReject(true);
                        path.add(activityBean);

                    } else {    // 会签双迁出线,驳回处理
                        activityBean.setDoReject(false);
                        path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);
                    }

                } else {
                    activityBean.setDoReject(false);
                    // 判断当前审批人是否为最后一个同意人
                    boolean isLastApprover = processEngineCommonService.isLastApprover(actId, insBean);
                    activityBean.setLastApprover(isLastApprover);
                    if (isLastApprover) {
                        path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);
                    } else {
                        path.add(activityBean);
                    }
                }
                break;

            case 3:    // 比例通过

                // 是否为本节点的最后一个审批人
                boolean isLastApprover3 = processEngineCommonService.isLastApprover(actId, insBean);

                // 执行线区别
                if ("Y".equals(insBean.getLineCategory())) { // Y线

                    // 判断是否达到会签规则设定通过比例
                    boolean isUpPassPercent = processEngineCommonService.isUpToExpectedPercent(activityBean, insId, "Y");

                    if (isUpPassPercent) {
                        /*
                         * 达到通过比例
                         */
                        activityBean.setLastApprover(true);
                        activityBean.setDoReject(false);
                        path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);

                    } else {
                        /*
                         * 未达到通过比例
                         */
                        activityBean.setLastApprover(isLastApprover3);
                        if (isLastApprover3) {
                            /*
                             *  当前审批人为本节点最后一个审批人
                             */
                            if (moveOutLines.size() == 1) { // 会签单迁出线,驳回处理
                                activityBean.setDoReject(true);
                                path.add(activityBean);

                            } else {    // 会签双迁出线,驳回处理

                                // 如果当前审批人为流程最后一个审批人 且流程通过实际比例仍未达到期望比例; 则此时按照驳回处理 。计算驳回线ID
                                TransitionBean transBean = this.getChooseMoveOutLine(activityBean, "N");
                                activityBean.setChooseLineCode(transBean.getCode());
                                activityBean.setDoReject(false);
                                insBean.setChooseMoveOutLineId(transBean.getId());
                                // 计算路径
                                path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);
                            }

                        } else {
                            /*
                             * 当前审批人非本节点最后一个审批人
                             */
                            activityBean.setDoReject(false);
                            path.add(activityBean);
                        }
                    }

                } else { // N线

                    activityBean.setLastApprover(isLastApprover3);

                    // 是否为本节点的最后一个审批人
                    if (isLastApprover3) {
                        if (moveOutLines.size() == 1) { // 会签单迁出线,驳回处理
                            activityBean.setDoReject(true);
                            path.add(activityBean);

                        } else {    // 会签双迁出线,驳回处理
                            activityBean.setDoReject(false);
                            path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);
                        }
                    } else {
                        activityBean.setDoReject(false);
                        path.add(activityBean);
                    }
                }
                break;

            case 4:    // 比例驳回

                // 是否为本节点的最后一个审批人
                boolean isLastApprover4 = processEngineCommonService.isLastApprover(actId, insBean);

                if ("N".equals(insBean.getLineCategory())) { // N线

                    // 判断是否达到会签规则设定驳回比例
                    boolean isUpRefusePercent = processEngineCommonService.isUpToExpectedPercent(activityBean, insId, "N");
                    if (isUpRefusePercent) {
                        /*
                         *  达到驳回比例
                         */
                        activityBean.setLastApprover(true);
                        if (moveOutLines.size() == 1) { // 会签单迁出线
                            activityBean.setDoReject(true);
                            path.add(activityBean);

                        } else {    // 会签双迁出线
                            activityBean.setDoReject(false);
                            path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);
                        }

                    } else {
                        /*
                         * 未达到驳回比例
                         */
                        activityBean.setLastApprover(isLastApprover4);
                        activityBean.setDoReject(false);

                        if (isLastApprover4) {
                            /*
                             * 如果当前审批人为流程最后一个审批人 且流程驳回实际比例仍未达到期望比例; 则此时按照通过处理 。 计算通过线ID
                             */
                            TransitionBean transBean = this.getChooseMoveOutLine(activityBean, "Y");
                            activityBean.setChooseLineCode(transBean.getCode());
                            insBean.setChooseMoveOutLineId(transBean.getId());
                            path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);

                        } else {
                            path.add(activityBean);
                        }
                    }

                } else { // Y线

                    // 是否为本节点的最后一个审批人
                    activityBean.setLastApprover(isLastApprover4);
                    activityBean.setDoReject(false);
                    if (isLastApprover4) {
                        path = this.getNextExecutionActivityPath4Notice(activityBean, insBean);

                    } else {
                        path.add(activityBean);
                    }
                }
                break;

            default:
                break;
        }

        return path;
    }

    /**
     * @param processInstanceBean
     * @return String    返回类型
     * @Title: getChooseMoveOutLine
     * @Description: 计算迁出线ID
     */
    private TransitionBean getChooseMoveOutLine(ActivityBean actBean, String lineCategory) {
        TransitionBean transBean = null;
        List<TransitionBean> beans = actBean.getTransitionList();
        for (TransitionBean bean : beans) {
            if (lineCategory.equals(bean.getCategory())) {
                transBean = bean;
                break;
            }
        }
        return transBean;
    }

    /**
     * @param activityBean
     * @param defineActivityList
     * @return ActivityBean    返回类型
     * @throws Exception
     * @Title: getNextActivity
     * @Description: 取得节点对应的下一个节点
     */
    private ActivityBean getNextActivity(ActivityBean activityBean, List<ActivityBean> defineActivityList) throws Exception {
        ActivityBean actBean = null;

        // 流程实例是否为动态通知节点: Y-是, N-否
        String isDynaNode = "N";
        if (activityBean.getActType().equals(WorkFlowConstants.NOTICE_NODE)) {
            isDynaNode = processEngineCommonService.isDynamicNode(activityBean);
        }

        // 查询序号最小且尚未审批的队列信息
        DynaNodeQueueBean queueBean = null;
        if ("Y".equals(isDynaNode)) {
            queueBean = activityExecutionPathDao.getCurrentNodeQueue(activityBean);
        }

        // 计算下一个节点
        if (queueBean == null) {
            // 取得当前节点迁出线信息
            List<TransitionBean> lineList = activityBean.getTransitionList();
            if (lineList == null || (lineList != null && lineList.size() == 0)) {
                return null;
            }
            // 查找节点
            int toActId = this.getToActIdForActivity(activityBean, activityBean.getChooseLineCode());

            // 校验节点的迁出线中是否存在类型为 "Y"时连线
            HashMap<String, String> tokens = new HashMap<String, String>();
            tokens.put("actName", activityBean.getName());

            if (toActId == -1 && lineList.size() > 1)
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0021", tokens));

            // 根据节点序号获取节点
            actBean = this.geActivityById(toActId, defineActivityList);

            // 动态通知节点
            if (actBean.getActType().equals(WorkFlowConstants.NOTICE_NODE) && "Y".equals(processEngineCommonService.isDynamicNode(actBean))) {

                // 设置流程实例ID
                actBean.setInstanceId(activityBean.getInstanceId());

                // 节点的最新状态
                ActivityStateBean lastActivityState = activityDAO.getLastActivityState(activityBean.getInstanceId(), actBean.getId());

                // 获取动态队列
                if (lastActivityState != null && WorkFlowConstants.NODE_COMPLETED_STATE.equals(lastActivityState.getActState())) {
                    queueBean = activityExecutionPathDao.getFirstClosedNodeQueue(actBean);
                    actBean.setAgainRunning(true); // 设置再次运行标志
                } else {
                    queueBean = activityExecutionPathDao.getCurrentNodeQueue(actBean);
                }
                if (queueBean != null) actBean.setQueueBean(queueBean);
            }

        } else {
            // 处理动态节点
            actBean = new ActivityBean();
            BeanUtils.copyProperties(activityBean, actBean);
            actBean.setQueueBean(queueBean);
        }

        return actBean;
    }

    /**
     * @param toActId
     * @param activityList
     * @return ActivityBean    返回类型
     * @Title: geActivityById
     * @Description: 根据节点序号取得节点
     */
    private ActivityBean geActivityById(int toActId, List<ActivityBean> activityList) {
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
     * @param processInstanceBean
     * @param path
     * @return void    返回类型
     * @throws Exception
     * @Title: getNextExecutionActivityPath
     * @Description: 取得下一个通知/会签节点
     */
    private void getNextExecutionActivityPath(ActivityBean nextActivityBean, ProcessInstanceBean processInstanceBean, List<ActivityBean> path) throws Exception {
        if (nextActivityBean == null) return;

        // 取得流程中节点信息
        List<ActivityBean> defineActivityList = processInstanceBean.getActivityBeanList();

        if (processInstanceBean.getBackFromActivityId() != null) { // 计算流程回退路径, (此时不计算节点执行人)

            path.add(nextActivityBean);

            if (nextActivityBean.getId().equals(processInstanceBean.getBackFromActivityId())) {
                return;

            } else {
                // 当前节点的所有迁出线信息
                List<TransitionBean> insTransList = nextActivityBean.getTransitionList();

                // 当前流程实例已经流经的节点连线信息
                List<TransStateBean> transStateList = processInstanceBean.getTransStateList();

                // 计算当前节点的迁出线编码及迁出线对象
                for (TransitionBean transBean : insTransList) {
                    String transitionId = transBean.getId();
                    for (TransStateBean transStateBean : transStateList) {
                        String transId = transStateBean.getTransitionId();
                        if (transId.equals(transitionId)) {
                            nextActivityBean.setMoveInTransition(transBean);
                            nextActivityBean.setChooseLineCode(transBean.getCode());
                            break;
                        }
                    }
                }

                // 取得节点对应的下一个节点
                ActivityBean afterOtherActivity = this.getNextActivity(nextActivityBean, defineActivityList);

                // 递归找路径
                getNextExecutionActivityPath(afterOtherActivity, processInstanceBean, path);
            }

        } else {    // 计算执行路径

            // 非法环路校验处理
            this.checkProcessLoop(nextActivityBean, path, processInstanceBean);

            if (nextActivityBean.getActType().equals(WorkFlowConstants.NOTICE_NODE)
                    || nextActivityBean.getActType().equals(WorkFlowConstants.COUNTERSIGN_NODE)) {    //处理通知会签节点

                this.processNoticeNode(nextActivityBean, processInstanceBean, path);

            } else if (nextActivityBean.getActType().equals(WorkFlowConstants.SWITCH_NODE)) {    //处理判断节点
                this.processSwitchNode(nextActivityBean, processInstanceBean, path);

            } else if (nextActivityBean.getActType().equals(WorkFlowConstants.RESPONSE_NODE)) {    //处理抄送节点
                this.processResponseNode(nextActivityBean, processInstanceBean, path);

            } else { //处理其它节点

                path.add(nextActivityBean);

                // 取得节点对应的下一个节点
                ActivityBean afterOtherActivity = this.getNextActivity(nextActivityBean, defineActivityList);

                // 递归找路径
                getNextExecutionActivityPath(afterOtherActivity, processInstanceBean, path);
            }
        }
    }

    /**
     * @param activityBean
     * @param path
     * @param processInstanceBean
     * @throws Exception 设定文件
     * @Title: checkProcessLoop
     * @Description: 验证流程环路是否合法
     */
    private void checkProcessLoop(ActivityBean activityBean, List<ActivityBean> path, ProcessInstanceBean instanceBean) throws Exception {
        if (instanceBean.isExistsLoop()) {
            List<Integer> currentPath = new ArrayList<Integer>();
            for (ActivityBean bean : path) {
                int actNum = bean.getNum();
                currentPath.add(actNum);
            }

            if (currentPath.contains(activityBean.getNum())) {
                if (WorkFlowConstants.RESPONSE_NODE.equals(activityBean.getActType())
                        || WorkFlowConstants.FUNCTION_NODE.equals(activityBean.getActType())
                        || WorkFlowConstants.SWITCH_NODE.equals(activityBean.getActType())) {
                    int startPos = currentPath.indexOf(activityBean.getNum());

                    StringBuffer str = new StringBuffer();
                    str.append("   ").append(XipUtil.getMessage("XIP_WF_SERVICE_0032", null)).append("{");
                    for (int i = 0; i < path.size(); i++) {
                        ActivityBean actBean = path.get(i);
                        if (i < startPos) {
                            str.append("[").append(actBean.getName()).append("]");
                        } else {
                            str.append("&lt;font color=red&gt;[" + actBean.getName() + "]&lt;/font&gt;");
                        }
                        str.append("-&gt;");
                    }
                    str.append("&lt;font color=red&gt;[" + activityBean.getName() + "]&lt;/font&gt;-&gt;...");
                    str.append("},").append(XipUtil.getMessage("XIP_WF_SERVICE_0033", null));

                    throw new Exception(str.toString());
                }
            }
        }
    }

    /**
     * @param nextActivityBean
     * @param processInstanceBean
     * @param path
     * @return void    返回类型
     * @throws Exception
     * @Title: processNoticeNode
     * @Description: 处理当前节点为通知节点的情况
     */
    private void processNoticeNode(ActivityBean nextActivityBean, ProcessInstanceBean processInstanceBean, List<ActivityBean> path) throws Exception {
        // 取得实例下节点信息
        List<ActivityBean> defineActivityList = processInstanceBean.getActivityBeanList();

        // 当前节点为动态节点且存在动态审批队列信息
        DynaNodeQueueBean queueBean = nextActivityBean.getQueueBean();

        // 取得节点配置执行人
        List<ActivityExecutorBean> configExecutorList = null;
        if (queueBean == null) {
            configExecutorList = activityConfigExecutorServiceFacade.getActivityConfigExecutorList(nextActivityBean, processInstanceBean);
            if (configExecutorList == null || configExecutorList.size() == 0) {
                HashMap<String, String> tokens = new HashMap<String, String>();
                tokens.put("code", nextActivityBean.getCode());
                tokens.put("name", nextActivityBean.getName());
                throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0031", tokens));
            }
        } else { // 当前动态队列行审批人
            configExecutorList = activityExecutionPathDao.getConfigExecutorList(queueBean.getApprover());
        }
        nextActivityBean.setActivityConfigExecutorList(configExecutorList);

        // 流程是否自动失效了规避策略（如果流程中存在环路时将自动失效规避策略）: Y-失效规避策略，N-不失效规避策略
        String isAutoDisabledAvoidStrategy = processInstanceBean.getAutoDisabledAvoidStrategy();

        // 节点规避属性： Y-参与规避, N-不参与规避
        String avoidAttr = "N";

        // 如果流程未自动失效规避策略，则计算节点的规避属性
        if ("N".equals(isAutoDisabledAvoidStrategy)) {
            avoidAttr = PropertyHandlerUtil.findPropertyValue(nextActivityBean.getActPropertyBeanList(), WorkFlowConstants.A_IS_AVOID_FLAG);
        }

        // 会签节点不参与规避处理
        if (WorkFlowConstants.COUNTERSIGN_NODE.equals(nextActivityBean.getActType())) avoidAttr = "N";

        if ("N".equals(avoidAttr)) {    // 不参与规避
            path.add(nextActivityBean);
            nextActivityBean.setAvoidFlag(false);
            // 设置最终执行人
            nextActivityBean.setActivityExecutorList(configExecutorList);

        } else if ("Y".equals(avoidAttr)) { // 参与规避
            // 计算发生规避后的节点最终执行人
            List<ActivityExecutorBean> finalExecutorList = activityExecutorAvoidServiceFacade.avoidExecutor(configExecutorList, processInstanceBean);

            // 判断节点是否已经被系统规避(即不需要再执行审批处理)
            boolean isAvoid = this.isAvoidActivity(configExecutorList, finalExecutorList);

            // 计算迁出线对象, 即 "Y" 属性线
            if (isAvoid) {
                // 排除动态节点已规避的队列
                if (queueBean != null) {
                    this.recordExcludeOpenNodes(nextActivityBean, queueBean);
                }
                // 判断动态节点是否仍存在审批队列信息
                DynaNodeQueueBean nodeQueueBean = activityExecutionPathDao.getCurrentNodeQueue(nextActivityBean);
                if (nodeQueueBean == null) { // 不存在则计算属性为 "Y" 的迁出线对象
                    List<TransitionBean> lines = nextActivityBean.getTransitionList();
                    for (TransitionBean transBean : lines) {
                        if ("Y".equals(transBean.getCategory())) {
                            nextActivityBean.setMoveInTransition(transBean);
                            break;
                        }
                    }
                } else {
                    nextActivityBean.setChooseLineCode(WorkFlowConstants.TASK_BTN_LOOP_AGREE);
                }
            }

            // 封装节点信息
            nextActivityBean.setActivityExecutorList(finalExecutorList);
            nextActivityBean.setAvoidFlag(isAvoid);
            path.add(nextActivityBean);

            // 如果当前节点已经被系统规避，则继续计算下一个节点
            if (isAvoid) {
                ActivityBean afterNoticeActivity = this.getNextActivity(nextActivityBean, defineActivityList);
                getNextExecutionActivityPath(afterNoticeActivity, processInstanceBean, path);
            }
        }
    }

    /**
     * @param activityBean
     * @param queueBean    设定文件
     * @Title: recordExcludeOpenNodes
     * @Description: 排除动态节点已规避的队列
     */
    private void recordExcludeOpenNodes(ActivityBean activityBean, DynaNodeQueueBean queueBean) {
        List<String> excludeOpenNodes = activityBean.getExcludeOpenNodes();
        if (excludeOpenNodes != null && excludeOpenNodes.size() > 0) {
            excludeOpenNodes.add(queueBean.getQueueId());
        } else {
            excludeOpenNodes = new ArrayList<String>();
            excludeOpenNodes.add(queueBean.getQueueId());
        }
        activityBean.setExcludeOpenNodes(excludeOpenNodes);
    }

    /**
     * @param nextActivityBean
     * @param processInstanceBean
     * @param path
     * @return void    返回类型
     * @throws Exception
     * @Title: processSwitchNode
     * @Description: 处理当前节点为判断节点的情况
     */
    private void processSwitchNode(ActivityBean nextActivityBean, ProcessInstanceBean processInstanceBean, List<ActivityBean> path) throws Exception {
        // 取得流程实例节点信息
        List<ActivityBean> defineActivityList = processInstanceBean.getActivityBeanList();

        // 取得判断节点绑定条件信息
        ConditionGroupBean cgb = this.getConditionGroupBeanForActivity(nextActivityBean);

        HashMap<String, String> tokens = new HashMap<String, String>();
        tokens.put("actName", nextActivityBean.getName());
        if (cgb == null) throw new Exception(XipUtil.getMessage("XIP_WF_SERVICE_0022", tokens));

        // 存储判断结果
        boolean switchResult = false;
        try {
            // 设置当前节点
            processInstanceBean.setCurrentActivityHandler(
                    engineCommonHandler.setCurrentActivityHandler(nextActivityBean).getCurrentActivityHandler());
            // 判断条件真假
            switchResult = expressionService.judgeByBoundExpression(cgb, processInstanceBean);

        } catch (Exception e) {
            throw e;

        } finally {
            // 清空当前节点
            processInstanceBean.setCurrentActivityHandler(null);
        }


        // 设置迁出线对象
        List<TransitionBean> lines = nextActivityBean.getTransitionList();
        for (TransitionBean transBean : lines) {
            if (switchResult) {    // 判断条件为真 , 则查找 "Y"属性线
                if ("Y".equals(transBean.getCategory())) {
                    nextActivityBean.setMoveInTransition(transBean);
                    break;
                }
            } else {    // 判断条件为假 , 则查找 "N"属性线
                if ("N".equals(transBean.getCategory())) {
                    nextActivityBean.setMoveInTransition(transBean);
                    break;
                }
            }
        }

        // 取得判断节点的下一节点
        // String lineType = switchResult ? "Y" : "N" ;
        String lineCode = nextActivityBean.getMoveInTransition().getCode();
        int toActId = this.getToActIdForActivity(nextActivityBean, lineCode);

        path.add(nextActivityBean);  //将判断节点添加到路径中

        ActivityBean afterSwitchActivity = this.geActivityById(toActId, defineActivityList);

        getNextExecutionActivityPath(afterSwitchActivity, processInstanceBean, path);
    }

    /**
     * @param nextActivityBean
     * @param processInstanceBean
     * @param path
     * @return void    返回类型
     * @throws Exception
     * @Title: processResponseNode
     * @Description: 处理当前节点为抄送节点的情况
     */
    private void processResponseNode(ActivityBean nextActivityBean, ProcessInstanceBean processInstanceBean, List<ActivityBean> path) throws Exception {
        // 取得实例下节点信息
        List<ActivityBean> defineActivityList = processInstanceBean.getActivityBeanList();

        // 取得节点配置执行人
        List<ActivityExecutorBean> configExecutorList = activityConfigExecutorServiceFacade.getActivityConfigExecutorList(nextActivityBean, processInstanceBean);
		/*		
		 * modify by linp 2014-12-19 , 抄送节点为配置执行人，视为不执行抄送处理
 		if(configExecutorList==null||configExecutorList.size()==0){
			throw new Exception("节点【"+nextActivityBean.getCode()+"-"+nextActivityBean.getName()+"】配置执行人为空");
		}*/
        nextActivityBean.setActivityConfigExecutorList(configExecutorList);

        path.add(nextActivityBean);    //将抄送节点添加到路径中

        // 取得节点对应的下一个节点
        ActivityBean afterOtherActivity = this.getNextActivity(nextActivityBean, defineActivityList);

        // 递归找路径
        getNextExecutionActivityPath(afterOtherActivity, processInstanceBean, path);
    }


    /**
     * @param bean
     * @return ConditionGroupBean    返回类型
     * @Title: getConditionGroupBeanForActivity
     * @Description: 取得节点对应的条件组
     */
    private ConditionGroupBean getConditionGroupBeanForActivity(ActivityBean bean) {
        ConditionGroupBean conditionGroupBean = null;

        /*
         *	存储流程实例节点属性中所涉及表达式信息
         * 	KEY值为属性编码(propertyCode)，Value为存储了ConditionGroupBean的List数组
         *
         */
        HashMap<String, List<ConditionGroupBean>> hashmap = bean.getActExpMap();

        // 获取绑定条件组信息
        List<ConditionGroupBean> groupList = hashmap.get(WorkFlowConstants.A_BOUND_CONDITON);

        // 对于一个判断节点只存在一个条件组
        if (groupList != null && groupList.size() > 0) {
            conditionGroupBean = groupList.get(0);
        }

        return conditionGroupBean;
    }


    /**
     * @param bean
     * @param lineCode
     * @return int    返回类型
     * @Title: getToActIdForActivity
     * @Description: 取得节点的下一节点
     */
    private int getToActIdForActivity(ActivityBean bean, String lineCode) {

        // 连线迁入点序号
        int toActId = -1;

        // 迁出线集合
        List<TransitionBean> transList = bean.getTransitionList();

        if (transList != null && transList.size() > 0) {    // 节点存在迁出线

            if (transList.size() == 1) { // 节点只有一条迁出线
                toActId = transList.get(0).getToActId();

            } else {    // 节点存在多条迁出线时, 查找指定属性的线信息
                for (TransitionBean tb : bean.getTransitionList()) {

                    // 排除提交时lineCode为空,category存在
                    if (StringUtils.isEmpty(lineCode) && "Y".equals(tb.getCategory())) {
                        toActId = tb.getToActId();
                        break;
                    } else if (tb.getCode().equals(lineCode)) {
                        toActId = tb.getToActId();
                        break;
                    } else {
                        // 扩展使用
                    }
                }
            }
        } else { // 节点不存在迁出线 （如结束节点）

            toActId = -1;
        }

        // 返回连线迁入节点序号
        return toActId;
    }


    /*
     * (非 Javadoc)
     * <p>Title: getLastExecutionActivityNode</p>
     * <p>Description: 取得最后节点信息 </p>
     * @param instanceId
     * @return
     * @see com.papla.cloud.wf.run.engine.core.handler.ActivityExecutionPathService#getLastExecutionActivityNode(java.lang.String)
     */
    @Override
    public ActivityBean getLastExecutionActivityNode(String instanceId) {
        // 取得本实例下最后(当前运行)的通知/会签结点信息
        ActivityBean lastActivityBean = activityExecutionPathDao.getLastExecutionActivityNode(instanceId);

        if (lastActivityBean == null) return null;
        lastActivityBean.setActivityExecutorList(activityExecutionPathDao.getActRealExecutor(lastActivityBean));

        return lastActivityBean;

    }

    /**
     * @param configExecutorList
     * @param finalExecutorList
     * @return boolean    返回类型
     * @Title: isAvoidActivity
     * @Description: 判断节点是否已经被系统规避 ; 如果系统被规避 ; 则不需要再执行审批处理
     */
    private boolean isAvoidActivity(List<ActivityExecutorBean> configExecutorList, List<ActivityExecutorBean> finalExecutorList) {
        if (finalExecutorList != null && finalExecutorList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

}
