package com.papla.cloud.generate.module.service.impl;

import com.papla.cloud.common.mybatis.exception.MessageRuntimeException;
import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.common.web.utils.StringUtils;
import com.papla.cloud.generate.code.utils.JdbcTemplateUtils;
import com.papla.cloud.generate.module.domain.Column;
import com.papla.cloud.generate.module.domain.ModuleColumn;
import com.papla.cloud.generate.module.domain.ModuleProject;
import com.papla.cloud.generate.module.domain.ModuleTable;
import com.papla.cloud.generate.module.mapper.ModuleColumnMapper;
import com.papla.cloud.generate.module.mapper.ModuleProjectMapper;
import com.papla.cloud.generate.module.mapper.ModuleTableMapper;
import com.papla.cloud.generate.module.service.ModuleColumnService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zhangfj·
 * @version V1.0
 * @Title: ModuleColumnServiceImpl
 * @Description: TODO   字段配置管理
 * @date 2021-09-03
 */
@Service
@RequiredArgsConstructor
public class ModuleColumnServiceImpl extends BaseServiceImpl<ModuleColumn> implements ModuleColumnService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Logger getLogger(){
        return logger;
    }

    private final ModuleColumnMapper mapper;

    private final ModuleTableMapper tableMapper;

    private final ModuleProjectMapper projectMapper;

    @Override
    public BaseMapper<ModuleColumn> getMapper(){
        return mapper;
    }

    /**
     * @param column
     * @param col
     * @return ModuleColumn
     * @Description TODO 同步列属性
     */
    private ModuleColumn copyColumnProperty(ModuleColumn column, Column col) {

        column.setColumnType(col.getColumnType());
        column.setExtra(col.getExtra());
        column.setKeyType(col.getKeyType());

        if (StringUtils.isAllBlank(column.getColumnName())) {
            column.setColumnName(col.getColumnName());
        }
        if (StringUtils.isAllBlank(column.getNotNull())) {
            column.setNotNull(col.getNotNull());
        }
        if (StringUtils.isAllBlank(column.getRemark())) {
            column.setRemark(col.getRemark());
        }
        if (StringUtils.isAllBlank(column.getAliasName())) {
            column.setAliasName(col.getRemark());
        }
        if (column.getSort() == null) {
            column.setSort(col.getSort());
        }

        return column;
    }

    @Override
    public List<ModuleColumn> columns(String tableId) throws Exception {

        if (StringUtils.isAllBlank(tableId)) throw new MessageRuntimeException("关联表ID不能为空");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableId", tableId);

        // 查询【已配置列】
        List<ModuleColumn> columns = findAll(param);

        // 如果未查询到配置列，则获取【数据库表】所对应的【数据库列】
        if (columns == null || columns.size() == 0) {
            // 先同步
            synch(tableId);
        }

        return mapper.findAll(param);
    }

    @Override
    public List<Column> columns(String projectId,String tableName) {

        ModuleProject moduleProject = projectMapper.selectByPK(projectId);

        // 获取JdbcTemplate
        JdbcTemplate jdbcTemplate = JdbcTemplateUtils.getJdbcTemplate(
                moduleProject.getDbType(),
                moduleProject.getDbUrl(),
                moduleProject.getDbUser(),
                moduleProject.getDbPass());

        // 查询指定表所有列
        String sql=
                "SELECT\n" +
                        "   column_name columnName,\n" +
                        "   data_type columnType,\n" +
                        "   case when is_nullable = 'YES' then 'NO' else 'YES' end notNull,\n" +
                        "   column_comment remark,\n" +
                        "   column_key keyType,\n" +
                        "   ordinal_position sort,\n" +
                        "   extra\n" +
                        "FROM information_schema.COLUMNS\n" +
                        "WHERE table_schema = (SELECT DATABASE()) AND TABLE_NAME = ? \n" +
                        "ORDER BY ordinal_position";

        return jdbcTemplate.query(sql,new Object[]{tableName},new BeanPropertyRowMapper<>(Column.class));
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void synch(String tableId) throws Exception {

        if (StringUtils.isAllBlank(tableId)) throw new MessageRuntimeException("关联表ID不能为空");

        ModuleTable table = tableMapper.selectByPK(tableId);

        String tableName = table.getTableName();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tableId", tableId);
        param.put("tableName", tableName);

        // 使用map缓存【数据库列】数据作为同步依据key =》 columnName，value =》 column
        Map<String, Column> dbColumnMapping = new HashMap<String, Column>();

        // 查询【已配置列】
        List<ModuleColumn> columns = findAll(param);
        // 查询【数据库列】
        List<Column> cols = columns(table.getProjectId(),tableName);

        if (cols != null) {
            // 缓存【数据库列】
            for (Column col : cols) {
                dbColumnMapping.put(col.getColumnName().trim(), col);
            }

            if (columns != null) {
                // 第一种情况，数据库列改变或者删除
                for (int i = columns.size() - 1; i >= 0; i--) {
                    ModuleColumn column = columns.get(i);
                    // 在缓存中查找【数据库列】
                    Column col = dbColumnMapping.get(column.getColumnName());
                    if (col == null) {
                        // 如果没有找到对应的【数据库列】，则从数据库中删除当前【配置列】
                        delete(column);
                    } else {
                        update(copyColumnProperty(column, col));
                        // 从缓存映射中异常当前【数据库列】
                        dbColumnMapping.remove(column.getColumnName());
                    }
                }

            }

            // 第二种情况，数据库列新增
            for (Column col : dbColumnMapping.values()) {
                ModuleColumn column = copyColumnProperty(new ModuleColumn(), col);
                column.setTableId(tableId);
                column.setTableName(tableName);
                insert(column);
            }

        }
    }


    @Override
    public void download(List<ModuleColumn> entitys, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ModuleColumn moduleColumn : entitys) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("项目ID", moduleColumn.getProjectId());
            map.put("模块ID", moduleColumn.getModuleId());
            map.put("关联表ID", moduleColumn.getTableId());
            map.put("数据库表名", moduleColumn.getTableName());
            map.put("数据库列名", moduleColumn.getColumnName());
            map.put("字段名称", moduleColumn.getAliasName());
            map.put("数据库列类型", moduleColumn.getColumnType());
            map.put("数据库键类型", moduleColumn.getKeyType());
            map.put("数据库列额外参数", moduleColumn.getExtra());
            map.put("数据库列描述", moduleColumn.getRemark());
            map.put("是否必填", moduleColumn.getNotNull());
            map.put("是否在列表显示", moduleColumn.getListShow());
            map.put("是否表单显示", moduleColumn.getFormShow());
            map.put("表单类型", moduleColumn.getFormType());
            map.put("查询类型", moduleColumn.getQueryType());
            map.put("查询方式", moduleColumn.getQueryWay());
            map.put("排序", moduleColumn.getSort());
            map.put("创建日期", moduleColumn.getCreateDt());
            map.put("创建人", moduleColumn.getCreateBy());
            map.put("最后更新日期", moduleColumn.getUpdateDt());
            map.put("最后更新人", moduleColumn.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
