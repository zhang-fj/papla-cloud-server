package com.papla.cloud.admin.auth.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.auth.domain.Perm;
import com.papla.cloud.admin.auth.service.PermService;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags="7、资源权限管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/perm")
public class PermController {

	private final PermService service;

    @Log("查询资源权限")
    @ApiOperation(value = "1、查询")
    @ApiImplicitParams({})
    @PostMapping(value="page")
	public ResponseEntity<TabPage<Perm>> page(@RequestBody Map<String, Object> param){
		return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
	}

    @Log("保存资源权限")
    @ApiOperation(value = "2、保存")
    @PostMapping(value="save")
    public ResponseEntity<Perm> save(@RequestBody Perm entity){
    	return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("批量保存资源权限")
    @ApiOperation(value = "3、批量保存")
    @PostMapping(value="batchSave")
    public ResponseEntity<SaveModel<Perm>> batchSave(@RequestBody SaveModel<Perm> entity){
        return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("删除资源权限")
    @ApiOperation(value = "4、删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id数组", required = true),
    })
    @DeleteMapping(value="del")
    public ResponseEntity<Object> del(@RequestBody List<Object> ids){
        service.deleteByIds(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("加载资源权限")
    @ApiOperation(value = "5、加载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "数据主键", required = true),
    })
    @GetMapping(value="load")
    public ResponseEntity<Perm> load(String id){
    	return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
    }

    @Log("导出资源权限")
    @ApiOperation(value = "6、导出")
    @PostMapping(value="download")
    public void download(@RequestBody Map<String, Object> param,HttpServletResponse response) throws IOException {
    	service.download(service.selectForPage(param).getData(), response);
    }
    
}
