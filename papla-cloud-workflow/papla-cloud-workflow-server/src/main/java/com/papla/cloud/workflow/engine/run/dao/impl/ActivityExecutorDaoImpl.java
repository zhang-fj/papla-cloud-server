package com.papla.cloud.workflow.engine.run.dao.impl;

import com.papla.cloud.workflow.engine.mapper.ActivityExecutorMapper;
import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityExecutorDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:ActivityExecutorDaoImpl
 * Function: 节点执行人dao
 */
@Repository("activityExecutorDao")
public class ActivityExecutorDaoImpl implements ActivityExecutorDao {

    @Resource
    private ActivityExecutorMapper activityExecutorMapper;

    @Override
    public List<ActivityExecutorBean> getActivityExecutorListByUserIds(List<String> userIds) {
        Map<String,Object> params = new HashMap();
        if(userIds != null&& userIds.size()>0){
            params.put("userIds",userIds);
        }else{
            params.put("not_data",true);
        }
        return activityExecutorMapper.getActivityExecutorList(params);
    }

    @Override
    public List<ActivityExecutorBean> getActivityExecutorListByPostIds(List<String> postIds) {
        Map<String,Object> params = new HashMap();
        if(postIds != null&& postIds.size()>0){
            params.put("postIds",postIds);
        }else{
            params.put("not_data",true);
        }
        return activityExecutorMapper.getActivityExecutorList(params);
    }
}
