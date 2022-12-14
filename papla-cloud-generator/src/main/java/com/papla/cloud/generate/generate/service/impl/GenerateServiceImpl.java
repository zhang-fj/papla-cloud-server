package com.papla.cloud.generate.generate.service.impl;

import com.google.common.base.CaseFormat;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.generate.generate.GenerateConfig;
import com.papla.cloud.generate.generate.domain.ModuleGenerateConfig;
import com.papla.cloud.generate.generate.mapper.GenerateMapper;
import com.papla.cloud.generate.generate.service.GenerateService;
import com.papla.cloud.generate.generate.util.ColUtil;
import com.papla.cloud.generate.generate.util.GenUtil;
import com.papla.cloud.generate.module.domain.ModuleColumn;
import com.papla.cloud.generate.module.mapper.ModuleColumnMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GenerateServiceImpl implements GenerateService {

    private final GenerateMapper mapper;

    private final ModuleColumnMapper moduleColumnMapper;

    @Override
    public List<Map<String, Object>> preview(Map<String, Object> params) throws Exception {

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        List<ModuleGenerateConfig> configs = mapper.queryGenerateConfigList(params);

        Map<String, Object> columnsMap = getColumnsData(params);

        for (ModuleGenerateConfig config : configs) {

            Map<String, Object> map = new HashMap<String, Object>();

            GenerateConfig cfg = getGenerateConfig(config);
            cfg.putColumnsData(columnsMap);

            try {
                map.put("content", GenUtil.renderCode(cfg));
            } catch (Exception e) {
                map.put("content", e.getMessage());
            }

            map.put("name", config.getTempFileName());

            result.add(map);
        }
        return result;
    }

    @Override
    public String download(Map<String, Object> params) throws Exception {

        String tempPath = FileUtil.SYS_TEM_DIR + "papla-gen-temp" + "/" + params.get("tableId") + "/";

        List<ModuleGenerateConfig> configs = mapper.queryGenerateConfigList(params);
        Map<String, Object> columnsMap = getColumnsData(params);

        for (ModuleGenerateConfig config : configs) {

            config.setProjectPath(tempPath + "papla-cloud");
            config.setPagePath(tempPath + "papla-cloud-web");
            GenerateConfig cfg = getGenerateConfig(config);
            cfg.putColumnsData(columnsMap);

            GenUtil.generatorCode(cfg);
        }
        return tempPath;
    }

    @Override
    public void generator(Map<String, Object> params) throws Exception {

        List<ModuleGenerateConfig> configs = mapper.queryGenerateConfigList(params);
        Map<String, Object> columnsMap = getColumnsData(params);

        for (ModuleGenerateConfig config : configs) {

            GenerateConfig cfg = getGenerateConfig(config);
            cfg.putColumnsData(columnsMap);
            GenUtil.generatorCode(cfg);

        }
    }

    private GenerateConfig getGenerateConfig(ModuleGenerateConfig config) {

        GenerateConfig cfg = new GenerateConfig();

        cfg.setSrcMode(config.getSrcMode());
        cfg.setCover(config.getCover());
        cfg.setOutFileName(config.getOutFileName());
        cfg.setOutFilePath(config.getOutFilePath());
        cfg.setTemplate(config.getTemplate());

        Map<String, Object> baseData = new HashMap<String, Object>();

        // ????????????
        baseData.put("apiAlias", config.getApiAlias());
        // ?????????
        baseData.put("package", config.getPackageName());
        // ????????????
        baseData.put("modulePath", config.getModulePath());
        // ??????
        baseData.put("author", "");
        // ????????????
        baseData.put("date", LocalDate.now().toString());
        // ??????
        baseData.put("tableName", config.getTableName());

        // ????????????
        baseData.put("ClassName", config.getClassName());
        baseData.put("className", config.getLowerClassName());
        baseData.put("hyphenClassName", config.getHyphenClassName());

        cfg.putBaseData(baseData);
        return cfg;
    }

    private Map<String, Object> getColumnsData(Map<String, Object> params) throws Exception {

        List<ModuleColumn> cols = moduleColumnMapper.findAll(params);

        Map<String, Object> genMap = new HashMap<String, Object>();

        // ?????????
        List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
        genMap.put("columns", columns);

        // ?????????
        List<Map<String, Object>> queryColumns = new ArrayList<Map<String, Object>>();
        genMap.put("queryColumns", queryColumns);

        // ?????????
        List<Map<String, Object>> notNullColumns = new ArrayList<Map<String, Object>>();
        genMap.put("notNullColumns", notNullColumns);

        // ????????????
        Set<String> dicts = new HashSet<String>();
        genMap.put("dicts", dicts);

        for (ModuleColumn column : cols) {

            Map<String, Object> columnMap = new HashMap<String, Object>();

            String columnName = column.getColumnName().toUpperCase();
            String lowerColumnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
            String upperColumnName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, column.getColumnName());

            String columnType = column.getColumnType();
            String javaType = ColUtil.cloToJava(columnType);
            String jdbcType = ColUtil.cloToJdbcType(columnType);

            // ???????????????
            columnMap.put("columnName", columnName);
            columnMap.put("lowerColumnName", lowerColumnName);
            columnMap.put("upperColumnName", upperColumnName);

            // ???????????????
            columnMap.put("columnType", columnType);
            columnMap.put("javaType", javaType);
            columnMap.put("jdbcType", jdbcType);

            // ???????????????
            columnMap.put("tableName", column.getTableName());
            columnMap.put("columnKey", column.getKeyType());
            // ????????????
            columnMap.put("aliasName", StringUtils.isNotBlank(column.getAliasName()) ? column.getAliasName() : column.getRemark());
            // ????????????
            columnMap.put("remark", column.getRemark());
            // ????????????
            columnMap.put("notNull", "YES".equals(column.getNotNull()));
            // ????????????
            columnMap.put("listShow", "YES".equals(column.getListShow()));
            // ????????????
            columnMap.put("formShow", "YES".equals(column.getFormShow()));
            // ????????????
            columnMap.put("formType", column.getFormType());
            // ????????????
            columnMap.put("queryType", StringUtils.isNotBlank(column.getQueryType()) ? column.getQueryType() : column.getFormType());
            // ????????????
            columnMap.put("queryWay", column.getQueryWay());
            // ????????????
            columnMap.put("dictName", column.getDictName());

            // ????????????
            if (StringUtils.isNotBlank(column.getDictName())) {
                dicts.add(column.getDictName());
            }

            // ????????????
            if (StringUtils.isNotBlank(column.getQueryWay())) {
                queryColumns.add(columnMap);
            }

            // ??????NULL??????
            if ("YES".equals(column.getNotNull())) {
                notNullColumns.add(columnMap);
            }

            if ("PRI".equals(column.getKeyType())) {
                //????????????
                genMap.put("pkColumnName", columnName);
                genMap.put("pkUpperColumnName", upperColumnName);
                genMap.put("pkLowerColumnName", lowerColumnName);
                // ??????????????????
                genMap.put("pkColumnType", javaType);
                genMap.put("columnType", columnType);
                genMap.put("pkJdbcType", jdbcType);
            }

            columns.add(columnMap);
        }
        return genMap;
    }

}
