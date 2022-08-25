package com.papla.cloud.workflow.engine.run.engine.core.arch.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.papla.cloud.workflow.engine.run.dao.InstanceArchiveDAO;
import com.papla.cloud.workflow.engine.run.engine.core.arch.InstanceArchiveService;

/**
 * @author linpeng
 * @ClassName: InstanceArchiveServiceImpl
 * @Description: 流程实例数据归档处理
 * @date 2015年4月7日 上午10:19:43
 */
@Service("instanceArchiveService")
public class InstanceArchiveServiceImpl implements InstanceArchiveService {

    @Resource
    private InstanceArchiveDAO instanceArchiveDAO;

    /*
     * (非 Javadoc)
     * <p>Title: archiveCompletedInstance</p>
     * <p>Description: 流程归档处理  </p>
     * @param instanceId
     * @throws Exception
     * @see com.papla.cloud.wf.run.engine.core.arch.InstanceArchiveService#archiveCompletedInstance(java.lang.String)
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void archiveCompletedInstance(String instanceId) throws Exception {
        // 1.创建流程实例归档数据信息
        try {
            instanceArchiveDAO.createArchiveData(instanceId);
        } catch (Exception e) {
            throw new Exception("Archive failure：" + e.getMessage());
        }
        // 2.删除归档后的流程实例数据信息
        try {
            instanceArchiveDAO.deleteArchivedData(instanceId);
        } catch (Exception e) {
            throw new Exception("Archive failure：" + e.getMessage());
        }
    }

}
