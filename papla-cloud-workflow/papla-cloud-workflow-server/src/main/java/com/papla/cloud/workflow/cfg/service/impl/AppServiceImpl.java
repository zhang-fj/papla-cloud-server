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

import com.papla.cloud.workflow.cfg.domain.App;
import com.papla.cloud.workflow.cfg.mapper.AppMapper;
import com.papla.cloud.workflow.cfg.service.AppService;

/**
* @Title: AppServiceImpl
* @Description: TODO 流程应用管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class AppServiceImpl extends BaseServiceImpl<App> implements AppService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final AppMapper mapper;

	@Override
	public BaseMapper<App> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<App> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (App app : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("应用编码", app.getAppCode());
			map.put("应用名称", app.getAppName());
			map.put("应用类型", app.getAppType());
			map.put("应用描述", app.getAppDesc());
			map.put("应用语音", app.getAppLang());
			map.put("验证地址", app.getAppLoginUrl());
			map.put("客户端ID", app.getClientId());
			map.put("客户端密码", app.getClientSecret());
			map.put("WEB应用主机可以是IP也可以是域名", app.getWebHost());
			map.put("WEB应用端口", app.getWebPort());
			map.put("WEB功能名", app.getWebProject());
			map.put("数据库类型  ORA；oracle数据库 MYSQL； MYSQL数据库 MSSQL；SQL Server数据库", app.getDbType());
			map.put("数据库连接串含主机端口SID", app.getDbUrl());
			map.put("数据库用户", app.getDbUser());
			map.put("数据库用户口令", app.getDbPassword());
			map.put("启用标记", app.getEnabled());
			map.put("应用版本号", app.getVersion());
			map.put("创建日期", app.getCreateDt());
			map.put("创建人", app.getCreateBy());
			map.put("最后更新日期", app.getUpdateDt());
			map.put("最后更新人", app.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
