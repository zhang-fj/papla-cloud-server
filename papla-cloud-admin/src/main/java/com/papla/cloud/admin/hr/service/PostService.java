package com.papla.cloud.admin.hr.service;

import java.io.IOException;

import com.papla.cloud.admin.hr.domain.Post;
import com.papla.cloud.common.mybatis.service.BaseService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
* @Title: PostService
* @Description: TODO 岗位管理
* @author 
* @date 2021-09-26
* @version V1.0
*/
public interface PostService extends BaseService<Post> {

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<Post> entitys, HttpServletResponse response) throws IOException;


    /**
     * 新增员工岗位分配
     * @param params
     */
    void insertEmpPostAsg(Map<String,Object> params);

    /**
     * 删除员工岗位分配
     */
    void deleteEmpPostAsg(Map<String,Object> params);

}