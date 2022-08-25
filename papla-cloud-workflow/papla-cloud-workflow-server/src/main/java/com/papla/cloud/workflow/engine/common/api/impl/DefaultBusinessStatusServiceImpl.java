package com.papla.cloud.workflow.engine.common.api.impl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;

import com.papla.cloud.workflow.engine.common.api.BusinessStatusService;
import com.papla.cloud.workflow.engine.common.util.JsonObjectUtil;
import com.papla.cloud.workflow.util.AppContext;

/**
 * @author linp
 * @ClassName: DefaultBusinessStatusServiceImpl
 * @Description: 工作流审批业务状态服务接口实现类
 * @date 2017年2月27日 上午9:57:44
 */
public class DefaultBusinessStatusServiceImpl implements BusinessStatusService {

    /* 按业务主键更新流程实例编码
     * (non-Javadoc)
     */
    @Override
    public String updateInsCode(String insCode, String insCol, String bizTabName, String bizPKCol, String bizId) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        Connection conn = null;
        Statement stmt = null;
        String sql = null;

        try {
            // 特殊字符替换
            if (bizTabName != null && !"".equals(bizTabName)) {
                bizTabName = this.replaceCRLF(bizTabName).trim();
            } else {
                throw new Exception("业务表名不能为空");
            }
            if (insCol != null && !"".equals(insCol)) {
                insCol = this.replaceCRLF(insCol).trim();
            } else {
                throw new Exception("流程实例编码列字段不能为空");
            }
            if (insCode != null && !"".equals(insCode)) {
                insCode = this.replaceCRLF(insCode).trim();
            } else {
                throw new Exception("流程实例编码不能为空");
            }
            if (bizPKCol != null && !"".equals(bizPKCol)) {
                bizPKCol = this.replaceCRLF(bizPKCol).trim();
            } else {
                throw new Exception("业务数据主键列不能为空且不能为联合主键");
            }
            if (bizId != null && !"".equals(bizId)) {
                bizId = this.replaceCRLF(bizId).trim();
            } else {
                throw new Exception("业务数据主键值不能为空且");
            }

            // 定义SQL语句
            StringBuffer str = new StringBuffer();
            str.append("update ").append(bizTabName).append(" set ");
            str.append(insCol).append(" = '").append(insCode).append("'");
            str.append(" where ").append(bizPKCol).append(" = '").append(bizId).append("'");

            sql = str.toString();

            // 获取数据连接
            JdbcTemplate jdbcTemplate = (JdbcTemplate) AppContext.getApplicationContext().getBean("jdbcTemplate");
            conn = jdbcTemplate.getDataSource().getConnection();

            // 执行SQL语句
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            // 提交事务
            if (!conn.getAutoCommit()) conn.commit();

            // 关闭连接
            if (stmt != null && !stmt.isClosed()) stmt.close();
            if (conn != null && !conn.isClosed()) conn.close();

            map.put("flag", "0");
            map.put("msg", "Success");

        } catch (Exception e) {
            e.printStackTrace();

            // 关闭连接
            if (stmt != null && !stmt.isClosed()) stmt.close();

            if (conn != null && !conn.isClosed()) {
                conn.rollback();
                conn.close();
            }
            map.put("flag", "0");
            map.put("msg", "" + e.getMessage() + "; SQL >> " + sql);
        }

