package com.papla.cloud.workflow.engine.common.api;

import java.util.List;

import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;


/**
 * @author linpeng
 * @ClassName: ProcessEngineCommonService
 * @Description: 流程引擎通用服务接口
 * @date 2015年4月15日 下午4:33:36
 */
public interface ProcessEngineCommonService {


    void invokeFunc(ProcessInstanceBean bean, String funcType, String funcName) throws Exception;

    /**
     * @param insBean 实例编码
     * @param str     字符串
     * @return String    返回类型
     * @throws Exception
     * @Title: invokeWebService
     * @Description: 调用WebService
     */
     String invokeWebService(ProcessInstanceBean insBean, String str) throws Exception;

    /**
     * @param insBean
     * @param str
     * @return String    返回类型
     * @throws Exception
     * @Title: callOracleProcedure
     * @Description: 调用Oracle数据库存储过程，约定输入和输出参数个数
     */
     String callOracleProcedure(ProcessInstanceBean insBean, String str) throws Exception;

    /**
     * 执行Java
     *
     * @param insBean
     * @param str
     * @return
     * @throws Exception
     */
     String invokeJavaMethod(ProcessInstanceBean insBean, String str) throws Exception;

    /**
     * @param exp
     * @param al
     * @param activityBean
     * @return String    返回类型
     * @Title: replaceVariable
     * @Description: 传入一个带有表达式的字符串，和实体属性集合，返回将表达式替换为具体值后的字符串
     */
     String replaceVariable(String exp, List<InstanceEntityAttrBean> al, ActivityBean activityBean) throws Exception;

    /**
     * @return String    返回类型
     * @throws Exception
     * @Title: createProcessInstanceCode
     * @Description: 维护流程实例编码
     * 编码规则为四位年两位月两位日_序列，序列由系统编码表维护。
     * 例如 ：20140702_123456
     */
     String createProcessInstanceCode() throws Exception;

    /**
     * @param attrCode            实例实体属性编码
     * @param processInstanceBean 实例Bean
     * @return String    返回类型
     * @throws Exception
     * @Title: getInsEntityAttrValue
     * @Description: 获取流程实例-实体属性值
     */
     String getInsEntityAttrValue(String attrCode, ProcessInstanceBean processInstanceBean) throws Exception;

    /**
     * @param actId
     * @param processInstanceBean
     * @return boolean    返回类型
     * @throws Exception
     * @Title: isLastApprover
     * @Description: 判断是否为最后一个审批人
     */
     boolean isLastApprover(String actId, ProcessInstanceBean processInstanceBean) throws Exception;

    /**
     * @param actBean
     * @param insId
     * @param passOrRefuseFlag
     * @return boolean    返回类型
     * @throws Exception
     * @Title: isUpToExpectedPercent
     * @Description: 判断是否达到通过/驳回比例
     */
     boolean isUpToExpectedPercent(ActivityBean actBean, String insId, String passOrRefuseFlag) throws Exception;

    /**
     * @param processId
     * @return
     * @throws Exception 设定文件
     * @Title: isDisableAvoidStrategy
     * @Description: 判断是否自动失效规避策略。
     * <p>
     * 满足以下情形则自动失效规避策:
     * 1)、如果流程图中存在环路则流程引擎将自动失效规避策略
     * 2)、...
     * </p>
     */
     boolean isDisableAvoidStrategy(String processId) throws Exception;

    /**
     * @param bean
     * @throws Exception 设定文件
     * @Title: createDynaActivityQueue
     * @Description: 生成动态通知节点的审批队列信息
     */
     void createDynaActivityQueue(ProcessInstanceBean bean) throws Exception;

    /**
     * @param activityBean
     * @return 设定文件
     * @Title: isDynamicNode
     * @Description: 判断当前节点是否为动态节点
     */
     String isDynamicNode(ActivityBean activityBean) throws Exception;

}
