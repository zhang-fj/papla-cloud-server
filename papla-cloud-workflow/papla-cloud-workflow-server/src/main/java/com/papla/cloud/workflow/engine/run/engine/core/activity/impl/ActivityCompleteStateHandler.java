package com.papla.cloud.workflow.engine.run.engine.core.activity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ActivityStateBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;
import com.papla.cloud.workflow.engine.run.engine.core.activity.ActivityStateHandler;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ActivityCompleteStateHandler.java
 * @Package com.papla.cloud.wf.run.engine.core.activity.impl
 * @Description: 节点完成处理类
 * @date 2021年8月29日 下午12:45:51
 */
@Service("activityCompleteStateHandler")
public class ActivityCompleteStateHandler extends ActivityStateHandler {

    @Autowired
    private ProcessEngineCommonService processEngineCommonService;
    @Autowired
    private ActivityDAO activityDAO;

    @Override
    public void toStart(ActivityBean bean) throws Exception {
    }

    @Override
    public void toRun(ActivityBean bean) throws Exception {

        Date date = CurrentUserUtil.getCurrentDate();

        // 更新节点最新状态标志
        ActivityStateBean updActStateBean = setActivityStateParam(bean, null, null, null, date);
        updActStateBean.setIsLastStatus("N");
        this.getActivityDAO().updateInsActState(updActStateBean);

        // 新增节点状态
        ActivityStateBean insActStateBean = new ActivityStateBean();
        BeanUtils.copyProperties(updActStateBean, insActStateBean);

        insActStateBean.setActState(WorkFlowConstants.NODE_RUNNING_STATE);
        insActStateBean.setBeginDate(date);
        insActStateBean.setEndDate(null);
        insActStateBean.setCreateBy(CurrentUserUtil.getCurrentUserId());
        insActStateBean.setCreateDt(date);

        this.getActivityDAO().insertInsActState(insActStateBean);

        // 如果当前节点为再次运行的动态通知节点且存在审批队列，则须将其动态审批队列的状态全部修改为open状态- 20170215
        if ("Y".equals(processEngineCommonService.isDynamicNode(bean)) && bean.isAgainRunning()) {
            // 参数
            List<String> actCodeList = new ArrayList<String>();
            actCodeList.add(bean.getCode());
            // 修改状态
            activityDAO.batchUpDynaActQueueStatus(bean.getInstanceCode(), actCodeList);
        }
    }

    @Override
    public void toCancel(ActivityBean bean) throws Exception {
    }

    @Override
    public void toTerminate(ActivityBean bean) throws Exception {
    }

    @Override
    public void toComplete(ActivityBean bean) throws Exception {
        Date date = CurrentUserUtil.getCurrentDate();

        // 更新节点最新状态标志
        ActivityStateBean updActStateBean = this.setActivityStateParam(bean, null, null, null, date);
        updActStateBean.setIsLastStatus("N");
        this.getActivityDAO().updateInsActState(updActStateBean);

        // 新增节点状态
        ActivityStateBean insActStateBean = new ActivityStateBean();
        BeanUtils.copyProperties(updActStateBean, insActStateBean);

        insActStateBean.setActState(WorkFlowConstants.NODE_COMPLETED_STATE);
        insActStateBean.setBeginDate(date);
        insActStateBean.setEndDate(date);
        insActStateBean.setCreateBy(CurrentUserUtil.getCurrentUserId());
        insActStateBean.setCreateDt(date);

        this.getActivityDAO().insertInsActState(insActStateBean);
    }
}
