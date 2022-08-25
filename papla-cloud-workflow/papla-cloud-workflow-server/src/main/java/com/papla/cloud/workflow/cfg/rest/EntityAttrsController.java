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

import com.papla.cloud.workflow.cfg.domain.EntityAttrs;
import com.papla.cloud.workflow.cfg.service.EntityAttrsService;

/**
* @Title: EntityAttrsController.java
* @Description: TODO 实体属性管理
* @author 
* @date 2021-09-29
* @version V1.0
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "实体属性管理")
@RequestMapping("/cfg/entityAttrs")
public class EntityAttrsController {

	private final EntityAttrsService service;
	
	@Log("查询【实体属性】")
	@ApiOperation("查询【实体属性】数据")
	@ApiImplicitParams({})
	@PostMapping(value="page")
	public ResponseEntity<TabPage<EntityAttrs>> page(@RequestBody Map<String, Object> param) throws Exception{
		return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
	}
	
	@Log("保存【实体属性】")
	@ApiOperation("保存【实体属性】数据")
	@PostMapping(value="save")
	public ResponseEntity<EntityAttrs> save(@RequestBody EntityAttrs entity) throws Exception{
		return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
	}
	
	@Log("批量保存【实体属性】")
	@ApiOperation(value = "批量保存【实体属性】数据")
	@PostMapping(value="batchSave")
	public ResponseEntity<SaveModel<EntityAttrs>> batchSave(@RequestBody SaveModel<EntityAttrs> entity){
	    return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
	}
	
	@Log("删除【实体属性】")
	@ApiOperation(value = "删除【实体属性】数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "id数组", required = true),
	})
	@DeleteMapping(value="del")
	public ResponseEntity<Object> del(@RequestBody List<Object> ids){
		service.deleteByIds(ids);
	    return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@Log("加载【实体属性】")
	@ApiOperation("加载【实体属性】数据")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "数据主键", required = true),
	})
	@GetMapping(value="load")
	public ResponseEntity<EntityAttrs> load(String id){
		return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
	}
	
	@Log("导出【实体属性】")
	@ApiOperation("导出【实体属性】数据")
	@PostMapping(value="download")
	public void download(@RequestBody Map<String, Object> param,HttpServletResponse response) throws IOException {
		service.download(service.selectForPage(param).getData(), response);
	}
    
}
