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

@Service("procActivityConfigExecutorService")
public class ProcedureActivityConfigExecutorServiceImpl extends AbstractActivityConfigExecutorServiceImpl {
    @Resource
    private ProcessEngineCommonService processEngineCommonService;

    /**
     * 通过调用Oracle存储过程获取节点配置的执行人信息
     * <result>
     * <flag>0</flag>
     * <msg>操作正确</msg>
     * <data>
     * <row>
     * <userId>e370f877086e4900b798a24939a0a6b5</userId>
     * <userName>admin</userName>
     * <empId>00E251FC376504C8E050A8C021821F64</empId>
     * <empCode>10001</empCode>
     * <empName>系统管理员</empName>
     * </row>
     * <row>
     * <userId>bfc540e4e1d14a2a857df072f96cf7ef</userId>
     * <userName>abc123</userName>
     * <empId>bde927d833ef45e38444da0529b106fc</empId>
     * <empCode>10001</empCode>
     * <empName>刘明</empName>
     * </row>
     * </data>
     * </result>
     */
    public List<ActivityExecutorBean> getActivityConfigExecutorList(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) throws Exception {
        List<ActivityExecutorBean> executors = null;
        // 取得存储过程执行串
        String proc = PropertyHandlerUtil.findActivityPropertyValue(activityBean, WorkFlowConstants.A_EXEC_PROC);

        try {
            // 取得字符信息
            String jsonStr = processEngineCommonService.callOracleProcedure(processInstanceBean, proc);

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
