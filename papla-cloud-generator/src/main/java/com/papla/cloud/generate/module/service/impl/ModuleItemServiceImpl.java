package com.papla.cloud.generate.module.service.impl;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.generate.module.domain.ModuleItem;
import com.papla.cloud.generate.module.mapper.ModuleItemMapper;
import com.papla.cloud.generate.module.service.ModuleItemService;
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
 * @Title: ModuleItemServiceImpl
 * @Description: TODO   模块管理管理
 * @date 2021-09-03
 */
@Service
@RequiredArgsConstructor
public class ModuleItemServiceImpl extends BaseServiceImpl<ModuleItem> implements ModuleItemService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Logger getLogger(){
        return logger;
    }

    private final ModuleItemMapper mapper;

    @Override
    public BaseMapper<ModuleItem> getMapper(){
        return mapper;
    }

    @Override
    public void download(List<ModuleItem> entitys, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ModuleItem moduleItem : entitys) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("项目ID", moduleItem.getProjectId());
            map.put("模块编码", moduleItem.getModuleCode());
            map.put("模块名称", moduleItem.getModuleName());
            map.put("模块包名", moduleItem.getPackageName());
            map.put("创建日期", moduleItem.getCreateDt());
            map.put("创建人", moduleItem.getCreateBy());
            map.put("最后更新日期", moduleItem.getUpdateDt());
            map.put("最后更新人", moduleItem.getUpdateBy());
            map.put("模块路径", moduleItem.getModulePath());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
