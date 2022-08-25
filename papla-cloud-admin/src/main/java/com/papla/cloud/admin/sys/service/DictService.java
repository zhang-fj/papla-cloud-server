package com.papla.cloud.admin.sys.service;

import java.io.IOException;

import com.papla.cloud.common.mybatis.service.BaseService;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.sys.domain.Dict;

/**
* @Title:                DictService
* @Description: TODO   字典管理管理
* @author                
* @date                2021-09-13
* @version            V1.0
*/
public interface DictService extends BaseService<Dict> {

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<Dict> entitys, HttpServletResponse response) throws IOException;

}