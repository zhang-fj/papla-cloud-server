package com.papla.cloud.workflow.cfg.service.impl;

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

import com.papla.cloud.workflow.cfg.domain.EntityForms;
import com.papla.cloud.workflow.cfg.mapper.EntityFormsMapper;
import com.papla.cloud.workflow.cfg.service.EntityFormsService;

/**
* @Title: EntityFormsServiceImpl
* @Description: TODO 实体表单管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class EntityFormsServiceImpl extends BaseServiceImpl<EntityForms> implements EntityFormsService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final EntityFormsMapper mapper;

	@Override
	public BaseMapper<EntityForms> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<EntityForms> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (EntityForms entityForms : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("表单URL", entityForms.getFormUrl());
			map.put("表单说明", entityForms.getFormDesc());
			map.put("应用ID", entityForms.getAppId());
			map.put("实体ID", entityForms.getEntityId());
			map.put("是否启用", entityForms.getEnabled());
			map.put("客户端分类", entityForms.getClientType());
			map.put("创建日期", entityForms.getCreateDt());
			map.put("创建人", entityForms.getCreateBy());
			map.put("最后更新日期", entityForms.getUpdateDt());
			map.put("最后更新人", entityForms.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
