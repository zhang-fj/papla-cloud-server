package com.papla.cloud.admin.auth.mapper;

import com.papla.cloud.admin.auth.domain.UserRole;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @Title:                UserRole
* @Description: TODO   角色管理管理
* @author                
* @date                2021-09-14
* @version            V1.0
*/
public interface UserRoleMapper extends BaseMapper<UserRole>{

    /**
     * 根据用户id获取用户默认角色
     * @param userId
     * @return
     */
    UserRole selectDefaultRoleByUserId(@Param("userId") String userId);

    /**
     * 根据用户id获取用户角色组
     * @param userId
     * @return
     */
    List<String> selectRolesByUserId(@Param("userId") String userId);

}