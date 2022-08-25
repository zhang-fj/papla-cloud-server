package com.papla.cloud.generate.module.rest;

import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.generate.module.domain.ModuleColumn;
import com.papla.cloud.generate.module.service.ModuleColumnService;
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
 * @author zhangfj·
 * @version V1.0
 * @Title: ModuleColumnController.java
 * @Description: TODO   字段配置管理
 * @date 2021-09-03
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "【字段配置】")
@RequestMapping("/module/moduleColumn")
public class ModuleColumnController {

    private final ModuleColumnService service;


    @Log("查询【字段配置】清单")
    @ApiOperation("查询【字段配置】清单")
    @RequestMapping(value = "columns", method = RequestMethod.POST)
    public ResponseEntity<List<ModuleColumn>> columns(@RequestBody Map<String, String> param) throws Exception {
        return new ResponseEntity(service.columns(param.get("tableId")), HttpStatus.OK);
    }

    @Log("同步【字段配置】数据")
    @ApiOperation("查询【字段配置】数据")
    @RequestMapping(value = "synch", method = RequestMethod.POST)
    public ResponseEntity<Object> synch(@RequestBody Map<String, String> param) throws Exception {
        service.synch(param.get("tableId"));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("查询【字段配置】数据")
    @ApiOperation("查询【字段配置】数据")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ResponseEntity<TabPage<ModuleColumn>> page(@RequestBody Map<String, Object> param) throws Exception{
        return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
    }

    @Log("删除【字段配置】数据")
    @ApiOperation("删除【字段配置】数据")
    @DeleteMapping(value="del")
    public ResponseEntity<Object> del(@RequestBody List<Object> ids){
        service.deleteByIds(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("批量保存【字段配置】数据")
    @ApiOperation("批量保存【字段配置】数据")
    @RequestMapping(value = "batchSave", method = RequestMethod.POST)
    public ResponseEntity<SaveModel<ModuleColumn>> batchSave(@RequestBody SaveModel<ModuleColumn> entity){
        return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("新增或修改【字段配置】")
    @ApiOperation("新增或修改【字段配置】")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<ModuleColumn> save(@RequestBody ModuleColumn entity) throws Exception{
        return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("加载【字段配置】数据")
    @ApiOperation("加载【字段配置】数据")
    @RequestMapping(value = "load", method = RequestMethod.POST)
    public ResponseEntity<ModuleColumn> load(String id) {
        return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
    }

    @Log("导出【字段配置】数据")
    @ApiOperation("导出【字段配置】数据")
    @RequestMapping(value = "download", method = RequestMethod.POST)
    public void download(@RequestBody Map<String, Object> param, HttpServletResponse response) throws Exception {
        service.download(service.selectForPage(param).getData(), response);
    }
}
