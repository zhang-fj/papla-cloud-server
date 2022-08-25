package com.papla.cloud.workflow.engine.common.api.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.DirectedGraphService;
import com.papla.cloud.workflow.engine.common.api.ExpressionService;
import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.JsonObjectUtil;
import com.papla.cloud.workflow.engine.common.util.WebserviceUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.deploy.dao.ProcessDeployDAO;
import com.papla.cloud.workflow.engine.mapper.ApplicationMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessEngineCommonServiceMapper;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ApplicationBean;
import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;
import com.papla.cloud.workflow.engine.modal.WorkItemBean;
import com.papla.cloud.workflow.engine.run.dao.ActivityDAO;
import com.papla.cloud.workflow.engine.run.engine.core.common.EngineCommonHandler;
//import com.papla.cloud.workflow.platform.modal.XipPubAppBean;
import com.papla.cloud.workflow.util.DateUtil;
import com.papla.cloud.workflow.util.XipUtil;

/**
 * @author linpeng
 * @ClassName: ProcessEngineCommonServiceImpl
 * @Description: 流程引擎通用服务接口实现类
 * @date 2015年4月27日 上午10:38:00
 */
@Service("processEngineCommonService")
public class ProcessEngineCommonServiceImpl implements ProcessEngineCommonService {

    @Resource
    EngineCommonHandler engineCommonHandler;
    @Resource
    ProcessEngineCommonServiceMapper processEngineCommonServiceMapper;
    @Resource
    ApplicationMapper applicationMapper;
    @Autowired
    private ProcessDeployDAO processDeployDAO;
    @Autowired
    private DirectedGraphService directedGraphService;
    @Autowired
    private ActivityDAO activityDAO;


    @Override
    public void invokeFunc(ProcessInstanceBean bean, String funcType, String funcName) throws Exception {
        String reslut = null;
        if (WorkFlowConstants.FUNC_TYPE_PROC.equals(funcType)) { // 存储过程
            reslut = callOracleProcedure(bean, funcName);
        } else if (WorkFlowConstants.FUNC_TYPE_WEB.equals(funcType)) {    // WebService
            reslut = invokeWebService(bean, funcName);
        } else if (WorkFlowConstants.FUNC_TYPE_JAVA.equals(funcType)) { // Java方法
            reslut = invokeJavaMethod(bean, funcName);
        }
        // 处理返回信息
        if (reslut != null) {
            Map<String, Object> result = JsonObjectUtil.JSONToObj(reslut, Map.class);
            // 标志位 : 0-正确,1-失败
            String flag = (String) result.get("flag");
            if ("1".equals(flag)) {
                throw new Exception((String) result.get("msg"));
            }
        }
    }

    @Override
    public String invokeWebService(ProcessInstanceBean insBean, String str) throws Exception {
        List<InstanceEntityAttrBean> arrtList = insBean.getAttrList();
        ActivityBean actBean = (insBean.getCurrentActivityHandler() == null) ? null : (insBean.getCurrentActivityHandler().getActivityBean());
        String s = replaceVariable(str, arrtList, actBean);
        String lowerStr = s.toLowerCase();
        String url = s.substring(0, lowerStr.indexOf("?wsdl") + 5);
        String methodName = s.substring(lowerStr.indexOf("?wsdl") + 6, s.indexOf("("));
        String paramsStr = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
        String[] paramArray = paramsStr.split(",");
        //调用WebService
        String resultJson = new WebserviceUtil().callWebService(url, methodName, paramArray);
        return resultJson;
    }

    ;

