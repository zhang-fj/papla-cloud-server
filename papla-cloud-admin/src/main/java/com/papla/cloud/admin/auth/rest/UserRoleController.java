package com.papla.cloud.admin.auth.rest;

import com.papla.cloud.admin.auth.domain.UserRole;
import com.papla.cloud.admin.auth.service.UserRoleService;
import com.papla.cloud.common.logging.annotation.Log;
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
import java.util.List;
import java.util.Map;

@Api(tags="5、用户角色授权")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/userRole")
public class UserRoleController {

	private final UserRoleService service;

    @Log("查询用户角色授权")
    @ApiOperation(value = "1、查询")
    @ApiImplicitParams({})
    @PostMapping(value="page")
	public ResponseEntity<TabPage<UserRole>> page(@RequestBody Map<String, Object> param){
		return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
	}

    @Log("用户角色授权")
    @ApiOperation(value = "2、用户角色授权")
    @PostMapping(value="auth")
    public ResponseEntity<Object> auth(@RequestBody List<UserRole> entitys){
        service.auth(entitys);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("设置默认角色")
    @ApiOperation(value = "3、设置默认角色")
    @PostMapping(value="defaultRole")
    public ResponseEntity<UserRole> defaultRole(@RequestBody UserRole entity){
        service.defaultRole(entity);
        return new ResponseEntity(entity, HttpStatus.OK);
    }

    @Log("取消授权")
    @ApiOperation(value = "4、删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id数组", required = true),
    })
	@DeleteMapping(value="del")
	public ResponseEntity<Object> del(@RequestBody List<Object> ids){
		service.deleteByIds(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

    @Log("导出授权")
    @ApiOperation(value = "6、导出")
    @PostMapping(value="download")
    public void download(@RequestBody Map<String, Object> param,HttpServletResponse response) throws IOException {
        service.download(service.selectForPage(param).getData(), response);
    }

}
