package com.papla.cloud.workflow.engine.dao.api.impl;

import com.papla.cloud.workflow.engine.dao.api.ExpressionServiceDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author linpeng
 * @ClassName: ExpressionServiceDAOImpl
 * @Description: 表达式处理DAO接口
 * @date 2015年4月23日 下午1:59:32
 */
@Repository("expressionServiceDAO")
public class ExpressionServiceDAOImpl implements ExpressionServiceDAO {
    @Resource
    private JdbcTemplate jdbcTemplate;

    /*
     * (非 Javadoc)
     * <p>Title: CheckSqlExpression</p>
     * <p>Description: 传入SQL，判断sql表达式是否正确，正确返回1  错误为0 </p>
     * @param sql
     * @return
     * @see com.papla.cloud.wf.dao.api.ExpressionServiceDAO#CheckSqlExpression(java.lang.String)
     */
    @Override
    public int CheckSqlExpression(String sql) {
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count;
    }

}
