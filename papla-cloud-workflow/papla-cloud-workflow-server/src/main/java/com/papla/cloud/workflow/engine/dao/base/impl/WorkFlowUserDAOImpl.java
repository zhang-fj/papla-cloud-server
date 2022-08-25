package com.papla.cloud.workflow.engine.dao.base.impl;

import com.papla.cloud.workflow.engine.dao.base.WorkFlowUserDAO;
import com.papla.cloud.workflow.engine.mapper.WorkFlowUserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author linpeng
 * @ClassName: WorkFlowUserDAOImpl
 * @Description: 工作流用户DAO实现类
 * @date 2015年4月27日 上午10:36:16
 */
@Repository("workFlowUserDAO")
public class WorkFlowUserDAOImpl implements WorkFlowUserDAO {

    @Resource
    private WorkFlowUserMapper workFlowUserMapper;

    @Override
    public String getEmpNameByUserId(String userId){
        return workFlowUserMapper.getEmpNameByUserId(userId);
    }

}
