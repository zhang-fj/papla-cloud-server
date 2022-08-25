package com.papla.cloud.generate.module.rest;

import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.generate.module.domain.ModuleItem;
import com.papla.cloud.generate.module.service.ModuleItemService;
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
 * @Title: ModuleItemController.java
 * @Description: TODO   模块管理管理
 * @date 2021-09-03
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "【模块管理】")
@RequestMapping("/module/moduleItem")
public class ModuleItemController {

    private final ModuleItemService service;

    @Log("查询【模块管理】数据")
    @ApiOperation("查询【模块管理】数据")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ResponseEntity<TabPage<ModuleItem>> page(@RequestBody Map<String, Object> param) throws Exception{
        return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
    }

    @Log("删除【模块管理】数据")
    @ApiOperation("删除【模块管理】数据")
    @DeleteMapping(value="del")
    public ResponseEntity<Object> del(@RequestBody List<Object> ids){
        service.deleteByIds(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("批量保存【模块管理】数据")
    @ApiOperation("批量保存【模块管理】数据")
    @RequestMapping(value = "batchSave", method = RequestMethod.POST)
    public ResponseEntity<SaveModel<ModuleItem>> batchSave(@RequestBody SaveModel<ModuleItem> entity){
        return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("新增或修改【模块管理】")
    @ApiOperation("新增或修改【模块管理】")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<ModuleItem> save(@RequestBody ModuleItem entity) throws Exception{
        return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("加载【模块管理】数据")
    @ApiOperation("加载【模块管理】数据")
    @RequestMapping(value = "load", method = RequestMethod.POST)
    public ResponseEntity<ModuleItem> load(String id) {
        return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
    }

    @Log("导出【模块管理】数据")
    @ApiOperation("导出【模块管理】数据")
    @RequestMapping(value = "download", method = RequestMethod.POST)
    public void download(@RequestBody Map<String, Object> param, HttpServletResponse response) throws Exception {
        service.download(service.selectForPage(param).getData(), response);
    }
}
