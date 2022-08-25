package com.papla.cloud.workflow.engine.run.engine.core.arch;

/**
 * @author linpeng
 * @ClassName: InstanceArchiveService
 * @Description: 流程实例数据归档处理
 * @date 2015年4月7日 上午10:19:15
 */
public interface InstanceArchiveService {

    /**
     * @param instanceId
     * @return void    返回类型
     * @throws Exception
     * @Title: archiveCompletedInstance
     * @Description: 流程归档处理
     */
    public void archiveCompletedInstance(String instanceId) throws Exception;

}
