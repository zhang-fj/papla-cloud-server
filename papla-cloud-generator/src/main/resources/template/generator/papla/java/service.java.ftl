package ${package}.service;

import java.io.IOException;
import com.papla.cloud.common.mybatis.service.BaseService;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ${package}.domain.${ClassName};

/**
* @Title: ${ClassName}Service
* @Description: TODO ${apiAlias}管理
* @author ${author}
* @date ${date}
* @version V1.0
*/
public interface ${ClassName}Service extends BaseService<${ClassName}>{

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<${ClassName}> entitys, HttpServletResponse response) throws IOException;

}