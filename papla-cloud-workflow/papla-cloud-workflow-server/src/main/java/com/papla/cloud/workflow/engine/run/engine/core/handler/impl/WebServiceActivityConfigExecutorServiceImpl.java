package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.JsonObjectUtil;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutors;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

@Service("webServiceActivityConfigExecutorService")
public class WebServiceActivityConfigExecutorServiceImpl extends AbstractActivityConfigExecutorServiceImpl {

    @Resource
    private ProcessEngineCommonService processEngineCommonService;

    public List<ActivityExecutorBean> getActivityConfigExecutorList(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) throws Exception {

        List<ActivityExecutorBean> executors = null;
        // 取得WebService执行串
        String wsdl = PropertyHandlerUtil.findActivityPropertyValue(activityBean, WorkFlowConstants.A_EXEC_WEB);

        try {
            // 取得字符信息
            String jsonStr = processEngineCommonService.invokeWebService(processInstanceBean, wsdl);

            ActivityExecutors activityExecutors = JsonObjectUtil.JSONToObj(jsonStr, ActivityExecutors.class);

            if ("0".equals(activityExecutors.getFlag())) {
                executors = activityExecutors.getExecutors();
            } else if ("1".equals(activityExecutors.getFlag())) {
                throw new Exception(activityExecutors.getMsg());
            }

        } catch (Exception e) {
            throw e;
        }

        return executors;
    }
}
