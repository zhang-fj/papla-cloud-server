package com.papla.cloud.generate.module.service.impl;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.generate.module.domain.ModuleProject;
import com.papla.cloud.generate.module.mapper.ModuleProjectMapper;
import com.papla.cloud.generate.module.service.ModuleProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangfj
 * @version V1.0
 * @Title: ModuleProjectServiceImpl
 * @Description: TODO   项目管理管理
 * @date 2021-09-03
 */
@Service
@RequiredArgsConstructor
public class ModuleProjectServiceImpl extends BaseServiceImpl<ModuleProject> implements ModuleProjectService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Logger getLogger(){
        return logger;
    }

    private final ModuleProjectMapper mapper;

    @Override
    public BaseMapper<ModuleProject> getMapper(){
        return mapper;
    }

    @Override
    public void download(List<ModuleProject> entitys, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ModuleProject moduleProject : entitys) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("项目编码", moduleProject.getProjectCode());
            map.put("项目名称", moduleProject.getProjectName());
            map.put("项目作者", moduleProject.getProjectAuth());
            map.put("工程名称", moduleProject.getProjectPath());
            map.put("创建日期", moduleProject.getCreateDt());
            map.put("创建人", moduleProject.getCreateBy());
            map.put("最后更新日期", moduleProject.getUpdateDt());
            map.put("最后更新人", moduleProject.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
