package com.papla.cloud.admin.auth.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.auth.service.dto.UserDto;
import com.papla.cloud.admin.auth.domain.User;
import com.papla.cloud.common.mybatis.service.BaseService;

/**
* @Title:                UserService
* @Description: TODO   用戶管理管理
* @author                
* @date                2021-09-10
* @version            V1.0
*/
public interface UserService extends BaseService<User>{

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<User> entitys, HttpServletResponse response) throws IOException;

    UserDto loadByUserName(String username);

}