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

import com.papla.cloud.workflow.cfg.domain.Entities;
import com.papla.cloud.workflow.cfg.mapper.EntitiesMapper;
import com.papla.cloud.workflow.cfg.service.EntitiesService;

/**
* @Title: EntitiesServiceImpl
* @Description: TODO 流程实体管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class EntitiesServiceImpl extends BaseServiceImpl<Entities> implements EntitiesService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final EntitiesMapper mapper;

	@Override
	public BaseMapper<Entities> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<Entities> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Entities entities : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("应用ID", entities.getAppId());
			map.put("实体编码", entities.getEntityCode());
			map.put("实体名称", entities.getEntityName());
			map.put("实体描述", entities.getEntityDesc());
			map.put("函数类型", entities.getFuncType());
			map.put("回调函数", entities.getFuncValue());
			map.put("业务表名", entities.getBusTableName());
			map.put("业务主键名", entities.getBusTablePk());
			map.put("获取授权审批人实现类", entities.getAuthUserCls());
			map.put("是否失效", entities.getEnabled());
			map.put("创建日期", entities.getCreateDt());
			map.put("创建人", entities.getCreateBy());
			map.put("最后更新日期", entities.getUpdateDt());
			map.put("最后更新人", entities.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
