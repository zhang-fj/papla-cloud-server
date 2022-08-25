package com.papla.cloud.admin.hr.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.hr.mapper.EmpAsgMapper;
import com.papla.cloud.admin.hr.service.EmpAsgService;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.papla.cloud.admin.hr.domain.EmpAsg;

/**
* @Title: EmpAsgServiceImpl
* @Description: TODO 员工分配管理
* @author 
* @date 2021-09-27
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class EmpAsgServiceImpl extends BaseServiceImpl<EmpAsg> implements EmpAsgService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final EmpAsgMapper mapper;

	@Override
	public BaseMapper<EmpAsg> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<EmpAsg> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (EmpAsg empAsg : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("员工ID", empAsg.getEmpId());
			map.put("岗位ID", empAsg.getPostId());
			map.put("部门ID，冗余字段", empAsg.getDeptId());
			map.put("组织ID，冗余字段", empAsg.getOrgId());
			map.put("启用状态：Y，启用；N，禁用", empAsg.getEnabled());
			map.put("创建日期", empAsg.getCreateDt());
			map.put("创建人", empAsg.getCreateBy());
			map.put("最后更新日期", empAsg.getUpdateDt());
			map.put("最后更新人", empAsg.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
