package com.papla.cloud.workflow.engine.run.dao;

import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;

import java.util.List;

/**
 * ClassName:ActivityExecutorDao
 * Function: 节点执行人dao
 *
 * @author gongbinglai
 * @version Ver 1.0
 * @Date 2014    2014年7月3日		下午2:26:09
 * @since Ver 1.0
 */
public interface ActivityExecutorDao {

    /**
     * 根据怕【用户ID】数组查询【节点执行人】
     * @param userIds
     * @return
     */
    List<ActivityExecutorBean> getActivityExecutorListByUserIds(List<String> userIds);

    /**
     * 根据【岗位ID】数组查询【节点执行人】
     * @param postIds
     * @return
     */
    List<ActivityExecutorBean> getActivityExecutorListByPostIds(List<String> postIds);

}
