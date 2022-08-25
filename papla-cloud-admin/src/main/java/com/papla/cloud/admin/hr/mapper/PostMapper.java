package com.papla.cloud.admin.hr.mapper;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.admin.hr.domain.Post;

import java.util.Map;

/**
* @Title: Post
* @Description: TODO 岗位管理
* @author 
* @date 2021-09-26
* @version V1.0
*/
public interface PostMapper extends BaseMapper<Post> {

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