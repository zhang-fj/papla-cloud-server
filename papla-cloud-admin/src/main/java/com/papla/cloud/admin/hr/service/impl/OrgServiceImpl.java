package com.papla.cloud.admin.hr.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.papla.cloud.admin.hr.mapper.OrgMapper;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.papla.cloud.admin.hr.domain.Org;
import com.papla.cloud.admin.hr.service.OrgService;

/**
 * @Title: OrgServiceImpl
 * @Description: TODO 组织管理
 * @author
 * @date 2021-09-26
 * @version V1.0
 */
@Service
@RequiredArgsConstructor
public class OrgServiceImpl extends BaseServiceImpl<Org> implements OrgService{

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Logger getLogger(){
        return logger;
    }

    private final OrgMapper mapper;

    @Override
    public BaseMapper<Org> getMapper(){
        return mapper;
    }

    @Override
    public void download(List<Org> entitys, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Org org : entitys) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("组织编码", org.getOrgCode());
            map.put("组织名称", org.getOrgName());
            map.put("组织简称", org.getOrgShortName());
            map.put("上级组织", org.getUpOrgId());
            map.put("组织类型", org.getOrgType());
            map.put("是否启用", org.getEnabled());
            map.put("创建日期", org.getCreateDt());
            map.put("创建人", org.getCreateBy());
            map.put("最后更新日期", org.getUpdateDt());
            map.put("最后更新人", org.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
