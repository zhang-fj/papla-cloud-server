package com.papla.cloud.admin.auth.service.impl;

import com.papla.cloud.admin.auth.mapper.PermMapper;
import com.papla.cloud.admin.auth.service.PermCacheService;
import com.papla.cloud.common.core.constants.GlobalConstants;
import com.papla.cloud.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @Title:                RoleServiceImpl
* @Description: TODO   角色管理管理
* @author                
* @date                2021-09-14
* @version            V1.0
*/
@Service
@RequiredArgsConstructor
public class PermCacheServiceImpl implements PermCacheService {


	private final RedisUtils redisUtils;

	private final PermMapper permMapper;


    /**
     * 刷新资源路径与角色缓存
     */
    @Override
	public void refreshRedisRoleUrls(){
	    List<Map<String,String>> list = permMapper.groupRolesByUrl();
	    Map<String,Object> obj = list.stream().collect(
            Collectors.toMap(
                p-> p.get("key"),
                p-> Arrays.stream(p.get("value").split(",")).collect(Collectors.toList())
            )
        );
	    redisUtils.del(GlobalConstants.URL_PERM_ROLES_KEY);
	    redisUtils.hmset(GlobalConstants.URL_PERM_ROLES_KEY,obj);
    }

    @Override
    public void refreshRedisAnonymousUrls() {
        Set<String> set = permMapper.selectAnonymousUrl();
        redisUtils.sSet(GlobalConstants.URL_PERM_ANONY_KEY,set.toArray());
    }

}
