package com.papla.cloud.workflow.cfg.rest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.common.mybatis.model.TabPage;

import com.papla.cloud.workflow.cfg.domain.EntityForms;
import com.papla.cloud.workflow.cfg.service.EntityFormsService;

/**
* @Title: EntityFormsController.java
* @Description: TODO 实体表单管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "实体表单管理")
@RequestMapping("/cfg/entityForms")
public class EntityFormsController {

	private final EntityFormsService service;
	
	@Log("查询【实体表单】")
	@ApiOperation("查询【实体表单】数据")
	@ApiImplicitParams({})
	@PostMapping(value="page")
	public ResponseEntity<TabPage<EntityForms>> page(@RequestBody Map<String, Object> param) throws Exception{
		return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
	}
	
	@Log("保存【实体表单】")
	@ApiOperation("保存【实体表单】数据")
	@PostMapping(value="save")
	public ResponseEntity<EntityForms> save(@RequestBody EntityForms entity) throws Exception{
		return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
	}
	
	@Log("批量保存【实体表单】")
	@ApiOperation(value = "批量保存【实体表单】数据")
	@PostMapping(value="batchSave")
	public ResponseEntity<SaveModel<EntityForms>> batchSave(@RequestBody SaveModel<EntityForms> entity){
	    return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
	}
	
	@Log("删除【实体表单】")
	@ApiOperation(value = "删除【实体表单】数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "id数组", required = true),
	})
	@DeleteMapping(value="del")
	public ResponseEntity<Object> del(@RequestBody List<Object> ids){
		service.deleteByIds(ids);
	    return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@Log("加载【实体表单】")
	@ApiOperation("加载【实体表单】数据")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "数据主键", required = true),
	})
	@GetMapping(value="load")
	public ResponseEntity<EntityForms> load(String id){
		return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
	}
	
	@Log("导出【实体表单】")
	@ApiOperation("导出【实体表单】数据")
	@PostMapping(value="download")
	public void download(@RequestBody Map<String, Object> param,HttpServletResponse response) throws IOException {
		service.download(service.selectForPage(param).getData(), response);
	}
    
}
