package com.papla.cloud.workflow.engine.run.dao;

/**
 * @author linpeng
 * @ClassName: InstanceArchiveDAO
 * @Description: 实例归档数据层接口
 * @date 2015年4月7日 上午10:20:27
 */
public interface InstanceArchiveDAO {

    /**
     * @param instanceId
     * @return void    返回类型
     * @throws Exception
     * @Title: createArchiveData
     * @Description: 创建归档数据
     */
    void createArchiveData(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @return void    返回类型
     * @throws Exception
     * @Title: deleteArchivedData
     * @Description: 删除归档数据
     */
    void deleteArchivedData(String instanceId) throws Exception;
}
