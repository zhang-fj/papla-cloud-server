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

import com.papla.cloud.workflow.cfg.domain.EntityStatus;
import com.papla.cloud.workflow.cfg.mapper.EntityStatusMapper;
import com.papla.cloud.workflow.cfg.service.EntityStatusService;

/**
* @Title: EntityStatusServiceImpl
* @Description: TODO 实体状态管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class EntityStatusServiceImpl extends BaseServiceImpl<EntityStatus> implements EntityStatusService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final EntityStatusMapper mapper;

	@Override
	public BaseMapper<EntityStatus> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<EntityStatus> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (EntityStatus entityStatus : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("实体ID", entityStatus.getEntityId());
			map.put("状态分类", entityStatus.getStatusCategory());
			map.put("状态编码", entityStatus.getStatusCode());
			map.put("状态名称", entityStatus.getStatusName());
			map.put("状态描述", entityStatus.getStatusDesc());
			map.put("是否启用", entityStatus.getEnabled());
			map.put("创建日期", entityStatus.getCreateDt());
			map.put("创建人", entityStatus.getCreateBy());
			map.put("最后更新日期", entityStatus.getUpdateDt());
			map.put("最后更新人", entityStatus.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
