package com.papla.cloud.admin.auth.service.impl;

import com.papla.cloud.admin.auth.domain.Perm;
import com.papla.cloud.admin.auth.mapper.PermMapper;
import com.papla.cloud.admin.auth.service.PermCacheService;
import com.papla.cloud.admin.auth.service.PermService;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Title:                PermServiceImpl
* @Description: TODO   资源权限管理管理
* @author                
* @date                2021-09-21
* @version            V1.0
*/
@Service
@RequiredArgsConstructor
public class PermServiceImpl extends BaseServiceImpl<Perm> implements PermService{

	private final Logger logger = LogManager.getLogger(this.getClass());

    private final PermMapper mapper;

    private final PermCacheService permCacheService;

	@Override
	public Logger getLogger(){
		return logger;
	}

	@Override
	public BaseMapper<Perm> getMapper(){
		return mapper;
	}

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public SaveModel<Perm> batchSaveOrUpdate(SaveModel<Perm> data) {
        super.batchSaveOrUpdate(data);

        // 刷新资源角色分配缓存
        permCacheService.refreshRedisRoleUrls();

        // 获取匿名资源
        List<Perm> anonymousUrls = data.getAllDatas().stream().filter( p -> "ANONYMOUS".equals(p.getPermType())).collect(Collectors.toList());

        // 当存在匿名资源时，刷新匿名资源缓存
        if(anonymousUrls.size()>0){
            permCacheService.refreshRedisAnonymousUrls();
        }

        return data;
    }

    @Override
	public void download(List<Perm> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Perm perm : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("菜单ID", perm.getMenuId());
			map.put("权限名称", perm.getPermName());
			map.put("权限类型", perm.getPermType());
			map.put("权限编码", perm.getPermCode());
			map.put("请求方式", perm.getPermMethod());
			map.put("资源路径", perm.getPermUrl());
			map.put("排序", perm.getPremSort());
			map.put("是否有效", perm.getEnabled());
			map.put("创建日期", perm.getCreateDt());
			map.put("创建人", perm.getCreateBy());
			map.put("最后更新日期", perm.getUpdateDt());
			map.put("最后更新人", perm.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

}