    @Override
    public String callOracleProcedure(ProcessInstanceBean insBean, String str) throws Exception {
        List<InstanceEntityAttrBean> arrtList = insBean.getAttrList();
        ActivityBean actBean = (insBean.getCurrentActivityHandler() == null) ? null : (insBean.getCurrentActivityHandler().getActivityBean());
        String s = replaceVariable(str, arrtList, actBean);
        String procedureName = s.substring(0, s.indexOf("("));
        String paramsStr = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
        String[] paramArray = new String[]{};
        if (!"".equals(paramsStr)) {
            paramArray = paramsStr.split(",");
        }
        ArrayList<String> paramList = new ArrayList<String>();
        for (int i = 0; i < paramArray.length; i++) {
            paramList.add(paramArray[i]);
        }

        ApplicationBean appBean = insBean.getAppBean();
        if (appBean == null) appBean = applicationMapper.getApplicationByProcId(insBean.getProcessId());

        Connection ct = null;
        String resultJson = "";
        try {

        } catch (Exception e) {
            e.printStackTrace();
            resultJson = "{\"flag\":\"0\",\"msg\":\"" + e.getMessage() + "; 过程名为:【" + procedureName + "】,参数串为:【" + paramList + "\"}";
        } finally {
            try {
                if (ct != null && !ct.isClosed()) {
                    ct.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultJson;
    }

    ;

    /*
     * (非 Javadoc)
     * <p>Title: invokeWebService</p>
     * <p>Description: 执行Java </p>
     * @param insBean
     * @param str
     * @return 方返回的结果是一个xml字符串 <result><flag></flag><msg></msg><data><row></row><row></row></data></result>
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ProcessEngineCommonService#invokeJavaMethod(com.papla.cloud.wf.modal.ProcessInstanceBean, java.lang.String)
     */
    @Override
    public String invokeJavaMethod(ProcessInstanceBean insBean, String str) throws Exception {
        // 定义返回值
        String resultJson = "";
        try {
            // 获取参数信息
            List<InstanceEntityAttrBean> arrtList = insBean.getAttrList();
            ActivityBean actBean = (insBean.getCurrentActivityHandler() == null) ? null : (insBean.getCurrentActivityHandler().getActivityBean());
            // 替换变量参数
            String s = replaceVariable(str, arrtList, actBean);
            // 获取全类名、方法名和参数串
            String clsStr = s.substring(0, s.indexOf("("));
            String paramStr = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
            // 获取全类名和方法名
            String clsName = clsStr.substring(0, clsStr.lastIndexOf("."));
            String methodName = clsStr.substring(clsStr.lastIndexOf(".") + 1, clsStr.length());
            // 利用反射调用
            Class<?> cls = Class.forName(clsName);
            // 处理参数
            Class<?>[] argsType = null;
            Object[] args = null;
            if (paramStr != null && !"".equals(paramStr)) {
                Object[] paramArray = paramStr.split(",");
                int len = paramArray.length;
                if (len > 0) {
                    argsType = new Class[len];
                    args = new Object[len];
                    for (int i = 0; i < len; i++) {
                        argsType[i] = String.class; // 只处理字符串
                        args[i] = paramArray[i];
                    }
                }
            }
            // 获取方法
            Method m = cls.getMethod(methodName, argsType);

            // 执行调用
            Object xmlStr = m.invoke(cls.newInstance(), args);

            // 处理返回值
            if (xmlStr == null || "".equals(xmlStr)) {
                throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0025", null));

            } else {
                resultJson = String.valueOf(xmlStr);
            }

        } catch (Exception e) {
            if ("".equals(e.getMessage()) || e.getMessage() == null) {
                resultJson = "{\"flag\":\"500\",\"msg\":\"" + e.getCause().getMessage() + "\"}";
            } else {
                resultJson = "{\"flag\":\"0\",\"msg\":\"" + e.getMessage() + "\"}";
            }
        }

        return resultJson;
    }

    public String replaceVariable(String exp, List<InstanceEntityAttrBean> al, ActivityBean activityBean) throws Exception {
        String leftSymbol = "${";
        String rightSymbol = "}";
        int leftIndex;
        int rightIndex;

        leftIndex = exp.indexOf(leftSymbol);
        rightIndex = exp.lastIndexOf(rightSymbol);

        // 判断是否结束
        if (leftIndex == -1 || rightIndex == -1 || leftIndex > rightIndex) {
            return exp;
        }

        // 第一个${左侧的字符串
        String ls = exp.substring(0, exp.indexOf(leftSymbol));

        // 第一个${右侧的字符串
        String rs = exp.substring(exp.indexOf(leftSymbol) + 2);

        // 第一个}的位置
        rightIndex = rs.indexOf(rightSymbol);

        // 表达式
        String vas = rs.substring(0, rightIndex);

        if (vas != null) {
            if (vas.equals(WorkFlowConstants.E_CURRENT_APPROVER)) {    // 当前登录用户ID
                vas = CurrentUserUtil.getCurrentUserId();
            } else if (vas.equals(WorkFlowConstants.E_CURRENT_NODE)) {    // 当前流程节点编码
                vas = (activityBean == null ? vas : activityBean.getCode());
            } else if (vas.equals(WorkFlowConstants.E_CURRENT_SYSDATE)) {    // 当前系统时间(格式为：yyyy-MM-dd HH:mm:ss)
                vas = DateUtil.formatDate2String(new Date());
            } else {
                for (InstanceEntityAttrBean bean : al) {
                    if (vas.equals(bean.getAttrCode())) {
                        if (bean.getAttrCategory().equals(WorkFlowConstants.ATTR_CATEGORY_STATIC)) { // 静态属性
                            // 替换表达式的值
                            vas = bean.getAttrResult() == null ? vas : bean.getAttrResult();
                        } else if (bean.getAttrCategory().equals(WorkFlowConstants.ATTR_CATEGORY_DYNAMIC)) { // 动态属性
                            String funcType = bean.getFuncType();        // 函数类型: PROC-存储过程,WEB-应用服务
                            String funcName = bean.getFuncValue();        // 函数名称

                            if (funcType != null && !"".equals(funcType) && funcName != null && !"".equals(funcName)) {
                                try {
                                    // 设置参数
                                    ProcessInstanceBean insBean = null;
                                    if (activityBean != null) {
                                        insBean = engineCommonHandler.setCurrentActivityHandler(activityBean);
                                    }
                                    insBean.setAttrList(al);
                                    // 计算返回参数
                                    String xmlStr = null;
                                    if (funcType.equals(WorkFlowConstants.FUNC_TYPE_PROC)) { // 调用存储过程
                                        if (activityBean != null) {
                                            insBean.setAppBean(applicationMapper.getApplicationByProcId(activityBean.getProcessId()));
                                            xmlStr = this.callOracleProcedure(insBean, funcName);
                                        }
                                    } else if (funcType.equals(WorkFlowConstants.FUNC_TYPE_WEB)) { // 调用WebService
                                        xmlStr = this.invokeWebService(insBean, funcName);

                                    } else if (funcType.equals(WorkFlowConstants.FUNC_TYPE_JAVA)) { // 调用Java方法
                                        xmlStr = this.invokeJavaMethod(insBean, funcName);

                                    } else {
                                        // 扩展使用
                                    }

                                    // 解析返回参数
                                    if (xmlStr != null) {
                                        Document document = DocumentHelper.parseText(xmlStr);
                                        Element root = document.getRootElement();

                                        Element flagE = root.element("flag");
                                        //Element msgE = root.element("msg");
                                        Element dataE = root.element("data");

                                        String flagValue = flagE.getTextTrim();
                                        //String msgValue = msgE.getTextTrim();
                                        String dataValue = dataE.getTextTrim();

                                        if ("0".equals(flagValue)) {
                                            vas = dataValue;

                                        } else if ("500".equals(flagValue)) { // 查找节点执行出错时返回标志
                                            String msgValue = root.element("msg").getTextTrim();
                                            throw new Exception(msgValue);

                                        } else {

                                        }
                                    }

                                } catch (Exception e1) {
                                    throw e1;
                                }
                            }
                        } else {
                            // 扩展使用
                        }
                        break;
                    }
                }
            }
        }
        // 第一个}右边的的字符串
        String rightS = rs.substring(rightIndex + 1);

        return replaceVariable(ls + vas + rightS, al, activityBean);
    }

    /*
     * (非 Javadoc)
     * <p>Title: createProcessInstanceCode</p>
     * <p>Description: 维护流程实例编码; 编码规则为四位年两位月两位日_序列，序列由系统编码表维护 ; 例如 ：20140702_123456。 </p>
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ProcessEngineCommonService#createProcessInstanceCode()
     */
    @Override
    public String createProcessInstanceCode() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
        // 暂时以uuid为后半段编码
        return df.format(new Date()) + "_" + UUID.randomUUID();// new
        // Date()为获取当前系统时间
    }

    ;

    /*
     * (非 Javadoc)
     * <p>Title: getInsEntityAttrValue</p>
     * <p>Description: 获取流程实例-实体属性值</p>
     * @param attrCode
     * @param processInstanceBean
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ProcessEngineCommonService#getInsEntityAttrValue(java.lang.String, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @Override
    public String getInsEntityAttrValue(String attrCode, ProcessInstanceBean processInstanceBean) throws Exception {

        List<InstanceEntityAttrBean> al = processInstanceBean.getAttrList();
        InstanceEntityAttrBean currentBean = null;
        for (InstanceEntityAttrBean bean : al) {
            if (attrCode.toUpperCase().equals(bean.getAttrCode().toUpperCase())) {
                currentBean = bean;
                break;
            }
        }
        String value = null;
        String type = currentBean.getAttrCategory();
        if ("static".equals(type)) {
            value = currentBean.getAttrResult();
        } else if ("dynamic".equals(type)) {
            String funcType = currentBean.getFuncType();
            // 设定动态函数类型：P-存储过程，S-Web服务
            String xml = null;
            if (WorkFlowConstants.FUNC_TYPE_PROC.equals(funcType)) {
                xml = callOracleProcedure(processInstanceBean, currentBean.getFuncValue());

            } else if (WorkFlowConstants.FUNC_TYPE_WEB.equals(funcType)) {
                xml = invokeWebService(processInstanceBean, currentBean.getFuncValue());

            } else if (WorkFlowConstants.FUNC_TYPE_JAVA.equals(funcType)) {
                xml = invokeJavaMethod(processInstanceBean, currentBean.getFuncValue());
            }
            try {
                Document document = DocumentHelper.parseText(xml);
                Element rootElt = document.getRootElement(); // 获取根节点
                Element flagE = rootElt.element("flag");
                Element msgE = rootElt.element("msg");
                Element dataE = rootElt.element("data");
                String flagValue = flagE.getTextTrim();
                String msgValue = msgE.getTextTrim();
                String dataValue = dataE.getTextTrim();
                if ("0".equals(flagValue)) {
                    value = dataValue;
                } else {
                    throw new Exception(msgValue);
                }
            } catch (Exception e) {
                throw e;
            }
        }
        return value;
    }

    ;

    /*
     * (非 Javadoc)
     * <p>Title: isLastApprover</p>
     * <p>Description: 判断是否为最后一个审批人 </p>
     * @param actId
     * @param processInstanceBean
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ProcessEngineCommonService#isLastApprover(java.lang.String, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @Override
    public synchronized boolean isLastApprover(String actId, ProcessInstanceBean processInstanceBean) throws Exception {
        boolean flag = false;
        try {
            // 查询参数
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("instanceId", processInstanceBean.getInstanceId());
            map.put("currentTaskId", processInstanceBean.getCurrentTaskId());
            map.put("actId", actId);
            map.put("taskState", WorkFlowConstants.TASK_STATE_OPEN);

            int total = processEngineCommonServiceMapper.isLastApprover(map);

            if (total == 0) flag = true;

        } catch (Exception e) {
            throw e;
        }
        return flag;
    }

    /*
     * (非 Javadoc)
     * <p>Title: isUpToExpectedPercent</p>
     * <p>Description: 判断是否达到通过/驳回比例 </p>
     * @param actBean
     * @param insId
     * @param passOrRefuseFlag
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ProcessEngineCommonService#isUpToSettingPercent(com.papla.cloud.wf.modal.ActivityBean, com.papla.cloud.wf.modal.ProcessInstanceBean, java.lang.String)
     */
    @Override
    public synchronized boolean isUpToExpectedPercent(ActivityBean actBean, String insId, String passOrRefuseFlag) throws Exception {
        boolean flag = false;
        try {
            // 查询实例下节点的待办信息
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("insId", insId);
            map.put("actId", actBean.getId());

            // 执行查询
            List<WorkItemBean> beans = processEngineCommonServiceMapper.getTasks(map);

            // 计算比例
            if (beans != null && beans.size() > 0) {
                // 总待办数
                int total = beans.size();
                // 关闭待办数(须计入当前待办)
                int completed = 1;

                if ("Y".equals(passOrRefuseFlag)) {
                    // 计算通过比例
                    for (WorkItemBean bean : beans) {
                        String taskState = bean.getTaskState();
                        String closedLineType = bean.getClosedLineType();

                        if (WorkFlowConstants.TASK_STATE_CLOSED.equals(taskState) && "Y".equals(closedLineType)) {
                            completed += 1;
                        }
                    }
                } else {
                    // 计算驳回比例
                    for (WorkItemBean bean : beans) {
                        String taskState = bean.getTaskState();
                        String closedLineType = bean.getClosedLineType();
                        if (WorkFlowConstants.TASK_STATE_CLOSED.equals(taskState) && "N".equals(closedLineType)) {
                            completed += 1;
                        }
                    }
                }

                // 计算通过或驳回实际完成比例
                BigDecimal completedNumberic = new BigDecimal(completed);    // 完成的待办数
                BigDecimal totalNumberic = new BigDecimal(total);    // 总待办数

                MathContext mc = new MathContext(4, RoundingMode.HALF_DOWN);    // 设置精度
                BigDecimal bd = completedNumberic.divide(totalNumberic, mc).multiply(new BigDecimal(100));
                float actualPercent = bd.floatValue();

                // 计算会签规则的配置比例
                Map<String, String> propertyMap = actBean.getPropertyMap();
                String percent = (String) propertyMap.get(WorkFlowConstants.A_COUNTERSIGN_PERCENT);
                float expectedPercent = Float.parseFloat(percent);

                // 是否达到期望比例
                if (actualPercent >= expectedPercent) {
                    flag = true;
                }

            } else {
                throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0026", null));
            }

        } catch (Exception e) {
            throw e;
        }
        return flag;
    }

    /*
     * (非 Javadoc)
     * <p>Title: isDisableAvoidStrategy</p>
     * <p>Description: 判断是否自动失效规避策略 </p>
     * <p>
     * 	满足以下情形则自动失效规避策:
     * 	 1)、如果流程图中存在环路则流程引擎将自动失效规避策略
     * 	 2)、...
     * </p>
     * @param processId
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ProcessEngineCommonService#isDisableAvoidStrategy(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean isDisableAvoidStrategy(String deployId) throws Exception {
        boolean flag = false;
        // 查询流程图的节点和连线信息
        Map<String, Object> map = processDeployDAO.getProcessActsAndTranitionsById(deployId);
        if (!map.isEmpty()) {
			/*-----------------------------------------------------------------------------------------
			 * 1.算法要求：判断流程图是否存在环路:
			 * 2.解决方案：对流程图执行拓扑排序处理
			 * 3.方案详解：
			 *    由AOV网构造拓扑序列的拓扑排序算法主要是循环执行以下两步，直到不存在入度为0的顶点为止。
			 * 		 (1) 选择一个入度为0的顶点并输出之；
			 * 		 (2) 从网中删除此顶点及所有出边。
			 * 	    循环结束后，若输出的顶点数小于网中的顶点数，则存在回路信息，否则不存在回路且输出的顶点序列就是一种拓扑序列。
			 * 4.AOV网：即顶点表示活动、边表示活动间先后关系的有向图称做顶点活动网(Activity On Vertex network).
			 -----------------------------------------------------------------------------------------*/

            // 判断节点和连线的合法性
            List<ActivityBean> acts = (List<ActivityBean>) map.get("acts");
            List<TransitionBean> trans = (List<TransitionBean>) map.get("trans");

            if (acts == null) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0023", null));
            if (acts != null && acts.size() == 0) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0023", null));

            if (trans == null) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0024", null));
            if (trans != null && trans.size() == 0) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0024", null));

            // 排序前顶点数
            List<Integer> vertexs = new ArrayList<Integer>();
            for (ActivityBean act : acts) {
                vertexs.add(act.getNum());
            }
            // 连线关系
            List<int[]> edges = new ArrayList<int[]>();
            for (TransitionBean transBean : trans) {
                int fromActId = transBean.getFromActId(); // 迁出节点
                int toActId = transBean.getToActId();    // 迁入节点

                int[] e = new int[2];
                e[0] = fromActId;
                e[1] = toActId;

                edges.add(e);
            }

            // 判断流程图中是否存在环路
            flag = directedGraphService.isExistsLoop(vertexs, edges);
        }

        return flag;
    }

