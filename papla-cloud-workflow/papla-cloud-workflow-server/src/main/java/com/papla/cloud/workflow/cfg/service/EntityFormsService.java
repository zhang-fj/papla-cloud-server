package com.papla.cloud.workflow.cfg.service;

import java.io.IOException;
import com.papla.cloud.common.mybatis.service.BaseService;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.workflow.cfg.domain.EntityForms;

/**
* @Title: EntityFormsService
* @Description: TODO 实体表单管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
public interface EntityFormsService extends BaseService<EntityForms>{

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<EntityForms> entitys, HttpServletResponse response) throws IOException;

}