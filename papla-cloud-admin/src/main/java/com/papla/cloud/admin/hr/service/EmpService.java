package com.papla.cloud.admin.hr.service;

import java.io.IOException;

import com.papla.cloud.common.mybatis.service.BaseService;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.hr.domain.Emp;

/**
* @Title: EmpService
* @Description: TODO 员工管理
* @author 
* @date 2021-09-27
* @version V1.0
*/
public interface EmpService extends BaseService<Emp> {

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<Emp> entitys, HttpServletResponse response) throws IOException;

}