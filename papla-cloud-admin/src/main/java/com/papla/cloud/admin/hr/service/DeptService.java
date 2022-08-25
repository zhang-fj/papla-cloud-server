package com.papla.cloud.admin.hr.service;

import java.io.IOException;

import com.papla.cloud.common.mybatis.service.BaseService;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.hr.domain.Dept;

/**
* @Title: DeptService
* @Description: TODO 部门管理
* @author 
* @date 2021-09-26
* @version V1.0
*/
public interface DeptService extends BaseService<Dept> {

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<Dept> entitys, HttpServletResponse response) throws IOException;

}