        return JsonObjectUtil.objectToJSON(map);
    }

    /*
     * (非 Javadoc)
     * <p>Title: updateBizStatus</p>
     * <p>Description: 按业务主键更新业务状态信息 </p>
     * @param insCode			流程实例编码
     * @param insCol			流程实例编码储存列
     * @param bizTabName		业务表名
     * @param statusCol			业务状态码存储列
     * @param statusVal			业务状态码
     * @param stautsDescCol		业务状态描述存储列
     * @param stautsDescVal		业务状态码描述
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.BusinessStatusService#updateBizStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String updateBizStatus(String insCode, String insCol, String bizTabName, String bizPKCol, String bizId,
                                  String statusCol, String statusVal, String stautsDescCol, String stautsDescVal) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        Connection conn = null;
        Statement stmt = null;
        String sql = null;

        try {
            // 特殊字符替换
            if (bizTabName != null && !"".equals(bizTabName)) {
                bizTabName = this.replaceCRLF(bizTabName).trim();
            } else {
                throw new Exception("业务表名不能为空");
            }
            if (insCol != null && !"".equals(insCol)) {
                insCol = this.replaceCRLF(insCol).trim();
            } else {
                throw new Exception("流程实例编码列字段不能为空");
            }
            if (insCode != null && !"".equals(insCode)) {
                insCode = this.replaceCRLF(insCode).trim();
            } else {
                throw new Exception("流程实例编码不能为空");
            }
            if (statusCol != null && !"".equals(statusCol)) {
                statusCol = this.replaceCRLF(statusCol).trim();
            }
            if (statusVal != null && !"".equals(statusVal)) {
                statusVal = this.replaceCRLF(statusVal).trim();
            }
            if (stautsDescCol != null && !"".equals(stautsDescCol)) {
                stautsDescCol = this.replaceCRLF(stautsDescCol).trim();
            }
            if (stautsDescVal != null && !"".equals(stautsDescVal)) {
                stautsDescVal = this.replaceCRLF(stautsDescVal).trim();
            }
            if (bizPKCol != null && !"".equals(bizPKCol)) {
                bizPKCol = this.replaceCRLF(bizPKCol).trim();
            } else {
                throw new Exception("业务数据主键列不能为空且不能为联合主键");
            }
            if (bizId != null && !"".equals(bizId)) {
                bizId = this.replaceCRLF(bizId).trim();
            } else {
                throw new Exception("业务数据主键值不能为空且");
            }

            // 定义SQL语句
            StringBuffer str = new StringBuffer();
            str.append("update ").append(bizTabName).append(" set ");
            str.append(insCol).append(" = '").append(insCode).append("'");

            if (statusCol != null && !"".equals(stautsDescCol) && statusVal != null && !"".equals(statusVal)) {
                str.append(", ").append(statusCol).append(" = '").append(statusVal).append("'");
            }
            if (stautsDescCol != null && !"".equals(stautsDescCol) && stautsDescVal != null && !"".equals(stautsDescVal)) {
                str.append(", ").append(stautsDescCol).append(" = '").append(stautsDescVal).append("'");
            }
            str.append(" where ").append(bizPKCol).append(" = '").append(bizId).append("'");

            sql = str.toString();

            // 获取数据连接
            JdbcTemplate jdbcTemplate = (JdbcTemplate) AppContext.getApplicationContext().getBean("jdbcTemplate");
            conn = jdbcTemplate.getDataSource().getConnection();

            // 执行SQL语句
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            // 提交事务
            if (!conn.getAutoCommit()) conn.commit();

            // 关闭连接
            if (stmt != null && !stmt.isClosed()) stmt.close();
            if (conn != null && !conn.isClosed()) conn.close();

            map.put("flag", "0");
            map.put("msg", "Success");

        } catch (Exception e) {
            e.printStackTrace();

            // 关闭连接
            if (stmt != null && !stmt.isClosed()) stmt.close();

            if (conn != null && !conn.isClosed()) {
                conn.rollback();
                conn.close();
            }
            map.put("flag", "0");
            map.put("msg", "" + e.getMessage() + "; SQL >> " + sql);
        }

        return JsonObjectUtil.objectToJSON(map);
    }

    /*
     * (非 Javadoc)
     * <p>Title: updateBizStatusByCode</p>
     * <p>Description: 按流程实例编码更新业务状态信息 </p>
     * @param insCode			流程实例编码
     * @param insCol			流程实例编码储存列
     * @param bizTabName		业务表名
     * @param statusCol			业务状态码存储列
     * @param statusVal			业务状态码
     * @param stautsDescCol		业务状态描述存储列
     * @param stautsDescVal		业务状态码描述
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.BusinessStatusService#updateBizStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String updateBizStatusByCode(String insCode, String insCol, String bizTabName, String statusCol, String statusVal, String stautsDescCol, String stautsDescVal) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();

        Connection conn = null;
        Statement stmt = null;
        String sql = null;

        try {
            // 特殊字符替换
            if (bizTabName != null && !"".equals(bizTabName)) {
                bizTabName = this.replaceCRLF(bizTabName).trim();
            } else {
                throw new Exception("业务表名不能为空");
            }
            if (insCol != null && !"".equals(insCol)) {
                insCol = this.replaceCRLF(insCol).trim();
            } else {
                throw new Exception("流程实例编码列字段不能为空");
            }
            if (insCode != null && !"".equals(insCode)) {
                insCode = this.replaceCRLF(insCode).trim();
            } else {
                throw new Exception("流程实例编码不能为空");
            }
            if (statusCol != null && !"".equals(statusCol)) {
                statusCol = this.replaceCRLF(statusCol).trim();
            }
            if (statusVal != null && !"".equals(statusVal)) {
                statusVal = this.replaceCRLF(statusVal).trim();
            }
            if (stautsDescCol != null && !"".equals(stautsDescCol)) {
                stautsDescCol = this.replaceCRLF(stautsDescCol).trim();
            }
            if (stautsDescVal != null && !"".equals(stautsDescVal)) {
                stautsDescVal = this.replaceCRLF(stautsDescVal).trim();
            }

            // 定义SQL语句
            StringBuffer str = new StringBuffer();
            str.append("update ").append(bizTabName).append(" set ");
            str.append(insCol).append(" = '").append(insCode).append("'");

            if (statusCol != null && !"".equals(stautsDescCol) && statusVal != null && !"".equals(statusVal)) {
                str.append(", ").append(statusCol).append(" = '").append(statusVal).append("'");
            }
            if (stautsDescCol != null && !"".equals(stautsDescCol) && stautsDescVal != null && !"".equals(stautsDescVal)) {
                str.append(", ").append(stautsDescCol).append(" = '").append(stautsDescVal).append("'");
            }
            str.append(" where ").append(insCol).append(" = '").append(insCode).append("'");

            sql = str.toString();

            // 获取数据连接
            JdbcTemplate jdbcTemplate = (JdbcTemplate) AppContext.getApplicationContext().getBean("jdbcTemplate");
            conn = jdbcTemplate.getDataSource().getConnection();

            // 执行SQL语句
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            // 提交事务
            if (!conn.getAutoCommit()) conn.commit();

            // 关闭连接
            if (stmt != null && !stmt.isClosed()) stmt.close();
            if (conn != null && !conn.isClosed()) conn.close();

            map.put("flag", "0");
            map.put("msg", "Success");

        } catch (Exception e) {
            e.printStackTrace();

            // 关闭连接
            if (stmt != null && !stmt.isClosed()) stmt.close();

            if (conn != null && !conn.isClosed()) {
                conn.rollback();
                conn.close();
            }
            map.put("flag", "0");
            map.put("msg", "" + e.getMessage() + "; SQL >> " + sql);
        }

        return JsonObjectUtil.objectToJSON(map);
    }

    /**
     * 替换回车和换行符
     *
     * @param oldStr
     * @return
     * @throws Exception
     */
    private String replaceCRLF(String oldStr) throws Exception {
        String destStr = null;
        Pattern p = Pattern.compile("\r|\n");
        Matcher m = p.matcher(oldStr);
        destStr = m.replaceAll("");
        return destStr;
    }

}
