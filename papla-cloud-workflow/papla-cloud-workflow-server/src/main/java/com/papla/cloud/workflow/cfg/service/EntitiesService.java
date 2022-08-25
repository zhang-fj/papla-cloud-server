package com.papla.cloud.workflow.cfg.service;

import java.io.IOException;
import com.papla.cloud.common.mybatis.service.BaseService;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.workflow.cfg.domain.Entities;

/**
* @Title: EntitiesService
* @Description: TODO 流程实体管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
public interface EntitiesService extends BaseService<Entities>{

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<Entities> entitys, HttpServletResponse response) throws IOException;

}