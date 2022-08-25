package com.papla.cloud.workflow.demo.service;

import java.io.IOException;
import com.papla.cloud.common.mybatis.service.BaseService;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.workflow.demo.domain.Demo;

/**
* @Title: DemoService
* @Description: TODO 流程演示管理
* @author 
* @date 2021-10-03
* @version V1.0
*/
public interface DemoService extends BaseService<Demo>{

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<Demo> entitys, HttpServletResponse response) throws IOException;

}