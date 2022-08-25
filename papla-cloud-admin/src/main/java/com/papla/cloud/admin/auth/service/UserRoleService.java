package com.papla.cloud.admin.auth.service;

import java.io.IOException;
import com.papla.cloud.common.mybatis.service.BaseService;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.auth.domain.UserRole;

/**
* @Title:                UserRoleService
* @Description: TODO   角色管理管理
* @author                
* @date                2021-09-14
* @version            V1.0
*/
public interface UserRoleService extends BaseService<UserRole>{


    /**
     *  用户角色授权
     * @param entitys 授权数据
     * @throws IOException
     */
    List<UserRole> auth(List<UserRole> entitys);

    /**
     *  设置默认角色
     * @param entity
     * @throws IOException
     */
    void defaultRole(UserRole entity);

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<UserRole> entitys, HttpServletResponse response) throws IOException;


}