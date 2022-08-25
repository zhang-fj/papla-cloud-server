package com.papla.cloud.generate.module.service;

import com.papla.cloud.common.mybatis.service.BaseService;
import com.papla.cloud.generate.module.domain.Column;
import com.papla.cloud.generate.module.domain.ModuleColumn;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangfj·
 * @version V1.0
 * @Title: ModuleColumnService
 * @Description: TODO   字段配置管理
 * @date 2021-09-03
 */
public interface ModuleColumnService extends BaseService<ModuleColumn> {

    /**
     * @param tableId
     * @param tableId
     * @return List<ModuleColumn>
     * @throws Exception
     * @Title columns
     * @Description TODO 获取配置列
     */
    public List<ModuleColumn> columns(String tableId) throws Exception;

    /**
     * @param projectId
     * @param tableName
     * @return List<Column>
     * @throws Exception
     * @Title columns
     * @Description TODO 获取数据库列
     */
    List<Column> columns(String projectId, String tableName);

    /**
     * @param tableId
     * @param tableId
     * @return
     * @throws Exception
     * @Title synch
     * @Description TODO 同步配置
     */
    public void synch(String tableId) throws Exception;

    /**
     * 导出数据
     * @param entitys  待导出的数据
     * @param response
     * @throws IOException
     */
    void download(List<ModuleColumn> entitys, HttpServletResponse response) throws IOException;


}