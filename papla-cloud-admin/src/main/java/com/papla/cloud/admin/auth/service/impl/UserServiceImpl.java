package com.papla.cloud.admin.auth.service.impl;

import com.papla.cloud.admin.auth.domain.User;
import com.papla.cloud.admin.auth.domain.UserRole;
import com.papla.cloud.admin.auth.mapper.UserMapper;
import com.papla.cloud.admin.auth.mapper.UserRoleMapper;
import com.papla.cloud.admin.auth.service.UserService;
import com.papla.cloud.admin.auth.service.dto.UserDto;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
* @Title:                UserServiceImpl
* @Description: TODO   用戶管理管理
* @author                
* @date                2021-09-10
* @version            V1.0
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger()  {
		return logger;
	}

    private final UserMapper mapper;
	private final UserRoleMapper userRoleMapper;

	@Override
	public BaseMapper<User> getMapper(){
		return mapper;
	}

    @Override
    public Integer insert(User entity){
	    entity.setPassword("$2a$10$F2ALbdd81xu7KcaGxxrX/uIzS4Rd36J58WGBSt.DGFRJc38rknalW");
	    entity.setHasResetPwd("Y");
        return super.insert(entity);
    }

    @Override
	public void download(List<User> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (User user : entitys) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("员工ID", user.getEmpId());
            map.put("用户名", user.getUsername());
            map.put("密码", user.getPassword());
            map.put("员工编码", user.getEmpCode());
            map.put("员工姓名", user.getEmpName());
            map.put("昵称", user.getNickName());
            map.put("性别", user.getGender());
            map.put("用户类型：S-系统内置用户；U-自定义用户；注册用户-R", user.getUserType());
            map.put("固定电话", user.getFixedPhone());
            map.put("手机号码", user.getMobilePhone());
            map.put("邮箱", user.getEmail());
            map.put("开始时间", user.getStartDate());
            map.put("结束时间", user.getEndDate());
            map.put("是否重置密码：Y-是；N-否", user.getHasResetPwd());
            map.put("修改密码的时间", user.getPwdResetTime());
            map.put("状态：Y-启用；N-禁用", user.getEnabled());
            map.put("创建日期", user.getCreateDt());
            map.put("创建人", user.getCreateBy());
            map.put("最后更新日期", user.getUpdateDt());
            map.put("最后更新人", user.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

    @Override
    public UserDto loadByUserName(String username)  {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("username",username);
        User user = mapper.selectByPropertys(params);

        UserDto u  = new UserDto();

        u.setUserId(user.getId());
        u.setUsername(user.getUsername());
        u.setNickname(user.getNickName());
        u.setPassword(user.getPassword());
        u.setEnabled("Y".equals(user.getEnabled()));

        // 设置用户角色
        u.setRoles(userRoleMapper.selectRolesByUserId(user.getId()));

        // 设置用户基本信息
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",user.getId());
        map.put("username",user.getUsername());
        map.put("userType",user.getUserType());
        map.put("empCode",user.getEmpCode());
        map.put("empName",user.getEmpName());
        map.put("roles",u.getRoles());

        // 设置用户默认角色
        UserRole defaultRole = userRoleMapper.selectDefaultRoleByUserId(user.getId());

        if(defaultRole!=null){
            map.put("defaultRoleCode",defaultRole.getRoleCode());
            map.put("defaultRoleName",defaultRole.getRoleName());
        }

        // 用户是否重置密码
        map.put("hasResetPwd",user.getHasResetPwd());
        u.setUserinfo(map);

        return u;
    }

}
