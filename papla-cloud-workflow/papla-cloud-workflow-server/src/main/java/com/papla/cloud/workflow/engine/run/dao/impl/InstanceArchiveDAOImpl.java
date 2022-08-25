package com.papla.cloud.workflow.engine.run.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.papla.cloud.workflow.engine.mapper.InstanceArchiveMapper;
import com.papla.cloud.workflow.engine.run.dao.InstanceArchiveDAO;

/**
 * @author linpeng
 * @ClassName: InstanceArchiveDAOImpl
 * @Description: 实例归档数据层接口
 * @date 2015年4月7日 上午10:21:37
 */
@Repository("instanceArchiveDAO")
public class InstanceArchiveDAOImpl implements InstanceArchiveDAO {
    @Resource
    private InstanceArchiveMapper instanceArchiveMapper;

    /*
     * (非 Javadoc)
     * <p>Title: createArchiveData</p>
     * <p>Description: 创建流程实例归档数据 </p>
     * @param instanceId
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.InstanceArchiveDAO#createArchiveData(java.lang.String)
     */
    @Override
    public void createArchiveData(String instanceId) throws Exception {
        // 归档流程实例
        try {
            instanceArchiveMapper.insertArchInstance(instanceId);
        } catch (Exception e) {
            throw new Exception("archived failure：" + e.getMessage());
        }

        // 归档流程实例实体属性
        try {
            instanceArchiveMapper.insertArchInsAttrValue(instanceId);
        } catch (Exception e) {
            throw new Exception("archived failure：" + e.getMessage());
        }

        // 归档流程实例节点状态
        try {
            instanceArchiveMapper.insertArchInsActState(instanceId);
        } catch (Exception e) {
            throw new Exception("archived failure：" + e.getMessage());
        }

        // 归档实例连线状态
        try {
            instanceArchiveMapper.insertArchInsTransState(instanceId);
        } catch (Exception e) {
            throw new Exception("archived failure：" + e.getMessage());
        }

        // 归档流程实例待办
        try {
            instanceArchiveMapper.insertArchInsTask(instanceId);
        } catch (Exception e) {
            throw new Exception("archived failure：" + e.getMessage());
        }

        // 归档流程实例动态通知节点审批队列信息
        try {
            instanceArchiveMapper.insertArchInsActQueue(instanceId);
        } catch (Exception e) {
            throw new Exception("archived failure：" + e.getMessage());
        }
    }

    /*
     * (非 Javadoc)
     * <p>Title: deleteArchivedData</p>
     * <p>Description: 删除流程实例归档数据 </p>
     * @param instanceId
     * @throws Exception
     * @see com.papla.cloud.wf.dao.engine.InstanceArchiveDAO#deleteArchivedData(java.lang.String)
     */
    @Override
    public void deleteArchivedData(String instanceId) throws Exception {

        // 删除流程实例待办
        try {
            instanceArchiveMapper.deleteInsTask(instanceId);
        } catch (Exception e) {
            throw new Exception("deleted failure：" + e.getMessage());
        }

        // 删除流程实例节点状态
        try {
            instanceArchiveMapper.deleteInsActState(instanceId);
        } catch (Exception e) {
            throw new Exception("deleted failure：" + e.getMessage());
        }

        // 删除流程实例动态通知节点审批队列信息
        try {
            instanceArchiveMapper.deleteDynaActQueue(instanceId);
        } catch (Exception e) {
            throw new Exception("deleted failure：" + e.getMessage());
        }

        // 删除流程实例连线状态
        try {
            instanceArchiveMapper.deleteInsTransState(instanceId);
        } catch (Exception e) {
            throw new Exception("deleted failure：" + e.getMessage());
        }

        // 删除流程实例实体属性
        try {
            instanceArchiveMapper.deleteInsAttrValues(instanceId);
        } catch (Exception e) {
            throw new Exception("deleted failure：" + e.getMessage());
        }

        // 删除流程实例
        try {
            instanceArchiveMapper.deleteProcessInstance(instanceId);
        } catch (Exception e) {
            throw new Exception("deleted failure：" + e.getMessage());
        }

    }
}
