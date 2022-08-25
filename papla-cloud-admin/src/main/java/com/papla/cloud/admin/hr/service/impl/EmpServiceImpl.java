package com.papla.cloud.admin.hr.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.auth.domain.User;
import com.papla.cloud.admin.auth.service.UserService;
import com.papla.cloud.admin.hr.service.EmpService;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.papla.cloud.admin.hr.domain.Emp;
import com.papla.cloud.admin.hr.mapper.EmpMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
* @Title: EmpServiceImpl
* @Description: TODO 员工管理
* @author 
* @date 2021-09-27
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class EmpServiceImpl extends BaseServiceImpl<Emp> implements EmpService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final EmpMapper mapper;

	private final UserService userService;

	@Override
	public BaseMapper<Emp> getMapper(){
		return mapper;
	}

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Integer insert(Emp entity) {

	    Integer num = super.insert(entity);

	    if("Y".equals(entity.getCreateUser())){
            User user = new User();
            user.setUsername(entity.getUsername());
            user.setEmail(entity.getEmail());
            user.setMobilePhone(entity.getPhoneNo());
            user.setEmpId(entity.getEmpId());
            user.setEmpCode(entity.getEmpCode());
            user.setEmpName(entity.getEmpName());
            user.setGender(entity.getEmpSex());
            user.setEnabled(entity.getEnabled());
            user.setUserType("U");
            user.setNickName(entity.getEmpName());
            user.setFixedPhone(entity.getFixedPhoneNo());
            userService.insert(user);
        }

        return num;
    }

    @Override
	public void download(List<Emp> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Emp emp : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("员工编号", emp.getEmpCode());
			map.put("员工姓名", emp.getEmpName());
			map.put("性别", emp.getEmpSex());
			map.put("身份证", emp.getIdNo());
			map.put("电话", emp.getPhoneNo());
			map.put("地址", emp.getAddress());
			map.put("邮箱", emp.getEmail());
			map.put("员工状态", emp.getEnabled());
			map.put("出生日期", emp.getBirthday());
			map.put("创建日期", emp.getCreateDt());
			map.put("创建人", emp.getCreateBy());
			map.put("最后更新日期", emp.getUpdateDt());
			map.put("最后更新人", emp.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
