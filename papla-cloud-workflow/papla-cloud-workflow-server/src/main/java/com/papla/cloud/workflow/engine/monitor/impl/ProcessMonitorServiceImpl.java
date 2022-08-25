package com.papla.cloud.workflow.engine.monitor.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.G6Service;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.monitor.ProcessMonitorService;
import com.papla.cloud.workflow.engine.run.dao.ProcessInstanceDAO;

@Service("processMonitorServiceImpl")
public class ProcessMonitorServiceImpl implements ProcessMonitorService {


    @Resource
    private G6Service g6Service;

    @Resource
    private ProcessInstanceDAO processInstanceDAO;


    @Override
    public Map<String, Object> getProcessJsonByInstanceCode(String instanceCode) throws Exception {
        ProcessInstanceBean processInstanceBean = processInstanceDAO.getBaseInstanceBeanByCode(instanceCode);
        return g6Service.buildMonitorG6(processInstanceBean.getInstanceId(), processInstanceBean.getProcessJson(), processInstanceBean.isQueryArch());
    }


    @Override
    public Map<String, Object> getProcessJsonByTaskId(String taskId) throws Exception {
        ProcessInstanceBean processInstanceBean = processInstanceDAO.getBaseInstanceBeanByTaskId(taskId);
        return g6Service.buildMonitorG6(processInstanceBean.getInstanceId(), processInstanceBean.getProcessJson(), processInstanceBean.isQueryArch());
    }

}
