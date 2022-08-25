package com.papla.cloud.admin.auth.service;

import java.io.IOException;

import com.papla.cloud.admin.auth.service.dto.RoleAuthDTO;
import com.papla.cloud.common.mybatis.service.BaseService;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.auth.domain.Role;

/**
* @Title:                RoleService
* @Description: TODO   角色管理管理
* @author                
* @date                2021-09-14
* @version            V1.0
*/
public interface RoleService extends BaseService<Role>{

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<Role> entitys, HttpServletResponse response) throws IOException;

    void auth(RoleAuthDTO dto);

    List<String> loadAuthPerms(Map<String, Object> params);

    List<String> loadAuthMenus(Map<String, Object> params);
}