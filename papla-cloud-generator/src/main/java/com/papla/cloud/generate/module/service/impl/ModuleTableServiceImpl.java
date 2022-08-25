package com.papla.cloud.generate.module.service.impl;

import com.papla.cloud.common.mybatis.mapper.BaseMapper;
import com.papla.cloud.common.mybatis.service.impl.BaseServiceImpl;
import com.papla.cloud.common.web.utils.FileUtil;
import com.papla.cloud.generate.code.utils.JdbcTemplateUtils;
import com.papla.cloud.generate.module.domain.ModuleProject;
import com.papla.cloud.generate.module.domain.ModuleTable;
import com.papla.cloud.generate.module.domain.Table;
import com.papla.cloud.generate.module.mapper.ModuleProjectMapper;
import com.papla.cloud.generate.module.mapper.ModuleTableMapper;
import com.papla.cloud.generate.module.service.ModuleTableService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
 * @Title: ModuleTableServiceImpl
 * @Description: TODO   关联表管理
 * @date 2021-09-03
 */
@Service
@RequiredArgsConstructor
public class ModuleTableServiceImpl extends BaseServiceImpl<ModuleTable> implements ModuleTableService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Logger getLogger(){
        return logger;
    }

    private final ModuleTableMapper mapper;
    private final ModuleProjectMapper projectMapper;

    @Override
    public BaseMapper<ModuleTable> getMapper(){
        return mapper;
    }

    @Override
    public List<Table> tables(String projectId) {

        ModuleProject moduleProject = projectMapper.selectByPK(projectId);

        JdbcTemplate jdbcTemplate = JdbcTemplateUtils.getJdbcTemplate(
                moduleProject.getDbType(),
                moduleProject.getDbUrl(),
                moduleProject.getDbUser(),
                moduleProject.getDbPass());

        // 默认查询MYSQL
        String sql =
                "SELECT\n" +
                        "   table_schema tableSchema,\n" +
                        "   table_name tableName,\n" +
                        "   create_time createDt,\n" +
                        "   engine enging,\n" +
                        "   table_collation coding,\n" +
                        "   table_comment remark\n" +
                        "FROM information_schema.TABLES\n" +
                        "WHERE table_schema = (SELECT DATABASE())";

        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Table.class));
    }

    @Override
    public void download(List<ModuleTable> entitys, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ModuleTable moduleTable : entitys) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("项目名称", moduleTable.getProjectId());
            map.put("模块ID", moduleTable.getModuleId());
            map.put("关联表名", moduleTable.getTableName());
            map.put("实体名称", moduleTable.getEntityName());
            map.put("创建日期", moduleTable.getCreateDt());
            map.put("创建人", moduleTable.getCreateBy());
            map.put("最后更新日期", moduleTable.getUpdateDt());
            map.put("最后更新人", moduleTable.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
