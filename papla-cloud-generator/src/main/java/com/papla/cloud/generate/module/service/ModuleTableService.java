package com.papla.cloud.generate.module.service;

import com.papla.cloud.common.mybatis.service.BaseService;
import com.papla.cloud.generate.module.domain.ModuleTable;
import com.papla.cloud.generate.module.domain.Table;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleTableService
 * @Description: TODO   关联表管理
 * @date 2021-09-03
 */
public interface ModuleTableService extends BaseService<ModuleTable> {

    /**
     * 获取数据库表
     * @param projectId  项目ID
     * @throws IOException
     */
    List<Table> tables(String projectId);

    /**
     * 导出数据
     * @param entitys  待导出的数据
     * @param response
     * @throws IOException
     */
    void download(List<ModuleTable> entitys, HttpServletResponse response) throws IOException;

}