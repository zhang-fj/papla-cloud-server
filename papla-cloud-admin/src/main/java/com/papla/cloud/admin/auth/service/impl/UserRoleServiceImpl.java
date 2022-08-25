package com.papla.cloud.admin.auth.service.impl;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;

import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.admin.auth.domain.UserRole;
import com.papla.cloud.admin.auth.mapper.UserRoleMapper;
import com.papla.cloud.admin.auth.service.UserRoleService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
* @Title:                UserRoleServiceImpl
* @Description: TODO   角色管理管理
* @author                
* @date                2021-09-14
* @version            V1.0
*/
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final UserRoleMapper mapper;

	@Override
	public BaseMapper<UserRole> getMapper(){
		return mapper;
	}

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public List<UserRole> auth(List<UserRole> entitys){

	    if(entitys != null){
	        for(UserRole userRole:entitys){
                Map<String,Object> params = new HashMap<String,Object>();
                params.put("userId",userRole.getUserId());
                params.put("roleId",userRole.getRoleId());
                // 判断用户是否拥有该角色，如果有则不新增授权
                if(mapper.selectCount(params)==0){
                    params.remove("roleId");
                    // 判断用户是否拥有其他角色，如果没有则将角色设置为默认角色
                    if(mapper.selectCount(params) == 0){
                        userRole.setIsDefault("Y");
                    }
                    insert(userRole);
                }

            }
        }
        return entitys;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void defaultRole(UserRole entity){
        UserRole ur= selectByPK(entity.getId());
        // 获取默认用户
        UserRole dur=mapper.selectDefaultRoleByUserId(ur.getUserId());
        if(dur != null){// 取消用户原默认角色
            dur.setIsDefault("N");
            update(dur);
        }
        // 设置新的用户默认角色
        ur.setIsDefault("Y");
        update(ur);
    }

    @Override
	public void download(List<UserRole> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (UserRole userRole : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("用户ID", userRole.getUserId());
			map.put("角色ID", userRole.getRoleId());
			map.put("默认角色：Y-是；N-否", userRole.getIsDefault());
			map.put("创建日期", userRole.getCreateDt());
			map.put("创建人", userRole.getCreateBy());
			map.put("最后更新日期", userRole.getUpdateDt());
			map.put("最后更新人", userRole.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
