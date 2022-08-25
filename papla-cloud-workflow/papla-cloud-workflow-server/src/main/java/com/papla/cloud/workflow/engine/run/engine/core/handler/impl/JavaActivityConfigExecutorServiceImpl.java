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

@Service("javaActivityConfigExecutorService")
public class JavaActivityConfigExecutorServiceImpl extends AbstractActivityConfigExecutorServiceImpl {

    @Resource
    private ProcessEngineCommonService processEngineCommonService;

    /*
     * (非 Javadoc)
     * <p>Title: getActivityConfigExecutorList</p>
     * <p>Description: 通过调用Java方法获取节点配置的执行人信息 </p>
     * @param activityBean
     * @param processInstanceBean
     * @return
     *
     * 返回值说明：
     *	<result>
     *	  <flag>0</flag>
     *	  <msg>操作正确</msg>
     *	  <data>
     *	    <row>
     *	      <userId>e370f877086e4900b798a24939a0a6b5</userId>
     *	      <userName>admin</userName>
     *	      <empId>00E251FC376504C8E050A8C021821F64</empId>
     *	      <empCode>10001</empCode>
     *		  <empName>系统管理员</empName>
     *	    </row>
     *	    <row>
     *	      <userId>bfc540e4e1d14a2a857df072f96cf7ef</userId>
     *	      <userName>abc123</userName>
     *	      <empId>bde927d833ef45e38444da0529b106fc</empId>
     *        <empCode>10001</empCode>
     *	      <empName>刘明</empName>
     *	    </row>
     *	  </data>
     *	</result>
     *
     * @throws Exception
     * @see com.papla.cloud.wf.run.engine.core.handler.ActivityConfigExecutorService#getActivityConfigExecutorList(com.papla.cloud.wf.modal.ActivityBean, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @Override
    public List<ActivityExecutorBean> getActivityConfigExecutorList(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) throws Exception {

        List<ActivityExecutorBean> executors = null;

        // 取得Java串信息
        String cls = PropertyHandlerUtil.findActivityPropertyValue(activityBean, WorkFlowConstants.A_MORE_JAVA);

        try {

            String jsonStr = processEngineCommonService.invokeJavaMethod(processInstanceBean, cls);

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
