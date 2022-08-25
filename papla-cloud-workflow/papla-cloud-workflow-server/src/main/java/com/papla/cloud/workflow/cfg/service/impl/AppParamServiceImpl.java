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

import com.papla.cloud.workflow.cfg.domain.AppParam;
import com.papla.cloud.workflow.cfg.mapper.AppParamMapper;
import com.papla.cloud.workflow.cfg.service.AppParamService;

/**
* @Title: AppParamServiceImpl
* @Description: TODO 应用参数管理
* @author 
* @date 2021-10-15
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class AppParamServiceImpl extends BaseServiceImpl<AppParam> implements AppParamService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final AppParamMapper mapper;

	@Override
	public BaseMapper<AppParam> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<AppParam> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (AppParam appParam : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("应用ID", appParam.getAppId());
			map.put("参数编码", appParam.getParamCode());
			map.put("参数名称", appParam.getParamName());
			map.put("参数值", appParam.getParamValue());
			map.put("启用标记", appParam.getEnabled());
			map.put("创建日期", appParam.getCreateDt());
			map.put("创建人", appParam.getCreateBy());
			map.put("最后更新日期", appParam.getUpdateDt());
			map.put("最后更新人", appParam.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
