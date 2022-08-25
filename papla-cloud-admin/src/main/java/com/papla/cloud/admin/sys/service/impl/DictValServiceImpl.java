package com.papla.cloud.admin.sys.service.impl;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.admin.sys.domain.DictVal;
import com.papla.cloud.admin.sys.mapper.DictValMapper;
import com.papla.cloud.admin.sys.service.DictValService;
import com.papla.cloud.common.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @Title:                DictValServiceImpl
* @Description: TODO   字典明细管理
* @author                
* @date                2021-09-13
* @version            V1.0
*/
@Service
@RequiredArgsConstructor
public class DictValServiceImpl extends BaseServiceImpl<DictVal> implements DictValService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final DictValMapper mapper;

	@Override
	public BaseMapper<DictVal> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<DictVal> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (DictVal dictVal : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("字典id", dictVal.getDictId());
			map.put("字典编码", dictVal.getDictCode());
			map.put("字典标签", dictVal.getDictLabel());
			map.put("字典值", dictVal.getDictValue());
			map.put("排序", dictVal.getSort());
			map.put("描述", dictVal.getDescription());
			map.put("是否启用", dictVal.getEnabled());
			map.put("创建日期", dictVal.getCreateDt());
			map.put("创建人", dictVal.getCreateBy());
			map.put("最后更新日期", dictVal.getUpdateDt());
			map.put("最后更新人", dictVal.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
