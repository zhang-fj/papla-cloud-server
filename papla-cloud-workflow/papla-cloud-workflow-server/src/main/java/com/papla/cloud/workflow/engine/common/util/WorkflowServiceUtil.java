package com.papla.cloud.workflow.engine.common.util;

import java.util.ArrayList;
import java.util.List;

import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;

/**
 * @ClassName: WorkflowServiceUtil
 * @Description: 工作流服务工具类
 * @date 2016年11月8日 上午9:24:27
 */
public class WorkflowServiceUtil {

    /**
     * 合并节点执行人集合，将同一userId的执行人的orgId和orgName 合并
     * userId =1 orgId = 1 orgName = "组织1" ，userId =1 orgId = 2 orgName = "组织2" 的两条记录
     * 将会合并为  userId =1 orgId = 1，2 orgName = "组织1，组织2"
     *
     * @param properytBeanList
     * @param prorpertyCode
     * @return
     */
    public static List<ActivityExecutorBean> joinUserOrgs(List<ActivityExecutorBean> userList) {
        List<ActivityExecutorBean> newList = new ArrayList<ActivityExecutorBean>();

        for (ActivityExecutorBean b : userList) {
            ActivityExecutorBean t = isContent(b, newList);
            if (t == null) {
                newList.add(b);
            } else {
                t.setOrgId(t.getOrgId() + "," + b.getOrgId());
                t.setOrgName(t.getOrgName() + "," + b.getOrgName());
            }
        }
        return newList;
    }

    /**
     * 判断节点执行人集合中是否包含useriD一致的执行人
     *
     * @param bean
     * @param userList
     * @return
     */
    public static ActivityExecutorBean isContent(ActivityExecutorBean bean, List<ActivityExecutorBean> userList) {
        for (ActivityExecutorBean b : userList) {
            if (b.getUserId().equals(bean.getUserId())) {
                return b;
            }
        }
        return null;
    }

    /**
     * isExecutorInActivityConfigExecutorList:判断节点执行人是否在节点执行人列表
     * @param bean
     * @param list
     * @return
     * @author gongbinglai
     * @version Ver 1.0
     * @since Ver 1.0
     */
    public static boolean isExecutorInActivityExecutorList(ActivityExecutorBean bean, List<ActivityExecutorBean> list) {
        for (ActivityExecutorBean ae : list) {
            if (ae.getUserId().equals(bean.getUserId())) return true;
        }
        return false;
    }

}
