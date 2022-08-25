package com.papla.cloud.admin.auth.mapper;

import com.papla.cloud.admin.auth.domain.Perm;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @Title:                Perm
* @Description: TODO   资源权限管理管理
* @author                
* @date                2021-09-21
* @version            V1.0
*/
public interface PermMapper extends BaseMapper<Perm>{

    /**
     * 分组查询 资源路径 与 角色映射关系 [{key:'POST:/admin/auth/user/save',value:'ADMIN,ROLE2'}]
     * @return
     */
    List<Map<String,String>> groupRolesByUrl();

    /**
     * 获取匿名访问资源路径
     * @return
     */
    Set<String> selectAnonymousUrl();

}