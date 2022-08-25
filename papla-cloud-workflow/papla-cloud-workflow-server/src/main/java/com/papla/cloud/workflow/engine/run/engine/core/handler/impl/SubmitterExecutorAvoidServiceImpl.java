package com.papla.cloud.workflow.engine.run.engine.core.handler.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.util.PropertyHandlerUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;


/**
 * @author linpeng
 * @ClassName: SubmitterExecutorAvoidServiceImpl
 * @Description: 提交人规避
 * @date 2015年3月19日 上午10:52:26
 */
@Service("submitterExecutorAvoidService")
public class SubmitterExecutorAvoidServiceImpl extends AbstractActivityExecutorAvoidServiceImpl {

    /**
     * @param processInstanceBean
     * @return ActivityExecutorBean    返回类型
     * @Title: getSubmitter
     * @Description: 取得提交人
     */
    private ActivityExecutorBean getSubmitter(ProcessInstanceBean processInstanceBean) {
        // 取得提交人变量名
        String submitterPropertyCode = PropertyHandlerUtil.findPropertyValue(processInstanceBean.getDeployPropertyList(), WorkFlowConstants.P_SUBMITTER_VAR); // 提交人属性

        // 判断系统是否设置了提交人变量
        if (submitterPropertyCode != null && !"".equals(submitterPropertyCode)) {
            submitterPropertyCode = submitterPropertyCode.replace("${", "").replace("}", "");
        } else {
            submitterPropertyCode = WorkFlowConstants.E_SUBMITTER;
        }

        // 取得提交人
        String submitter = PropertyHandlerUtil.findEntityPropertyValue(processInstanceBean.getAttrList(), submitterPropertyCode);
        ActivityExecutorBean bean = new ActivityExecutorBean();
        bean.setUserId(submitter);
        return bean;
    }

    /*
     * (非 Javadoc)
     * <p>Title: avoidExecutorForMultiple</p>
     * <p>Description: 执行提交人规避处理 </p>
     * @param executorList
     * @param processInstanceBean
     * @return
     * @see com.papla.cloud.wf.run.engine.core.handler.impl.AbstractActivityExecutorAvoidServiceImpl#avoidExecutorForMultiple(java.util.List, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @Override
    protected List<ActivityExecutorBean> avoidExecutorForMultiple(List<ActivityExecutorBean> executorList, ProcessInstanceBean processInstanceBean) {
        // 取得最终执行人列表
        List<ActivityExecutorBean> finalExecutorList = new ArrayList<ActivityExecutorBean>();
        // 取得提交人
        ActivityExecutorBean submitterBean = this.getSubmitter(processInstanceBean);
        // 判断是否规避提交人
        for (ActivityExecutorBean aeb : executorList) {
            if (!aeb.equals(submitterBean)) finalExecutorList.add(aeb);
        }
        return finalExecutorList;
    }

}
