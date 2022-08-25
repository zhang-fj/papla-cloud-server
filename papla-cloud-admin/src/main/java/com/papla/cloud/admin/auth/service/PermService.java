package com.papla.cloud.admin.auth.service;

import java.io.IOException;
import com.papla.cloud.common.mybatis.service.BaseService;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.auth.domain.Perm;

/**
* @Title:                PermService
* @Description: TODO   资源权限管理管理
* @author                
* @date                2021-09-21
* @version            V1.0
*/
public interface PermService extends BaseService<Perm>{

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<Perm> entitys, HttpServletResponse response) throws IOException;

}