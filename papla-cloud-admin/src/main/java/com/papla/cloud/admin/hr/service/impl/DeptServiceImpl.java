package com.papla.cloud.admin.hr.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.hr.mapper.DeptMapper;
import com.papla.cloud.admin.hr.service.DeptService;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.papla.cloud.admin.hr.domain.Dept;

/**
* @Title: DeptServiceImpl
* @Description: TODO 部门管理
* @author 
* @date 2021-09-26
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class DeptServiceImpl extends BaseServiceImpl<Dept> implements DeptService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final DeptMapper mapper;

	@Override
	public BaseMapper<Dept> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<Dept> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Dept dept : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("部门编码", dept.getDeptCode());
			map.put("部门名称", dept.getDeptName());
			map.put("部门简称", dept.getDeptShortName());
			map.put("上级部门", dept.getUpDpId());
			map.put("所属组织", dept.getOrgId());
			map.put("是否启用", dept.getEnabled());
			map.put("排序", dept.getSort());
			map.put("创建日期", dept.getCreateDt());
			map.put("创建人", dept.getCreateBy());
			map.put("最后更新日期", dept.getUpdateDt());
			map.put("最后更新人", dept.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

	public static void main(String[] args) {
		System.out.println("10_10_22_3".replaceAll("_","."));
	}
}
