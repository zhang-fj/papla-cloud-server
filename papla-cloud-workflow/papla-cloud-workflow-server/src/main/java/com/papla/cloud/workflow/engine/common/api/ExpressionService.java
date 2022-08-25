package com.papla.cloud.workflow.engine.common.api;

import com.papla.cloud.workflow.engine.modal.ConditionGroupBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;

import java.util.List;


/**
 * @author linpeng
 * @ClassName: ExpressionService
 * @Description: 表达式处理服务接口
 * @date 2015年4月27日 上午10:53:29
 */
public interface ExpressionService {


    /**
     * @param parentType   表达式分类：流程级-P，节点级-A
     * @param parentId     表达式分类ID：如果为流程级时，则为流程ID；否则为节点ID
     * @param propertyCode 条件绑定的流程或节点属性编码
     * @param expStr       表达式串，必输入参数
     * @return List<ConditionGroupBean>    返回类型
     * @Title: paraseExpressionToDB
     * @Description: 解析流程设计器中涉及的路由表达式串; 包括路由表单、路由节点执行人和绑定条件。
     */
     List<ConditionGroupBean> paraseExpressionToDB(String parentType, String parentId, String propertyCode, String expStr);

    /**
     * @param cgb                 条件组
     * @param processInstanceBeans
     * @return boolean    返回类型
     * @throws Exception
     * @Title: judgeByBoundExpression
     * @Description: 判断条件表达式值的真假
     */
     boolean judgeByBoundExpression(ConditionGroupBean cgb, ProcessInstanceBean processInstanceBeans) throws Exception;

    /**
     * @param parentType
     * @param parentId
     * @param propertyCode
     * @param processInstanceBean 流程实例Bean
     * @return List    返回类型
     * @throws Exception
     * @Title: getRouteValue
     * @Description: 获取路由条件值
     */
     List<String> getRouteValue(String parentType, String parentId, String propertyCode, ProcessInstanceBean processInstanceBean) throws Exception;
}
