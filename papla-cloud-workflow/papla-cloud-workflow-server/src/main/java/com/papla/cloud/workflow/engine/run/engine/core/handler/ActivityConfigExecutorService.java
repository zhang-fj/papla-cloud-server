package com.papla.cloud.workflow.engine.run.engine.core.handler;

import java.util.List;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

/**
 * @author linpeng
 * @ClassName: ActivityConfigExecutorService
 * @Description: 节点配置执行人服务类
 * @date 2015年5月12日 上午11:11:40
 */
public interface ActivityConfigExecutorService {

    /**
     * @param activityBean
     * @param processInstanceBean
     * @return List<ActivityExecutorBean>    返回类型
     * @throws Exception
     * @Title: getActivityConfigExecutorList
     * @Description: 取得节点配置执行人列表
     */
     List<ActivityExecutorBean> getActivityConfigExecutorList(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) throws Exception;
}
