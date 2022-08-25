package com.papla.cloud.admin.hr.service;

import java.io.IOException;

import com.papla.cloud.common.mybatis.service.BaseService;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.hr.domain.Org;

/**
 * @Title: OrgService
 * @Description: TODO 组织管理
 * @author
 * @date 2021-09-26
 * @version V1.0
 */
public interface OrgService extends BaseService<Org> {

    /**
     * 导出数据
     * @param entitys 待导出的数据
     * @param response
     * @throws IOException
     */
    void download(List<Org> entitys, HttpServletResponse response) throws IOException;

}