    /*
     * (非 Javadoc)
     * <p>Title: createDynaActivityQueue</p>
     * <p>Description: 生成动态通知节点的审批队列信息 </p>
     * @param bean
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ProcessEngineCommonService#createDynaActivityQueue(com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void createDynaActivityQueue(ProcessInstanceBean bean) throws Exception {

        List<ActivityBean> acts = activityDAO.getDynaActivities(bean.getInstanceId());

        if (acts != null && acts.size() > 0) {

            for (ActivityBean act : acts) {
                // 参数信息
                ProcessInstanceBean insBean = engineCommonHandler.setCurrentActivityHandler(act);
                insBean.setCurrentActivityHandler(insBean.getCurrentActivityHandler());
                insBean.setAttrList(bean.getAttrList());

                // 删除原审批队列信息
                activityDAO.delDynaActivityQueue(bean.getInstanceCode());

                // 动态节点函数类型及函数
                Map<String, String> map = act.getPropertyMap();
                String funcType = (String) map.get(WorkFlowConstants.A_DYNA_FUNC_TYPE);
                String funcName = (String) map.get(WorkFlowConstants.A_DYNA_FUNC_NAME);

                if (funcType != null && funcName != null) {
                    this.invokeFunc(insBean, funcType, funcName);
                }
            }
        }
    }

    @Override
    public String isDynamicNode(ActivityBean activityBean) throws Exception {
        Map<String, String> map = activityBean.getPropertyMap();

        Object isDynaNode = map.get(WorkFlowConstants.A_IS_DYNAMIC_NODE);

        if (isDynaNode == null || "".equals(isDynaNode)) isDynaNode = "N";

        return String.valueOf(isDynaNode);
    }


}
