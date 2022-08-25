package com.papla.cloud.workflow.engine.dao.api;

import java.util.List;

import com.papla.cloud.workflow.engine.modal.ConditionGroupBean;

/**
 * @author linpeng
 * @ClassName: ExpressionServiceDAO
 * @Description: 表达式处理DAO接口
 * @date 2015年4月23日 下午1:59:05
 */
public interface ExpressionServiceDAO {

    /**
     * @param sql
     * @return int    返回类型
     * @Title: CheckSqlExpression
     * @Description: 传入SQL，判断sql表达式是否正确，正确返回1  错误为0
     */
     int CheckSqlExpression(String sql);
}
