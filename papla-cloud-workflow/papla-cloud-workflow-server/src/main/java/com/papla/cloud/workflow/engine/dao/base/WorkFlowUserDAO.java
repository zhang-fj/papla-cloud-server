package com.papla.cloud.workflow.engine.dao.base;

public interface WorkFlowUserDAO {

    /**
     * 根据用户Id , 查找员工姓名
     * @param userId
     * @return
     * @throws Exception
     */
     String getEmpNameByUserId(String userId);

}
