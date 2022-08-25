package com.papla.cloud.workflow.engine.run.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.papla.cloud.workflow.engine.mapper.ActivityExecutionPathMapper;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.DynaNodeQueueBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityExecutionPathDao;
import com.papla.cloud.workflow.engine.run.dao.ActivityExecutorDao;
import com.papla.cloud.workflow.engine.run.dao.ProcessInstanceDAO;

/**
 * @author linpeng
 * @ClassName: ActivityExecutionPathDaoImpl
 * @Description:节点执行路径dao实现类
 * @date 2015年4月16日 上午9:43:46
 */
@Repository("activityExecutionPathDao")
public class ActivityExecutionPathDaoImpl implements ActivityExecutionPathDao {

    @Autowired
    private ActivityExecutionPathMapper activityExecutionPathMapper;

    @Autowired
    private ProcessInstanceDAO processInstanceDAO;


    @Override
    public List<ActivityBean> getExecutePath(String instanceId) {
        return activityExecutionPathMapper.getExecutePath(instanceId);
    }

    @Override
    public List<ActivityBean> getExecutedPath(String instanceId) {
        return activityExecutionPathMapper.getExecutedPath(instanceId);
    }

    @Override
    public List<ActivityExecutorBean> getActRealExecutor(ActivityBean activityBean) {
        return activityExecutionPathMapper.getActRealExecutor(activityBean);
    }

    @Override
    public ActivityBean getLastExecutionActivityNode(String instanceId) {
        return activityExecutionPathMapper.getLastExecutionActivityNode(instanceId);
    }

    @Override
    public DynaNodeQueueBean getCurrentNodeQueue(ActivityBean activityBean) {
        String insCode = activityBean.getInstanceCode();
        try {
            if (insCode == null) {
                ProcessInstanceBean bean = processInstanceDAO.getRunBaseInstanceBeanById(activityBean.getInstanceId());
                if (bean != null) {
                    insCode = bean.getInstanceCode();
                }
            }
        } catch (Exception e) {
            insCode = null;
        }
        // 执行查询
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("insCode", insCode);
        map.put("actCode", activityBean.getCode());
        map.put("list", activityBean.getExcludeOpenNodes());

        return activityExecutionPathMapper.getCurrentNodeQueue(map);
    }

    @Override
    public DynaNodeQueueBean getRunningNodeQueue(ActivityBean activityBean) {
        String insCode = activityBean.getInstanceCode();
        try {
            if (insCode == null) {
                insCode = processInstanceDAO.getRunInstanceCodeById(activityBean.getInstanceId());
            }
        } catch (Exception e) {
            insCode = null;
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("insCode", insCode);
        map.put("actCode", activityBean.getCode());

        return activityExecutionPathMapper.getRunningNodeQueue(map);
    }

    @Override
    public DynaNodeQueueBean getFirstClosedNodeQueue(ActivityBean activityBean) {
        String insCode = activityBean.getInstanceCode();
        try {
            if (insCode == null) {
                insCode = processInstanceDAO.getRunInstanceCodeById(activityBean.getInstanceId());
            }
        } catch (Exception e) {
            insCode = null;
        }
        // 执行查询
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("insCode", insCode);
        map.put("actCode", activityBean.getCode());

        return activityExecutionPathMapper.getFirstClosedNodeQueue(map);
    }

    @Override
    public List<ActivityExecutorBean> getConfigExecutorList(String approverId) {
        return activityExecutionPathMapper.getExecutors(approverId);
    }

}
