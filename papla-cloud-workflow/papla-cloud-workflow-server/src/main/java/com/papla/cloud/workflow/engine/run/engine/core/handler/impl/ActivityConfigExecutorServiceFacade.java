package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.engine.core.handler.ActivityConfigExecutorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName:ActivityConfigExecutorService
 * Function: 节点配置执行人服务类
 *
 * @author gongbinglai
 * @version Ver 1.0
 * @Date 2014    2014年7月3日		下午2:56:33
 * @since Ver 1.0
 */
@Service("activityConfigExecutorServiceFacade")
public class ActivityConfigExecutorServiceFacade {


    @Resource
    private ActivityConfigExecutorService staticActivityConfigExecutorService;

    @Resource
    private ActivityConfigExecutorService varActivityConfigExecutorService;

    @Resource
    private ActivityConfigExecutorService sqlActivityConfigExecutorService;

    @Resource
    private ActivityConfigExecutorService procActivityConfigExecutorService;

    @Resource
    private ActivityConfigExecutorService webServiceActivityConfigExecutorService;

    @Resource
    private ActivityConfigExecutorService javaActivityConfigExecutorService;

    /**
     * getActivityConfigExecutorList:取得节点配置执行人列表
     *
     * @param activityBean
     * @return
     * @author gongbinglai
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public List<ActivityExecutorBean> getActivityConfigExecutorList(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) throws Exception {

        processInstanceBean.getAppBean();

        String executorType = PropertyHandlerUtil.findPropertyValue(activityBean.getActPropertyBeanList(), WorkFlowConstants.A_EXECUTOR_TYPE);

        if (WorkFlowConstants.EXECUTOR_TYPE_SU.equals(executorType) || WorkFlowConstants.EXECUTOR_TYPE_SP.equals(executorType)) {
            // 静态用户或静态岗位
            return staticActivityConfigExecutorService.getActivityConfigExecutorList(activityBean, processInstanceBean);

        } else if (WorkFlowConstants.EXECUTOR_TYPE_VAR.equals(executorType)) {
            // 变量类型
            return varActivityConfigExecutorService.getActivityConfigExecutorList(activityBean, processInstanceBean);

        } else if (WorkFlowConstants.EXECUTOR_TYPE_PROC.equals(executorType)) {
            // 存储过程
            return procActivityConfigExecutorService.getActivityConfigExecutorList(activityBean, processInstanceBean);

        } else if (WorkFlowConstants.EXECUTOR_TYPE_SQL.equals(executorType)) {
            // SQL类型
            return sqlActivityConfigExecutorService.getActivityConfigExecutorList(activityBean, processInstanceBean);

        } else if (WorkFlowConstants.EXECUTOR_TYPE_WEB.equals(executorType)) {
            // WEB服务
            return webServiceActivityConfigExecutorService.getActivityConfigExecutorList(activityBean, processInstanceBean);

        } else if (WorkFlowConstants.EXECUTOR_TYPE_JAVA.equals(executorType)) {
            // JAVA方法
            return javaActivityConfigExecutorService.getActivityConfigExecutorList(activityBean, processInstanceBean);
        }

        return null;
    }
}
