package com.papla.cloud.admin.hr.service;

import java.io.IOException;

import com.papla.cloud.common.mybatis.service.BaseService;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.hr.domain.EmpAsg;

/**
* @Title: EmpAsgService
* @Description: TODO 员工分配管理
* @author 
* @date 2021-09-27
* @version V1.0
*/
public interface EmpAsgService extends BaseService<EmpAsg> {

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<EmpAsg> entitys, HttpServletResponse response) throws IOException;

}