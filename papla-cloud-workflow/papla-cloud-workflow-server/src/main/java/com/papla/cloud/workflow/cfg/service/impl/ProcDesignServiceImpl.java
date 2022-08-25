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

import com.papla.cloud.workflow.cfg.domain.ProcDesign;
import com.papla.cloud.workflow.cfg.mapper.ProcDesignMapper;
import com.papla.cloud.workflow.cfg.service.ProcDesignService;

/**
* @Title: ProcDesignServiceImpl
* @Description: TODO 设计流程管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class ProcDesignServiceImpl extends BaseServiceImpl<ProcDesign> implements ProcDesignService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final ProcDesignMapper mapper;

	@Override
	public BaseMapper<ProcDesign> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<ProcDesign> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (ProcDesign procDesign : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("应用ID", procDesign.getAppId());
			map.put("组织ID", procDesign.getOrgId());
			map.put("实体ID", procDesign.getEntityId());
			map.put("流程编码", procDesign.getProcessCode());
			map.put("流程名称", procDesign.getProcessName());
			map.put("流程版本", procDesign.getProcessVersion());
			map.put("流程分类", procDesign.getProcessCategory());
			map.put("流程描述", procDesign.getProcessDesc());
			map.put("流程配置XML文件", procDesign.getProcessJson());
			map.put("是否失效", procDesign.getEnabled());
			map.put("创建日期", procDesign.getCreateDt());
			map.put("创建人", procDesign.getCreateBy());
			map.put("最后更新日期", procDesign.getUpdateDt());
			map.put("最后更新人", procDesign.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
