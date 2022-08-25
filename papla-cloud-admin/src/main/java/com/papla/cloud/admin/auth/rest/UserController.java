package com.papla.cloud.admin.auth.rest;

import com.papla.cloud.admin.auth.domain.User;
import com.papla.cloud.admin.auth.service.UserService;
import com.papla.cloud.admin.auth.service.dto.UserDto;
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
import java.util.List;
import java.util.Map;

@Api(tags="3、用户管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserController {

	private final UserService service;

    @Log("查询用户")
    @ApiOperation(value = "1、查询")
    @ApiImplicitParams({})
    @PostMapping(value="page")
	public ResponseEntity<TabPage<User>> page(@RequestBody Map<String, Object> param) {
		return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
	}

    @Log("保存用户")
    @ApiOperation(value = "2、保存")
    @PostMapping(value="save")
    public ResponseEntity<User> save(@RequestBody User entity) {
        return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("批量保存用户")
    @ApiOperation(value = "3、批量保存")
    @PostMapping(value="batchSave")
    public ResponseEntity<SaveModel<User>> batchSave(@RequestBody SaveModel<User> entity) {
        return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("删除用户")
    @ApiOperation(value = "4、删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id数组", required = true),
    })
	@DeleteMapping(value="del")
	public ResponseEntity<Object> del(@RequestBody List<Object> ids) {
		service.deleteByIds(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

    @Log("加载用户")
    @ApiOperation(value = "5、加载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "数据主键", required = true),
    })
    @GetMapping(value="load")
    public ResponseEntity<User> load(String id) {
    	return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
    }

    @ApiOperation(value = "5、获取用户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名称", required = true),
    })
    @GetMapping(value="getUserByName")
    public ResponseEntity<UserDto> getUserByName(String username) {
        return new ResponseEntity(service.loadByUserName(username), HttpStatus.OK);
    }

    @Log("导出用户")
    @ApiOperation(value = "6、导出")
    @PostMapping(value="download")
    public void download(@RequestBody Map<String, Object> param,HttpServletResponse response) throws IOException {
    	service.download(service.selectForPage(param).getData(), response);
    }
    
}
