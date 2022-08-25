package com.papla.cloud.admin.auth.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.auth.domain.Menu;
import com.papla.cloud.admin.auth.service.MenuService;
import com.papla.cloud.admin.auth.service.vo.MenuTreeVo;
import com.papla.cloud.admin.auth.service.vo.MenuVo;
import com.papla.cloud.common.logging.annotation.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.common.security.utils.JwtUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags="6、菜单管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/menu")
public class MenuController {

	private final MenuService service;

	@Log("查询菜单")
    @ApiOperation(value = "1、查询")
    @ApiImplicitParams({})
	@PostMapping(value="page")
	public ResponseEntity<TabPage<Menu>> page(@RequestBody Map<String, Object> param){
		return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
	}

    @Log("保存菜单")
    @ApiOperation(value = "2、保存")
    @PostMapping(value="save")
    public ResponseEntity<Menu> save(@RequestBody Menu entity){
        return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("批量保存菜单")
    @ApiOperation(value = "3、批量保存")
    @PostMapping(value="batchSave")
    public ResponseEntity<SaveModel<Menu>> batchSave(@RequestBody SaveModel<Menu> entity){
        return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("删除菜单")
    @ApiOperation(value = "4、删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id数组", required = true),
    })
	@DeleteMapping(value="del")
	public ResponseEntity<Object> del(@RequestBody List<Object> ids){
		service.deleteByIds(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

    @Log("加载菜单")
    @ApiOperation(value = "5、加载")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "数据主键", required = true),
    })
    @GetMapping(value="load")
    public ResponseEntity<Menu> load(String id){
    	return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
    }

    @Log("加载导出")
    @ApiOperation(value = "6、导出")
    @PostMapping(value="download")
    public void download(@RequestBody Map<String, Object> param,HttpServletResponse response) throws IOException {
    	service.download(service.selectForPage(param).getData(), response);
    }

    @Log("获取角色分配菜单树")
    @ApiOperation(value = "7、获取角色分配菜单树")
    @GetMapping(value="tree")
    public ResponseEntity<List<MenuTreeVo>> Tree(String pid) {
	    Map<String,Object> params = new HashMap();
        params.put("pid","root");
        if(!JwtUtils.getRoles().contains("ADMIN")){
            params.put("userId", JwtUtils.getUserId());
        }
        return new ResponseEntity(service.getAuthtree(service.getTree(params)), HttpStatus.OK);
    }


    @ApiOperation(value = "8、获取用户首页菜单")
    @GetMapping(value="build")
    public ResponseEntity<List<MenuVo>> build() {
	    Map<String,Object> params = new HashMap();
        params.put("pid","root");
        if(!JwtUtils.getRoles().contains("ADMIN")){
            params.put("userId", JwtUtils.getUserId());
        }
        return new ResponseEntity(service.buildMenus(service.getTree(params)), HttpStatus.OK);
    }

}
