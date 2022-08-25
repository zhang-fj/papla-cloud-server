package com.papla.cloud.workflow.demo.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;

import com.papla.cloud.workflow.demo.domain.Demo;
import com.papla.cloud.workflow.demo.mapper.DemoMapper;
import com.papla.cloud.workflow.demo.service.DemoService;

/**
* @Title: DemoServiceImpl
* @Description: TODO 流程演示管理
* @author 
* @date 2021-10-03
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class DemoServiceImpl extends BaseServiceImpl<Demo> implements DemoService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final DemoMapper mapper;

	@Override
	public BaseMapper<Demo> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<Demo> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Demo demo : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("订单编号", demo.getPoNo());
			map.put("订单说明", demo.getPoDesc());
			map.put("供应商", demo.getVendor());
			map.put("组织", demo.getOrgId());
			map.put("部门", demo.getDeptId());
			map.put("采购员", demo.getBuyer());
			map.put("订单金额", demo.getAmt());
			map.put("采购日期", demo.getPoDate());
			map.put("流程实例编码", demo.getInstCode());
			map.put("状态", demo.getStatus());
			map.put("创建日期", demo.getCreateDt());
			map.put("创建人", demo.getCreateBy());
			map.put("最后更新日期", demo.getUpdateDt());
			map.put("最后更新人", demo.getUpdateBy());
			map.put("状态", demo.getStatusDesc());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
