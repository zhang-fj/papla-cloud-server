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
 * @Description: ???????????????????????????????????????
 * @date 2015???4???27??? ??????10:38:00
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
        if (WorkFlowConstants.FUNC_TYPE_PROC.equals(funcType)) { // ????????????
            reslut = callOracleProcedure(bean, funcName);
        } else if (WorkFlowConstants.FUNC_TYPE_WEB.equals(funcType)) {    // WebService
            reslut = invokeWebService(bean, funcName);
        } else if (WorkFlowConstants.FUNC_TYPE_JAVA.equals(funcType)) { // Java??????
            reslut = invokeJavaMethod(bean, funcName);
        }
        // ??????????????????
        if (reslut != null) {
            Map<String, Object> result = JsonObjectUtil.JSONToObj(reslut, Map.class);
            // ????????? : 0-??????,1-??????
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
        //??????WebService
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
            resultJson = "{\"flag\":\"0\",\"msg\":\"" + e.getMessage() + "; ????????????:???" + procedureName + "???,????????????:???" + paramList + "\"}";
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
     * (??? Javadoc)
     * <p>Title: invokeWebService</p>
     * <p>Description: ??????Java </p>
     * @param insBean
     * @param str
     * @return ???????????????????????????xml????????? <result><flag></flag><msg></msg><data><row></row><row></row></data></result>
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ProcessEngineCommonService#invokeJavaMethod(com.papla.cloud.wf.modal.ProcessInstanceBean, java.lang.String)
     */
    @Override
    public String invokeJavaMethod(ProcessInstanceBean insBean, String str) throws Exception {
        // ???????????????
        String resultJson = "";
        try {
            // ??????????????????
            List<InstanceEntityAttrBean> arrtList = insBean.getAttrList();
            ActivityBean actBean = (insBean.getCurrentActivityHandler() == null) ? null : (insBean.getCurrentActivityHandler().getActivityBean());
            // ??????????????????
            String s = replaceVariable(str, arrtList, actBean);
            // ???????????????????????????????????????
            String clsStr = s.substring(0, s.indexOf("("));
            String paramStr = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
            // ???????????????????????????
            String clsName = clsStr.substring(0, clsStr.lastIndexOf("."));
            String methodName = clsStr.substring(clsStr.lastIndexOf(".") + 1, clsStr.length());
            // ??????????????????
            Class<?> cls = Class.forName(clsName);
            // ????????????
            Class<?>[] argsType = null;
            Object[] args = null;
            if (paramStr != null && !"".equals(paramStr)) {
                Object[] paramArray = paramStr.split(",");
                int len = paramArray.length;
                if (len > 0) {
                    argsType = new Class[len];
                    args = new Object[len];
                    for (int i = 0; i < len; i++) {
                        argsType[i] = String.class; // ??????????????????
                        args[i] = paramArray[i];
                    }
                }
            }
            // ????????????
            Method m = cls.getMethod(methodName, argsType);

            // ????????????
            Object xmlStr = m.invoke(cls.newInstance(), args);

            // ???????????????
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

        // ??????????????????
        if (leftIndex == -1 || rightIndex == -1 || leftIndex > rightIndex) {
            return exp;
        }

        // ?????????${??????????????????
        String ls = exp.substring(0, exp.indexOf(leftSymbol));

        // ?????????${??????????????????
        String rs = exp.substring(exp.indexOf(leftSymbol) + 2);

        // ?????????}?????????
        rightIndex = rs.indexOf(rightSymbol);

        // ?????????
        String vas = rs.substring(0, rightIndex);

        if (vas != null) {
            if (vas.equals(WorkFlowConstants.E_CURRENT_APPROVER)) {    // ??????????????????ID
                vas = CurrentUserUtil.getCurrentUserId();
            } else if (vas.equals(WorkFlowConstants.E_CURRENT_NODE)) {    // ????????????????????????
                vas = (activityBean == null ? vas : activityBean.getCode());
            } else if (vas.equals(WorkFlowConstants.E_CURRENT_SYSDATE)) {    // ??????????????????(????????????yyyy-MM-dd HH:mm:ss)
                vas = DateUtil.formatDate2String(new Date());
            } else {
                for (InstanceEntityAttrBean bean : al) {
                    if (vas.equals(bean.getAttrCode())) {
                        if (bean.getAttrCategory().equals(WorkFlowConstants.ATTR_CATEGORY_STATIC)) { // ????????????
                            // ?????????????????????
                            vas = bean.getAttrResult() == null ? vas : bean.getAttrResult();
                        } else if (bean.getAttrCategory().equals(WorkFlowConstants.ATTR_CATEGORY_DYNAMIC)) { // ????????????
                            String funcType = bean.getFuncType();        // ????????????: PROC-????????????,WEB-????????????
                            String funcName = bean.getFuncValue();        // ????????????

                            if (funcType != null && !"".equals(funcType) && funcName != null && !"".equals(funcName)) {
                                try {
                                    // ????????????
                                    ProcessInstanceBean insBean = null;
                                    if (activityBean != null) {
                                        insBean = engineCommonHandler.setCurrentActivityHandler(activityBean);
                                    }
                                    insBean.setAttrList(al);
                                    // ??????????????????
                                    String xmlStr = null;
                                    if (funcType.equals(WorkFlowConstants.FUNC_TYPE_PROC)) { // ??????????????????
                                        if (activityBean != null) {
                                            insBean.setAppBean(applicationMapper.getApplicationByProcId(activityBean.getProcessId()));
                                            xmlStr = this.callOracleProcedure(insBean, funcName);
                                        }
                                    } else if (funcType.equals(WorkFlowConstants.FUNC_TYPE_WEB)) { // ??????WebService
                                        xmlStr = this.invokeWebService(insBean, funcName);

                                    } else if (funcType.equals(WorkFlowConstants.FUNC_TYPE_JAVA)) { // ??????Java??????
                                        xmlStr = this.invokeJavaMethod(insBean, funcName);

                                    } else {
                                        // ????????????
                                    }

                                    // ??????????????????
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

                                        } else if ("500".equals(flagValue)) { // ???????????????????????????????????????
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
                            // ????????????
                        }
                        break;
                    }
                }
            }
        }
        // ?????????}?????????????????????
        String rightS = rs.substring(rightIndex + 1);

        return replaceVariable(ls + vas + rightS, al, activityBean);
    }

    /*
     * (??? Javadoc)
     * <p>Title: createProcessInstanceCode</p>
     * <p>Description: ????????????????????????; ??????????????????????????????????????????_??????????????????????????????????????? ; ?????? ???20140702_123456??? </p>
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ProcessEngineCommonService#createProcessInstanceCode()
     */
    @Override
    public String createProcessInstanceCode() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// ??????????????????
        // ?????????uuid??????????????????
        return df.format(new Date()) + "_" + UUID.randomUUID();// new
        // Date()???????????????????????????
    }

    ;

    /*
     * (??? Javadoc)
     * <p>Title: getInsEntityAttrValue</p>
     * <p>Description: ??????????????????-???????????????</p>
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
            // ???????????????????????????P-???????????????S-Web??????
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
                Element rootElt = document.getRootElement(); // ???????????????
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
     * (??? Javadoc)
     * <p>Title: isLastApprover</p>
     * <p>Description: ???????????????????????????????????? </p>
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
            // ????????????
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
     * (??? Javadoc)
     * <p>Title: isUpToExpectedPercent</p>
     * <p>Description: ????????????????????????/???????????? </p>
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
            // ????????????????????????????????????
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("insId", insId);
            map.put("actId", actBean.getId());

            // ????????????
            List<WorkItemBean> beans = processEngineCommonServiceMapper.getTasks(map);

            // ????????????
            if (beans != null && beans.size() > 0) {
                // ????????????
                int total = beans.size();
                // ???????????????(?????????????????????)
                int completed = 1;

                if ("Y".equals(passOrRefuseFlag)) {
                    // ??????????????????
                    for (WorkItemBean bean : beans) {
                        String taskState = bean.getTaskState();
                        String closedLineType = bean.getClosedLineType();

                        if (WorkFlowConstants.TASK_STATE_CLOSED.equals(taskState) && "Y".equals(closedLineType)) {
                            completed += 1;
                        }
                    }
                } else {
                    // ??????????????????
                    for (WorkItemBean bean : beans) {
                        String taskState = bean.getTaskState();
                        String closedLineType = bean.getClosedLineType();
                        if (WorkFlowConstants.TASK_STATE_CLOSED.equals(taskState) && "N".equals(closedLineType)) {
                            completed += 1;
                        }
                    }
                }

                // ???????????????????????????????????????
                BigDecimal completedNumberic = new BigDecimal(completed);    // ??????????????????
                BigDecimal totalNumberic = new BigDecimal(total);    // ????????????

                MathContext mc = new MathContext(4, RoundingMode.HALF_DOWN);    // ????????????
                BigDecimal bd = completedNumberic.divide(totalNumberic, mc).multiply(new BigDecimal(100));
                float actualPercent = bd.floatValue();

                // ?????????????????????????????????
                Map<String, String> propertyMap = actBean.getPropertyMap();
                String percent = (String) propertyMap.get(WorkFlowConstants.A_COUNTERSIGN_PERCENT);
                float expectedPercent = Float.parseFloat(percent);

                // ????????????????????????
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
     * (??? Javadoc)
     * <p>Title: isDisableAvoidStrategy</p>
     * <p>Description: ???????????????????????????????????? </p>
     * <p>
     * 	??????????????????????????????????????????:
     * 	 1)???????????????????????????????????????????????????????????????????????????
     * 	 2)???...
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
        // ???????????????????????????????????????
        Map<String, Object> map = processDeployDAO.getProcessActsAndTranitionsById(deployId);
        if (!map.isEmpty()) {
			/*-----------------------------------------------------------------------------------------
			 * 1.????????????????????????????????????????????????:
			 * 2.???????????????????????????????????????????????????
			 * 3.???????????????
			 *    ???AOV??????????????????????????????????????????????????????????????????????????????????????????????????????0??????????????????
			 * 		 (1) ?????????????????????0????????????????????????
			 * 		 (2) ??????????????????????????????????????????
			 * 	    ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			 * 4.AOV?????????????????????????????????????????????????????????????????????????????????????????????(Activity On Vertex network).
			 -----------------------------------------------------------------------------------------*/

            // ?????????????????????????????????
            List<ActivityBean> acts = (List<ActivityBean>) map.get("acts");
            List<TransitionBean> trans = (List<TransitionBean>) map.get("trans");

            if (acts == null) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0023", null));
            if (acts != null && acts.size() == 0) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0023", null));

            if (trans == null) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0024", null));
            if (trans != null && trans.size() == 0) throw new Exception(XipUtil.getMessage("XIP_WF_COMMON_0024", null));

            // ??????????????????
            List<Integer> vertexs = new ArrayList<Integer>();
            for (ActivityBean act : acts) {
                vertexs.add(act.getNum());
            }
            // ????????????
            List<int[]> edges = new ArrayList<int[]>();
            for (TransitionBean transBean : trans) {
                int fromActId = transBean.getFromActId(); // ????????????
                int toActId = transBean.getToActId();    // ????????????

                int[] e = new int[2];
                e[0] = fromActId;
                e[1] = toActId;

                edges.add(e);
            }

            // ????????????????????????????????????
            flag = directedGraphService.isExistsLoop(vertexs, edges);
        }

        return flag;
    }

    /*
     * (??? Javadoc)
     * <p>Title: createDynaActivityQueue</p>
     * <p>Description: ????????????????????????????????????????????? </p>
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
                // ????????????
                ProcessInstanceBean insBean = engineCommonHandler.setCurrentActivityHandler(act);
                insBean.setCurrentActivityHandler(insBean.getCurrentActivityHandler());
                insBean.setAttrList(bean.getAttrList());

                // ???????????????????????????
                activityDAO.delDynaActivityQueue(bean.getInstanceCode());

                // ?????????????????????????????????
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
