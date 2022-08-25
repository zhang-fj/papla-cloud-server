package com.papla.cloud.generate.module.service;

import com.papla.cloud.common.mybatis.service.BaseService;
import com.papla.cloud.generate.module.domain.ModuleProject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleProjectService
 * @Description: TODO   项目管理管理
 * @date 2021-09-03
 */
public interface ModuleProjectService extends BaseService<ModuleProject> {

    /**
     * 导出数据
     *
     * @param entitys  待导出的数据
     * @param response
     * @throws IOException
     */
    void download(List<ModuleProject> entitys, HttpServletResponse response) throws IOException;

}