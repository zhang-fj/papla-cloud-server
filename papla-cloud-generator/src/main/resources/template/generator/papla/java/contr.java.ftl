package ${package}.rest;

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

import ${package}.domain.${ClassName};
import ${package}.service.${ClassName}Service;

/**
* @Title: ${ClassName}Controller.java
* @Description: TODO ${apiAlias}管理
* @author ${author}
* @date ${date}
* @version V1.0
*/
@RestController
@RequiredArgsConstructor
@Api(tags = "${apiAlias}管理")
@RequestMapping("/${modulePath}/${className}")
public class ${ClassName}Controller {

	private final ${ClassName}Service service;
	
	@Log("查询【${apiAlias}】")
	@ApiOperation("查询【${apiAlias}】数据")
	@ApiImplicitParams({})
	@PostMapping(value="page")
	public ResponseEntity<TabPage<${ClassName}>> page(@RequestBody Map<String, Object> param) throws Exception{
		return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
	}
	
	@Log("保存【${apiAlias}】")
	@ApiOperation("保存【${apiAlias}】数据")
	@PostMapping(value="save")
	public ResponseEntity<${ClassName}> save(@RequestBody ${ClassName} entity) throws Exception{
		return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
	}
	
	@Log("批量保存【${apiAlias}】")
	@ApiOperation(value = "批量保存【${apiAlias}】数据")
	@PostMapping(value="batchSave")
	public ResponseEntity<SaveModel<${ClassName}>> batchSave(@RequestBody SaveModel<${ClassName}> entity){
	    return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
	}
	
	@Log("删除【${apiAlias}】")
	@ApiOperation(value = "删除【${apiAlias}】数据")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "id数组", required = true),
	})
	@DeleteMapping(value="del")
	public ResponseEntity<Object> del(@RequestBody List<Object> ids){
		service.deleteByIds(ids);
	    return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@Log("加载【${apiAlias}】")
	@ApiOperation("加载【${apiAlias}】数据")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "数据主键", required = true),
	})
	@GetMapping(value="load")
	public ResponseEntity<${ClassName}> load(String id){
		return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
	}
	
	@Log("导出【${apiAlias}】")
	@ApiOperation("导出【${apiAlias}】数据")
	@PostMapping(value="download")
	public void download(@RequestBody Map<String, Object> param,HttpServletResponse response) throws IOException {
		service.download(service.selectForPage(param).getData(), response);
	}
    
}
