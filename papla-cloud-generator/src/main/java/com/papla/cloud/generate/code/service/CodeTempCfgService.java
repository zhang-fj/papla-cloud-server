package com.papla.cloud.generate.code.service;

import com.papla.cloud.common.mybatis.service.BaseService;
import com.papla.cloud.generate.code.domain.CodeTempCfg;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: CodeTempCfgService
 * @Description: TODO   模板配置管理
 * @date 2021-09-02
 */
public interface CodeTempCfgService extends BaseService<CodeTempCfg> {

    /**
     * 导出数据
     *
     * @param entitys  待导出的数据
     * @param response
     * @throws IOException
     */
    void download(List<CodeTempCfg> entitys, HttpServletResponse response) throws IOException;

}