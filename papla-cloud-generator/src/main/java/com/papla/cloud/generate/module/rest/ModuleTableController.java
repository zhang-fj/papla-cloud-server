package com.papla.cloud.generate.module.rest;

import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.generate.module.domain.ModuleTable;
import com.papla.cloud.generate.module.domain.Table;
import com.papla.cloud.generate.module.service.ModuleTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleTableController.java
 * @Description: TODO   关联表管理
 * @date 2021-09-03
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "【关联表】")
@RequestMapping("/module/moduleTable")
public class ModuleTableController {
    
    private final ModuleTableService service;

    @Log("查询【数据库表】数据")
    @ApiOperation("查询【数据库表】数据")
    @RequestMapping(value = "tables", method = RequestMethod.GET)
    public ResponseEntity<List<Table>> tables(String projectId){
        return new ResponseEntity(service.tables(projectId), HttpStatus.OK);
    }

    @Log("查询【关联表】数据")
    @ApiOperation("查询【关联表】数据")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ResponseEntity<TabPage<ModuleTable>> page(@RequestBody Map<String, Object> param) throws Exception{
        return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
    }

    @Log("删除【关联表】数据")
    @ApiOperation("删除【关联表】数据")
    @DeleteMapping(value="del")
    public ResponseEntity<Object> del(@RequestBody List<Object> ids){
        service.deleteByIds(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("批量保存【关联表】数据")
    @ApiOperation("批量保存【关联表】数据")
    @RequestMapping(value = "batchSave", method = RequestMethod.POST)
    public ResponseEntity<SaveModel<ModuleTable>> batchSave(@RequestBody SaveModel<ModuleTable> entity){
        return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("新增或修改【关联表】")
    @ApiOperation("新增或修改【关联表】")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<ModuleTable> save(@RequestBody ModuleTable entity) throws Exception{
        return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("加载【关联表】数据")
    @ApiOperation("加载【关联表】数据")
    @RequestMapping(value = "load", method = RequestMethod.POST)
    public ResponseEntity<ModuleTable> load(String id) {
        return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
    }

    @Log("导出【关联表】数据")
    @ApiOperation("导出【关联表】数据")
    @RequestMapping(value = "download", method = RequestMethod.POST)
    public void download(@RequestBody Map<String, Object> param, HttpServletResponse response) throws Exception {
        service.download(service.selectForPage(param).getData(), response);
    }
}
