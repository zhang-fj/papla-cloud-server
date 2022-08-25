package com.papla.cloud.workflow.cfg.service;

import com.papla.cloud.common.mybatis.service.BaseService;
import com.papla.cloud.workflow.cfg.domain.Dict;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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