package ${package}.service.impl;

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

import ${package}.domain.${ClassName};
import ${package}.mapper.${ClassName}Mapper;
import ${package}.service.${ClassName}Service;

/**
* @Title: ${ClassName}ServiceImpl
* @Description: TODO ${apiAlias}管理
* @author ${author}
* @date ${date}
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class ${ClassName}ServiceImpl extends BaseServiceImpl<${ClassName}> implements ${ClassName}Service{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final ${ClassName}Mapper mapper;

	@Override
	public BaseMapper<${ClassName}> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<${ClassName}> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (${ClassName} ${className} : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
<#list columns as column>
    <#if column.columnKey != 'PRI'>
    <#if column.remark != ''>
			map.put("${column.aliasName}", ${className}.get${column.upperColumnName}());
    <#else>
			map.put(" ${column.lowerColumnName}",  ${className}.get${column.UpperColumnName}());
    </#if>
    </#if>
</#list>
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
