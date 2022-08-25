package com.papla.cloud.generate.code.service.impl;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.generate.code.domain.CodeTempCfg;
import com.papla.cloud.generate.code.mapper.CodeTempCfgMapper;
import com.papla.cloud.generate.code.service.CodeTempCfgService;
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
 * @Title: CodeTempCfgServiceImpl
 * @Description: TODO   模板配置管理
 * @date 2021-09-02
 */
@Service
@RequiredArgsConstructor
public class CodeTempCfgServiceImpl extends BaseServiceImpl<CodeTempCfg> implements CodeTempCfgService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Logger getLogger(){
        return logger;
    }

    private final CodeTempCfgMapper mapper;

    @Override
    public BaseMapper<CodeTempCfg> getMapper(){
        return mapper;
    }

    @Override
    public void download(List<CodeTempCfg> entitys, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CodeTempCfg codeTempCfg : entitys) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("模板配置编码", codeTempCfg.getCfgCode());
            map.put("模板配置名称", codeTempCfg.getCfgName());
            map.put("创建日期", codeTempCfg.getCreateDt());
            map.put("创建人", codeTempCfg.getCreateBy());
            map.put("最后更新日期", codeTempCfg.getUpdateDt());
            map.put("最后更新人", codeTempCfg.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
