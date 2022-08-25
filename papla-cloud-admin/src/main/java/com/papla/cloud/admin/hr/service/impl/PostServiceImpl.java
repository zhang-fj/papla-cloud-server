package com.papla.cloud.admin.hr.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.hr.domain.Post;
import com.papla.cloud.admin.hr.mapper.PostMapper;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.papla.cloud.admin.hr.service.PostService;

/**
* @Title: PostServiceImpl
* @Description: TODO 岗位管理
* @author 
* @date 2021-09-26
* @version V1.0
*/
@Service
@RequiredArgsConstructor
public class PostServiceImpl extends BaseServiceImpl<Post> implements PostService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final PostMapper mapper;

	@Override
	public BaseMapper<Post> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<Post> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Post post : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("岗位编码", post.getPostCode());
			map.put("岗位名称", post.getPostName());
			map.put("岗位描述", post.getPostDesc());
			map.put("岗位级别，值集编码：XSR_PUB_POST_LEVEL", post.getPostLevel());
			map.put("部门ID", post.getDeptId());
			map.put("组织ID，冗余字段", post.getOrgId());
			map.put("启用标记：Y，启用；N，禁用", post.getEnabled());
			map.put("创建日期", post.getCreateDt());
			map.put("创建人", post.getCreateBy());
			map.put("最后更新日期", post.getUpdateDt());
			map.put("最后更新人", post.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

    @Override
    public void insertEmpPostAsg(Map<String, Object> params) {
        mapper.insertEmpPostAsg(params);
    }

    @Override
    public void deleteEmpPostAsg(Map<String, Object> params) {
        mapper.deleteEmpPostAsg(params);
    }

}
