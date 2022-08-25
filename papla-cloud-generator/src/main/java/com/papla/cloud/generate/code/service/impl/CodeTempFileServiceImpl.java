package com.papla.cloud.generate.code.service.impl;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.generate.code.domain.CodeTempFile;
import com.papla.cloud.generate.code.mapper.CodeTempFileMapper;
import com.papla.cloud.generate.code.service.CodeTempFileService;
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
 * @Title: CodeTempFileServiceImpl
 * @Description: TODO   模板文件管理
 * @date 2021-09-02
 */
@Service
@RequiredArgsConstructor
public class CodeTempFileServiceImpl extends BaseServiceImpl<CodeTempFile> implements CodeTempFileService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Logger getLogger(){
        return logger;
    }

    private final CodeTempFileMapper mapper;

    @Override
    public BaseMapper<CodeTempFile> getMapper(){
        return mapper;
    }

    @Override
    public void download(List<CodeTempFile> entitys, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CodeTempFile codeTempFile : entitys) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("模板配置ID", codeTempFile.getCfgId());
            map.put("模板名称", codeTempFile.getTempName());
            map.put("文件名称", codeTempFile.getTempFileName());
            map.put("文件路径", codeTempFile.getTempFilePath());
            map.put("生成路径", codeTempFile.getGenFilePath());
            map.put("文件前缀", codeTempFile.getGenFilePrefix());
            map.put("文件后缀", codeTempFile.getGenFileSuffix());
            map.put("文件格式", codeTempFile.getGenFileFormat());
            map.put("创建日期", codeTempFile.getCreateDt());
            map.put("创建人", codeTempFile.getCreateBy());
            map.put("最后更新日期", codeTempFile.getUpdateDt());
            map.put("最后更新人", codeTempFile.getUpdateBy());
            map.put("资源路径", codeTempFile.getGenRootPath());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
