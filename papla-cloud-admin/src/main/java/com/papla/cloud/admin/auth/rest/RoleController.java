package com.papla.cloud.admin.auth.rest;

import com.papla.cloud.admin.auth.domain.Role;
import com.papla.cloud.admin.auth.service.RoleService;
import com.papla.cloud.admin.auth.service.dto.RoleAuthDTO;
import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.common.mybatis.model.TabPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="4、角色管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/role")
public class RoleController {

	private final RoleService service;

	@Log("查询角色")
    @ApiOperation(value = "1、查询")
    @ApiImplicitParams({})
    @PostMapping(value="page")
	public ResponseEntity<TabPage<Role>> page(@RequestBody Map<String, Object> param){
		return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
	}

    @Log("保存角色")
    @ApiOperation(value = "2、保存")
    @PostMapping(value="save")
    public ResponseEntity<Role> save(@RequestBody Role entity){
    	return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("批量保存角色")
    @ApiOperation(value = "3、批量保存")
    @PostMapping(value="batchSave")
    public ResponseEntity<SaveModel<Role>> batchSave(@RequestBody SaveModel<Role> entity){
        return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("删除角色")
    @ApiOperation(value = "4、删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id数组", required = true),
    })
    @DeleteMapping(value="del")
    public ResponseEntity<Object> del(@RequestBody List<Object> ids){
        service.deleteByIds(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("加载角色")
    @ApiOperation(value = "5、加载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "数据主键", required = true),
    })
    @GetMapping(value="load")
    public ResponseEntity<Role> load(String id){
        return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
    }

    @Log("导出角色")
    @ApiOperation(value = "6、导出")
    @PostMapping(value="download")
    public void download(@RequestBody Map<String, Object> param,HttpServletResponse response) throws IOException {
        service.download(service.selectForPage(param).getData(), response);
    }

    @Log("角色-菜单、资源授权")
    @ApiOperation(value = "6、角色-菜单、资源授权")
    @PostMapping(value="auth")
    public ResponseEntity<Object> auth(@RequestBody RoleAuthDTO dto){
        service.auth(dto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "7、获取已授权资源id数组")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleId", value = "角色ID", required = true),
    })
    @GetMapping(value="loadAuthPerms")
    public ResponseEntity<List<String>> loadAuthPerms(String roleId){
	    Map<String,Object> params = new HashMap();
	    params.put("roleId",roleId);
        return new ResponseEntity(service.loadAuthPerms(params), HttpStatus.OK);
    }

    @ApiOperation(value = "7、获取已授权菜单id数组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true),
    })
    @GetMapping(value="loadAuthMenus")
    public ResponseEntity<List<String>> loadAuthMenus(String roleId){
        Map<String,Object> params = new HashMap();
        params.put("roleId",roleId);
        return new ResponseEntity<>(service.loadAuthMenus(params), HttpStatus.OK);
    }



}
