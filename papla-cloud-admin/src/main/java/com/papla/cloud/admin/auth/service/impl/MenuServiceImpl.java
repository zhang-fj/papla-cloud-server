package com.papla.cloud.admin.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.papla.cloud.admin.auth.domain.Menu;
import com.papla.cloud.admin.auth.mapper.MenuMapper;
import com.papla.cloud.admin.auth.mapper.PermMapper;
import com.papla.cloud.admin.auth.service.MenuService;
import com.papla.cloud.admin.auth.service.mapstruct.MenuTreeMapper;
import com.papla.cloud.admin.auth.service.vo.MenuMetaVo;
import com.papla.cloud.admin.auth.service.vo.MenuTreeVo;
import com.papla.cloud.admin.auth.service.vo.MenuVo;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
* @Title:                MenuServiceImpl
* @Description: TODO   菜单管理管理
* @author                
* @date                2021-09-10
* @version            V1.0
*/
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public Logger getLogger(){
		return logger;
	}

    private final MenuMapper mapper;

    private final MenuTreeMapper menuTreeMapper;

    private final PermMapper permMapper;

	@Override
	public BaseMapper<Menu> getMapper(){
		return mapper;
	}

	@Override
	public void download(List<Menu> entitys, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Menu menu : entitys) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("上级菜单ID", menu.getPid());
			map.put("子菜单数目", menu.getSubCount());
			map.put("菜单类型", menu.getType());
			map.put("菜单标题", menu.getTitle());
			map.put("组件名称", menu.getName());
			map.put("组件", menu.getComponent());
			map.put("排序", menu.getMenuSort());
			map.put("图标", menu.getIcon());
			map.put("链接地址", menu.getPath());
			map.put("是否外链", menu.getIFrame());
			map.put("缓存", menu.getCache());
			map.put("隐藏", menu.getHidden());
			map.put("创建日期", menu.getCreateDt());
			map.put("创建人", menu.getCreateBy());
			map.put("最后更新日期", menu.getUpdateDt());
			map.put("最后更新人", menu.getUpdateBy());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}

    /**
     * 查询功能树
     */
    @Override
    public List<MenuTreeVo> getTree(Map<String, Object> params){
        List<MenuTreeVo> nodes = new ArrayList<MenuTreeVo>();
        List<Menu> menus = mapper.findAll(params);
        if(menus != null){
            for(Menu menu: menus){
                MenuTreeVo node =  menuTreeMapper.toDto(menu);
                params.put("pid",node.getId());
                node.setChildren(getTree(params));
                nodes.add(node);
            }
        }
        return nodes;
    }

    @Override
    public List<MenuTreeVo> getAuthtree(List<MenuTreeVo> menus){
        Map<String,Object> params = new HashMap<>();
        if(menus != null){
            for(MenuTreeVo menu: menus){
                params.put("menuId",menu.getId());
                params.put("permType","BTN");
                menu.setPerms(permMapper.findAll(params));
                menu.setChildren(getAuthtree(menu.getChildren()));
            }
        }
        return menus;
    }

    @Override
    public List<MenuVo> buildMenus(List<MenuTreeVo> menus){
	    List<MenuVo> mvs = new ArrayList<MenuVo>();
	    if(menus != null){
	        for(MenuTreeVo menu:menus){
	            MenuVo menuVo = new MenuVo();
                menuVo.setName(StringUtils.isNotBlank(menu.getName()) ? menu.getName() : menu.getTitle());
                menuVo.setPath(("root".equals(menu.getPid())?"/":"")+menu.getPath());
                menuVo.setHidden("Y".equals(menu.getHidden()));
                menuVo.setMeta(new MenuMetaVo(menu.getTitle(), menu.getIcon(), !"Y".equals(menu.getCache())));
                if(!"Y".equals(menu.getIFrame())){
                    if("root".equals(menu.getPid())){
                        menuVo.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
                    }else if("com".equals(menu.getType())){
                        menuVo.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "ParentView" : menu.getComponent());
                    }else if (StringUtils.isNoneBlank(menu.getComponent())) {
                        menuVo.setComponent(menu.getComponent());
                    }
                }
                List<MenuTreeVo> chlidren = menu.getChildren();
                if(CollectionUtil.isNotEmpty(chlidren)){
                    menuVo.setAlwaysShow(true);
                    menuVo.setRedirect("noredirect");
                    menuVo.setChildren(buildMenus(chlidren));
                }else if ("root".equals(menu.getPid())) {
                    MenuVo menuVo1 = new MenuVo();
                    menuVo1.setMeta(menuVo.getMeta());
                    // 非外链
                    if (!"Y".equals(menu.getIFrame())) {
                        menuVo1.setPath("index");
                        menuVo1.setName(menuVo.getName());
                        menuVo1.setComponent(menuVo.getComponent());
                    } else {
                        menuVo1.setPath(menu.getPath());
                    }
                    menuVo.setName(null);
                    menuVo.setMeta(null);
                    menuVo.setComponent("Layout");
                    List<MenuVo> list1 = new ArrayList<>();
                    list1.add(menuVo1);
                    menuVo.setChildren(list1);
                }
                mvs.add(menuVo);
            }
        }
        return mvs;
    }

}
