package com.papla.cloud.admin.hr.rest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

import com.papla.cloud.admin.hr.domain.Post;
import com.papla.cloud.admin.hr.service.PostService;

/**
* @Title: PostController.java
* @Description: TODO 岗位管理
* @author 
* @date 2021-09-26
* @version V1.0
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "岗位管理")
@RequestMapping("/hr/post")
public class PostController {

	private final PostService service;
	
	@Log("查询【岗位】")
	@ApiOperation("查询【岗位】数据")
	@ApiImplicitParams({})
	@PostMapping(value="page")
	public ResponseEntity<TabPage<Post>> page(@RequestBody Map<String, Object> param) throws Exception{
		return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
	}
	
	@Log("保存【岗位】")
	@ApiOperation("保存【岗位】数据")
	@PostMapping(value="save")
	public ResponseEntity<Post> save(@RequestBody Post entity) throws Exception{
		return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
	}
	
	@Log("批量保存【岗位】")
	@ApiOperation(value = "批量保存【岗位】数据")
	@PostMapping(value="batchSave")
	public ResponseEntity<SaveModel<Post>> batchSave(@RequestBody SaveModel<Post> entity){
	    return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
	}
	
	@Log("删除【岗位】")
	@ApiOperation(value = "删除【岗位】数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "id数组", required = true),
	})
	@DeleteMapping(value="del")
	public ResponseEntity<Object> del(@RequestBody List<Object> ids){
		service.deleteByIds(ids);
	    return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@Log("加载【岗位】")
	@ApiOperation("加载【岗位】数据")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "数据主键", required = true),
	})
	@GetMapping(value="load")
	public ResponseEntity<Post> load(String id){
		return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
	}
	
	@Log("导出【岗位】")
	@ApiOperation("导出【岗位】数据")
	@PostMapping(value="download")
	public void download(@RequestBody Map<String, Object> param,HttpServletResponse response) throws IOException {
		service.download(service.selectForPage(param).getData(), response);
	}

    @Log("新增【员工岗位分配】")
    @ApiOperation(value = "新增【员工岗位分配】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "postId", value = "岗位ID", required = true),
        @ApiImplicitParam(name = "empIds", value = "员工ID数组",required = true),
    })
    @PostMapping(value="addEmpPostAsg")
    public ResponseEntity<Object> addEmpPostAsg(@RequestBody Map<String,Object> params){
        service.insertEmpPostAsg(params);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除【员工岗位分配】")
    @ApiOperation(value = "删除【员工岗位分配】")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "postId", value = "岗位ID", required = true),
        @ApiImplicitParam(name = "empIds", value = "员工ID数组",required = true),
    })
    @PostMapping(value="delEmpPostAsg")
    public ResponseEntity<SaveModel<Post>> delEmpPostAsg(@RequestBody Map<String,Object> params){
        service.deleteEmpPostAsg(params);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
}
