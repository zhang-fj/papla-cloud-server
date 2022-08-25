package com.papla.cloud.generate.code.rest;

import com.papla.cloud.common.logging.annotation.Log;
import com.papla.cloud.common.mybatis.model.SaveModel;
import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.generate.code.domain.CodeTempFile;
import com.papla.cloud.generate.code.service.CodeTempFileService;
import com.papla.cloud.generate.code.service.CodeTempFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: CodeTempFileController.java
 * @Description: TODO   模板文件管理
 * @date 2021-09-02
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "【模板文件】管理")
@RequestMapping("/code/codeTempFile")
public class CodeTempFileController {

    private final CodeTempFileService service;

    @Log("查询【模板配置】数据")
    @ApiOperation("查询【模板配置】数据")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ResponseEntity<TabPage<CodeTempFile>> page(@RequestBody Map<String, Object> param) throws Exception{
        return new ResponseEntity(service.selectForPage(param), HttpStatus.OK);
    }

    @Log("删除【模板配置】数据")
    @ApiOperation("删除【模板配置】数据")
    @DeleteMapping(value="del")
    public ResponseEntity<Object> del(@RequestBody List<Object> ids){
        service.deleteByIds(ids);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("批量保存【模板配置】数据")
    @ApiOperation("批量保存【模板配置】数据")
    @RequestMapping(value = "batchSave", method = RequestMethod.POST)
    public ResponseEntity<SaveModel<CodeTempFile>> batchSave(@RequestBody SaveModel<CodeTempFile> entity){
        return new ResponseEntity(service.batchSaveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("新增或修改【模板配置】")
    @ApiOperation("新增或修改【模板配置】")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<CodeTempFile> save(@RequestBody CodeTempFile entity) throws Exception{
        return new ResponseEntity(service.saveOrUpdate(entity), HttpStatus.OK);
    }

    @Log("加载【模板配置】数据")
    @ApiOperation("加载【模板配置】数据")
    @RequestMapping(value = "load", method = RequestMethod.POST)
    public ResponseEntity<CodeTempFile> load(String id) {
        return new ResponseEntity(service.selectByPK(id), HttpStatus.OK);
    }

    @Log("导出【模板配置】数据")
    @ApiOperation("导出【模板配置】数据")
    @RequestMapping(value = "download", method = RequestMethod.POST)
    public void download(@RequestBody Map<String, Object> param, HttpServletResponse response) throws Exception {
        service.download(service.selectForPage(param).getData(), response);
    }
}
