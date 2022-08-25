package com.papla.cloud.admin.sys.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.admin.sys.domain.Dict;
import com.papla.cloud.admin.sys.mapper.DictMapper;
import com.papla.cloud.admin.sys.service.DictService;

/**
* @Title:                DictServiceImpl
* @Description: TODO   字典管理管理
* @author                
* @date                2021-09-13
* @version            V1.0
*/
@Service
@RequiredArgsConstructor
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final DictMapper mapper;

	@Override
	public BaseMapper<Dict> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<Dict> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Dict dict : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("类型", dict.getDictType());
			map.put("字典名称", dict.getDictName());
			map.put("字典编码", dict.getDictCode());
			map.put("是否启用", dict.getEnabled());
			map.put("描述", dict.getDictDesc());
			map.put("创建日期", dict.getCreateDt());
			map.put("创建人", dict.getCreateBy());
			map.put("最后更新日期", dict.getUpdateDt());
			map.put("最后更新人", dict.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
