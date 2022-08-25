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

import com.papla.cloud.workflow.cfg.domain.EntityAttrs;
import com.papla.cloud.workflow.cfg.mapper.EntityAttrsMapper;
import com.papla.cloud.workflow.cfg.service.EntityAttrsService;

/**
* @Title: EntityAttrsServiceImpl
* @Description: TODO 实体属性管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class EntityAttrsServiceImpl extends BaseServiceImpl<EntityAttrs> implements EntityAttrsService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final EntityAttrsMapper mapper;

	@Override
	public BaseMapper<EntityAttrs> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<EntityAttrs> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (EntityAttrs entityAttrs : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("实体ID", entityAttrs.getEntityId());
			map.put("属性编码", entityAttrs.getAttrCode());
			map.put("属性名称", entityAttrs.getAttrName());
			map.put("值类型", entityAttrs.getAttrCategory());
			map.put("函数类型", entityAttrs.getFuncType());
			map.put("属性值", entityAttrs.getFuncValue());
			map.put("属性描述", entityAttrs.getAttrDesc());
			map.put("数据类型", entityAttrs.getAttrDataType());
			map.put("创建日期", entityAttrs.getCreateDt());
			map.put("创建人", entityAttrs.getCreateBy());
			map.put("最后更新日期", entityAttrs.getUpdateDt());
			map.put("最后更新人", entityAttrs.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
