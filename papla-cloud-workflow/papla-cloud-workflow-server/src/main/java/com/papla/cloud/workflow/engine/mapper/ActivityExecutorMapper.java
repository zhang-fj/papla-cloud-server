package com.papla.cloud.workflow.engine.mapper;

import com.papla.cloud.workflow.engine.modal.ActivityExecutorBean;

import java.util.List;
import java.util.Map;

/**
 * @author linpeng
 * @ClassName: ActivityExecutorMapper
 * @Description: 节点执行人mapper
 * @date 2015年4月29日 下午12:22:30
 */
public interface ActivityExecutorMapper {

    /**
     * 查询节点执行人
     * @param map
     * @return
     */
    List<ActivityExecutorBean> getActivityExecutorList(Map<String, Object> map);

}
