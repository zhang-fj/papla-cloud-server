package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityExecutorDao;

/**
 * ClassName:VarActivityConfigExecutorServiceImpl
 * Function: 节点执行人类型为变量类型
 *
 * @author gongbinglai
 * @version Ver 1.0
 * @Date 2014    2014年7月4日		上午9:56:45
 * @since Ver 1.0
 */
@Service("varActivityConfigExecutorService")
public class VarActivityConfigExecutorServiceImpl extends StaticActivityConfigExecutorServiceImpl {

    @Resource
    private ProcessEngineCommonService processEngineCommonService;
    @Resource
    private ActivityExecutorDao activityExecutorDao;

    public List<ActivityExecutorBean> getActivityConfigExecutorList(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) throws Exception {
        String executorVar = PropertyHandlerUtil.findPropertyValue(activityBean.getActPropertyBeanList(), WorkFlowConstants.A_EXEC_VAR);  //执行人变量
        String users = processEngineCommonService.replaceVariable(executorVar, processInstanceBean.getAttrList(), activityBean);
        if (users == null || "".equals(users)) return null;
        String[] userArray = users.split(",");
        return activityExecutorDao.getActivityExecutorListByUserIds(Stream.of(userArray).collect(Collectors.toList()));
    }
}
