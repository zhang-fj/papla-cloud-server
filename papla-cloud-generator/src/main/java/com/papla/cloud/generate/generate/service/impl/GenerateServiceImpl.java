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

        // 接口别名
        baseData.put("apiAlias", config.getApiAlias());
        // 包名称
        baseData.put("package", config.getPackageName());
        // 模块名称
        baseData.put("modulePath", config.getModulePath());
        // 作者
        baseData.put("author", "");
        // 创建日期
        baseData.put("date", LocalDate.now().toString());
        // 表名
        baseData.put("tableName", config.getTableName());

        // 实体类名
        baseData.put("ClassName", config.getClassName());
        baseData.put("className", config.getLowerClassName());
        baseData.put("hyphenClassName", config.getHyphenClassName());

        cfg.putBaseData(baseData);
        return cfg;
    }

    private Map<String, Object> getColumnsData(Map<String, Object> params) throws Exception {

        List<ModuleColumn> cols = moduleColumnMapper.findAll(params);

        Map<String, Object> genMap = new HashMap<String, Object>();

        // 字段列
        List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
        genMap.put("columns", columns);

        // 查询列
        List<Map<String, Object>> queryColumns = new ArrayList<Map<String, Object>>();
        genMap.put("queryColumns", queryColumns);

        // 必填列
        List<Map<String, Object>> notNullColumns = new ArrayList<Map<String, Object>>();
        genMap.put("notNullColumns", notNullColumns);

        // 字典列表
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

            // 数据库列名
            columnMap.put("columnName", columnName);
            columnMap.put("lowerColumnName", lowerColumnName);
            columnMap.put("upperColumnName", upperColumnName);

            // 数据列类型
            columnMap.put("columnType", columnType);
            columnMap.put("javaType", javaType);
            columnMap.put("jdbcType", jdbcType);

            // 数据库表名
            columnMap.put("tableName", column.getTableName());
            columnMap.put("columnKey", column.getKeyType());
            // 字段别名
            columnMap.put("aliasName", StringUtils.isNotBlank(column.getAliasName()) ? column.getAliasName() : column.getRemark());
            // 字段描述
            columnMap.put("remark", column.getRemark());
            // 是否必填
            columnMap.put("notNull", "YES".equals(column.getNotNull()));
            // 是否列表
            columnMap.put("listShow", "YES".equals(column.getListShow()));
            // 是否表单
            columnMap.put("formShow", "YES".equals(column.getFormShow()));
            // 表单类型
            columnMap.put("formType", column.getFormType());
            // 查询类型
            columnMap.put("queryType", StringUtils.isNotBlank(column.getQueryType()) ? column.getQueryType() : column.getFormType());
            // 查询方式
            columnMap.put("queryWay", column.getQueryWay());
            // 字典名称
            columnMap.put("dictName", column.getDictName());

            // 存在字典
            if (StringUtils.isNotBlank(column.getDictName())) {
                dicts.add(column.getDictName());
            }

            // 存在查询
            if (StringUtils.isNotBlank(column.getQueryWay())) {
                queryColumns.add(columnMap);
            }

            // 不为NULL列表
            if ("YES".equals(column.getNotNull())) {
                notNullColumns.add(columnMap);
            }

            if ("PRI".equals(column.getKeyType())) {
                //主键列名
                genMap.put("pkColumnName", columnName);
                genMap.put("pkUpperColumnName", upperColumnName);
                genMap.put("pkLowerColumnName", lowerColumnName);
                // 存储主键类型
                genMap.put("pkColumnType", javaType);
                genMap.put("columnType", columnType);
                genMap.put("pkJdbcType", jdbcType);
            }

            columns.add(columnMap);
        }
        return genMap;
    }

}
