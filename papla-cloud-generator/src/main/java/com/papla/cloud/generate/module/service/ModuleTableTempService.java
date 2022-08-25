package com.papla.cloud.generate.module.service;

import com.papla.cloud.common.mybatis.service.BaseService;
import com.papla.cloud.generate.module.domain.ModuleTableTemp;
import com.papla.cloud.generate.module.model.ModuleTableTempSaveModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleTableTempService
 * @Description: TODO   模块关联表模板配置管理
 * @date 2021-09-05
 */
public interface ModuleTableTempService extends BaseService<ModuleTableTemp> {


    public List<ModuleTableTemp> temps(Map<String, Object> params) throws Exception;


    /**
     * @param data
     * @return ModuleTableTempSaveModel
     * @throws Exception
     * @Title batchSave
     * @Description TODO
     */
    ModuleTableTempSaveModel batchSave(ModuleTableTempSaveModel data) throws Exception;

    /**
     * 导出数据
     *
     * @param entitys  待导出的数据
     * @param response
     * @throws IOException
     */
    void download(List<ModuleTableTemp> entitys, HttpServletResponse response) throws IOException;


}