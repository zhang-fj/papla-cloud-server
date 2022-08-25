package com.papla.cloud.workflow.engine.mapper;

public interface WorkFlowUserMapper {

    /**
     * @param userId
     * @return String    返回类型
     * @Title: getEmpNameByUserId
     * @Description: 根据用户Id , 查找员工姓名
     */
     String getEmpNameByUserId(String userId);


}
