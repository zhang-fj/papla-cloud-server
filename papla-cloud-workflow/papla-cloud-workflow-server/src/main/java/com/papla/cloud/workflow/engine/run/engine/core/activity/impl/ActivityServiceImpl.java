package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;
import com.papla.cloud.workflow.engine.run.dao.TransitionDAO;
import com.papla.cloud.workflow.engine.run.engine.core.activity.ActivityService;
import com.papla.cloud.workflow.engine.run.engine.core.controller.ActionEvent;
import com.papla.cloud.workflow.engine.run.engine.core.controller.EngineEvent;
import com.papla.cloud.workflow.engine.run.engine.core.controller.EngineEventObserver;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ActivityServiceImpl.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 运行时【流程节点】处理服务实现类
 * @date 2021年8月29日 下午12:13:48
 */
@Service("activityService")
public class ActivityServiceImpl extends EngineEventObserver implements ActivityService {

    @Resource
    private ActivityDAO activityDAO;
    @Resource
    private TransitionDAO transitionDAO;
    @Resource
    private EngineEvent engineEvent;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doStartActivity(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();
            // 启动节点
            bean.getCurrentActivityHandler().doStart(bean);

        } catch (Exception e) {
            throw e;
        }
    }

    ;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doRunActivity(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();
            // 运行节点
            bean.getCurrentActivityHandler().doRun(bean);

        } catch (Exception e) {
            throw e;
        }
    }

    ;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doCancelActivity(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();

            // 将节点状态修改为撤销状态
            activityDAO.cancelInsActState(bean);

            // 清除节点连线信息
            transitionDAO.deleteInsTransState(bean.getInstanceId());

            // 关闭待办信息
            ActionEvent event = new ActionEvent(bean, "doCancelWorkItem");
            engineEvent.notifyObserver(event);

        } catch (Exception e) {
            throw e;
        }
    }

    ;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doRejectActivity(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();

            // 清除节点状态信息
            activityDAO.deleteInsActState(bean);

            // 清除动态节点审批队列信息
            activityDAO.delDynaActivityQueue(bean.getInstanceCode());

            // 清除节点连线状态信息
            transitionDAO.deleteInsTransState(bean.getInstanceId());

            // 关闭待办信息
            ActionEvent event = new ActionEvent(bean, "doRejectWorkItem");
            engineEvent.notifyObserver(event);

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doTerminateActivity(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();
            // 终止节点
            bean.getCurrentActivityHandler().doTerminate(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    ;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void doCompleteActivity(ActionEvent actionEvent) throws Exception {
        try {
            // 流程实例Bean
            ProcessInstanceBean bean = actionEvent.getBean();
            // 完成节点
            bean.getCurrentActivityHandler().doComplete(bean);
        } catch (Exception e) {
            throw e;
        }
    }

    ;
}
