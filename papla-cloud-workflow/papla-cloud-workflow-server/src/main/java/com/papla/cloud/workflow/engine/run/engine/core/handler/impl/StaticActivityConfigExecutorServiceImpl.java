package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityExecutorDao;


/**
 * @author linpeng
 * @ClassName: StaticActivityConfigExecutorServiceImpl
 * @Description: 静态节点执行人服务类
 * @date 2015年5月12日 上午11:11:56
 */
@Service("staticActivityConfigExecutorService")
public class StaticActivityConfigExecutorServiceImpl extends AbstractActivityConfigExecutorServiceImpl {

    @Resource
    private ActivityExecutorDao activityExecutorDao;

    /*
     * (非 Javadoc)
     * <p>Title: getActivityConfigExecutorList</p>
     * <p>Description: 取得节点配置执行人信息 </p>
     * @param activityBean
     * @param processInstanceBean
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.run.engine.core.handler.ActivityConfigExecutorService#getActivityConfigExecutorList(com.papla.cloud.wf.modal.ActivityBean, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public List<ActivityExecutorBean> getActivityConfigExecutorList(ActivityBean activityBean, ProcessInstanceBean processInstanceBean) throws Exception {
        // 取得节点执行人类型
        String executorType = PropertyHandlerUtil.findPropertyValue(activityBean.getActPropertyBeanList(), WorkFlowConstants.A_EXECUTOR_TYPE);
        List<ActivityExecutorBean> activityExectorList = null;

        if (WorkFlowConstants.EXECUTOR_TYPE_SU.equals(executorType)) {

            // 取得静态用户ID列表
            List<String> userList = PropertyHandlerUtil.findActivityPVEnumValue(activityBean.getActPropEnumList(), WorkFlowConstants.A_STATIC_USER);
            if (userList != null && userList.size() > 0) {
                activityExectorList = activityExecutorDao.getActivityExecutorListByUserIds(userList);
            }

        } else if (WorkFlowConstants.EXECUTOR_TYPE_SP.equals(executorType)) {

            // 取得静态岗位ID列表
            List<String> postIds = PropertyHandlerUtil.findActivityPVEnumValue(activityBean.getActPropEnumList(), WorkFlowConstants.A_STATIC_POSTION);
            if (postIds != null && postIds.size() > 0) {
                activityExectorList = activityExecutorDao.getActivityExecutorListByPostIds(postIds);
            }
        }

        return activityExectorList;
    }
}
