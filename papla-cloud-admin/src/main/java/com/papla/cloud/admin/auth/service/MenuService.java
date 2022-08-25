package com.papla.cloud.admin.auth.service;

import com.papla.cloud.admin.auth.service.vo.MenuTreeVo;
import com.papla.cloud.admin.auth.service.vo.MenuVo;
import com.papla.cloud.admin.auth.domain.Menu;
import com.papla.cloud.common.mybatis.service.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @Title:                MenuService
* @Description: TODO   菜单管理管理
* @author                
* @date                2021-09-10
* @version            V1.0
*/
public interface MenuService extends BaseService<Menu>{

	/**
	* 导出数据
	* @param entitys 待导出的数据
	* @param response
	* @throws IOException
	*/
	void download(List<Menu> entitys, HttpServletResponse response) throws IOException;

    /**
     * 获取菜单树
     * @param params
     * @return
     */
    List<MenuTreeVo> getTree(Map<String, Object> params);

    /**
     * 获取角色菜单分配中使用的菜单树
     * @param menus
     * @return
     */
    List<MenuTreeVo> getAuthtree(List<MenuTreeVo> menus);

    /**
     * 获取首页用户功能菜单
     * @param menus
     * @return
     */
    List<MenuVo> buildMenus(List<MenuTreeVo> menus);
}