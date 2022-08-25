package com.papla.cloud.workflow.cfg.service;

import java.io.IOException;
import com.papla.cloud.common.mybatis.service.BaseService;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.workflow.cfg.domain.AppParam;

/**
* @Title: AppParamService
* @Description: TODO 应用参数管理
* @author 
* @date 2021-10-15
* @version V1.0
*/
public interface AppParamService extends BaseService<AppParam>{

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<AppParam> entitys, HttpServletResponse response) throws IOException;

}