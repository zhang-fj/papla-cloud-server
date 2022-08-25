package com.papla.cloud.admin.auth.service.impl;

import com.papla.cloud.admin.auth.domain.Role;
import com.papla.cloud.admin.auth.domain.RoleMenu;
import com.papla.cloud.admin.auth.domain.RolePerm;
import com.papla.cloud.admin.auth.mapper.RoleMapper;
import com.papla.cloud.admin.auth.mapper.RoleMenuMapper;
import com.papla.cloud.admin.auth.mapper.RolePermMapper;
import com.papla.cloud.admin.auth.service.PermCacheService;
import com.papla.cloud.admin.auth.service.RoleService;
import com.papla.cloud.admin.auth.service.dto.RoleAuthDTO;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
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
import java.util.*;
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
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService{

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

	private final RoleMapper mapper;

	private final RoleMenuMapper roleMenuMapper;

	private final RolePermMapper rolePermMapper;

	private final PermCacheService permCacheService;

	@Override
	public BaseMapper<Role> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<Role> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Role role : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("角色编码", role.getRoleCode());
			map.put("角色名称", role.getRoleName());
			map.put("角色描述", role.getRoleDesc());
			map.put("启用状态：Y-是；N-否", role.getEnabled());
			map.put("创建日期", role.getCreateDt());
			map.put("创建人", role.getCreateBy());
			map.put("最后更新日期", role.getUpdateDt());
			map.put("最后更新人", role.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void auth(RoleAuthDTO dto){
	    String roleId = dto.getRoleId();

	    Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleId",roleId);
        // 根据角色ID删除角色菜单授权
        roleMenuMapper.deleteByParams(params);
        // 根据角色ID删除角色资源授权
        rolePermMapper.deleteByParams(params);

        // 新增角色菜单授权
        List<String> meunIds = dto.getMenuIds();
        if(meunIds!=null){
            for(String menuId:meunIds){
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenu.setId(UUID.randomUUID().toString());
                roleMenu.setWhoForInsert("");
                roleMenuMapper.insert(roleMenu);
            }
        }

        // 新增角色权限授权
        List<String> permIds = dto.getPermIds();
        if(permIds!=null){
            for(String permId:permIds){
                RolePerm rolePerm = new RolePerm();
                rolePerm.setRoleId(roleId);
                rolePerm.setPermId(permId);
                rolePerm.setId(UUID.randomUUID().toString());
                rolePerm.setWhoForInsert("");
                rolePermMapper.insert(rolePerm);
            }
        }

        // 根据角色ID 删除未授权菜单下的资源权限
        params.put("roleId",roleId);
        params.put("notMenu",true);
        rolePermMapper.deleteByParams(params);

        // 刷新资源角色分配缓存
        permCacheService.refreshRedisRoleUrls();

    }

    @Override
    public List<String> loadAuthPerms(Map<String, Object> params){
	    List<RolePerm> rps = rolePermMapper.findAll(params);
        return rps.stream().map(RolePerm::getPermId).collect(Collectors.toList());
    }

    @Override
    public List<String> loadAuthMenus(Map<String, Object> params){
        List<RoleMenu> rms = roleMenuMapper.findAll(params);
        return rms.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
    }

}
