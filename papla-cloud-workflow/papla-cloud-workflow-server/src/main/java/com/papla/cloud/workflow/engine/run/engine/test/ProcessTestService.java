package com.papla.cloud.workflow.engine.run.engine.test;

import java.util.List;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ProcessBean;

/**
 * @author linpeng
 * @ClassName: ProcessTestService
 * @Description: 流程测试服务接口
 * @date 2015年4月16日 下午2:05:33
 */
public interface ProcessTestService {

    /**
     * @param activityBean
     * @param processBean
     * @return List<ActivityBean>    返回类型
     * @throws Exception
     * @Title: getExecutionActPathAndExecutors
     * @Description: 取得节点执行路径及节点执行人信息
     */
    public List<ActivityBean> getExecutionActPathAndExecutors(ActivityBean activityBean, ProcessBean processBean) throws Exception;
}
