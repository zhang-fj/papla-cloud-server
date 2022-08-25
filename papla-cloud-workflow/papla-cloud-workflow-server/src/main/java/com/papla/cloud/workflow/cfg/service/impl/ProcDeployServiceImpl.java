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

import com.papla.cloud.workflow.cfg.domain.ProcDeploy;
import com.papla.cloud.workflow.cfg.mapper.ProcDeployMapper;
import com.papla.cloud.workflow.cfg.service.ProcDeployService;

/**
* @Title: ProcDeployServiceImpl
* @Description: TODO 流程部署管理
* @author 
* @date 2021-09-30
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class ProcDeployServiceImpl extends BaseServiceImpl<ProcDeploy> implements ProcDeployService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final ProcDeployMapper mapper;

	@Override
	public BaseMapper<ProcDeploy> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<ProcDeploy> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (ProcDeploy procDeploy : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("流程ID", procDeploy.getProcessId());
			map.put("应用ID", procDeploy.getAppId());
			map.put("组织ID", procDeploy.getOrgId());
			map.put("实体ID", procDeploy.getEntityId());
			map.put("流程编码，全局唯一", procDeploy.getProcessCode());
			map.put("流程名称", procDeploy.getProcessName());
			map.put("流程状态：D-部署流程，H-历史流程", procDeploy.getProcessStatus());
			map.put("流程版本，自增", procDeploy.getProcessVersion());
			map.put("流程分类：P-公共流程，O-组织流程", procDeploy.getProcessCategory());
			map.put("流程描述", procDeploy.getProcessDesc());
			map.put("流程配置XML文件", procDeploy.getProcessJson());
			map.put("是否失效 Y-有效,N-失效", procDeploy.getEnabled());
			map.put("创建日期", procDeploy.getCreateDt());
			map.put("创建人", procDeploy.getCreateBy());
			map.put("最后更新日期", procDeploy.getUpdateDt());
			map.put("最后更新人", procDeploy.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
