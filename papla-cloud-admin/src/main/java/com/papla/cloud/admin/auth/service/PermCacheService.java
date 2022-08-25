package com.papla.cloud.admin.auth.service;

/**
* @Title:              RoleService
* @Description: TODO   角色资源分配
* @author                
* @date                2021-09-14
* @version            V1.0
*/
public interface PermCacheService{

     void refreshRedisRoleUrls();

     void refreshRedisAnonymousUrls();

}