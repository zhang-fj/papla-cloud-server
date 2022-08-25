package com.papla.cloud.workflow.engine.common.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.papla.cloud.workflow.engine.common.api.ExpressionService;
import com.papla.cloud.workflow.engine.common.api.ProcessEngineCommonService;
import com.papla.cloud.workflow.engine.common.util.CurrentUserUtil;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.dao.api.ExpressionServiceDAO;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ConditionBean;
import com.papla.cloud.workflow.engine.modal.ConditionGroupBean;
import com.papla.cloud.workflow.engine.modal.InstanceEntityAttrBean;
import com.papla.cloud.workflow.engine.modal.ProcessInstanceBean;
import com.papla.cloud.workflow.engine.run.dao.ProcessInstanceDAO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author linpeng
 * @ClassName: ExpressionServiceImpl
 * @Description: 表达式处理服务接口实现类
 * @date 2015年4月27日 上午10:53:20
 */
@Service("expressionService")
public class ExpressionServiceImpl implements ExpressionService {
    @Resource
    private ExpressionServiceDAO expressionServiceDAO;
    @Resource
    private ProcessEngineCommonService processEngineCommonService;
    @Resource
    private ProcessInstanceDAO processInstanceDAO;


    /*
     * (非 Javadoc)
     * <p>Title: paraseExpressionToDB</p>
     * <p>Description: 解析流程设计器中涉及的路由表达式串。 包括路由表单、路由节点执行人和绑定条件 </p>
     * @param parentType	表达式分类：流程级-P，节点级-A
     * @param parentId		表达式分类ID：如果为流程级时，则为流程ID；否则为节点ID
     * @param propertyCode	条件绑定的流程或节点属性编码
     * @param expStr		表达式串，必输入参数
     * @return
     * @see com.papla.cloud.wf.common.api.ExpressionService#paraseExpressionToDB(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public List<ConditionGroupBean> paraseExpressionToDB(String parentType,
                                                         String parentId, String propertyCode, String expStr) {
        String userId = null;
        try {
            userId = CurrentUserUtil.getCurrentUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // [{"formId":"1233","conditions":"同绑定条件的"},{"formId":"1233","conditions":"同绑定条件的"}]
        // num,左括号 ,左表达式 ,运算符，表达式类型，有表达式，右括号，联接符 #
        // 表达式类型 【attr    属性值，fixed    固定值】
        if (expStr == null || "".equals(expStr.trim())) {
            return new ArrayList<ConditionGroupBean>();
        }
        ArrayList<ConditionGroupBean> groupList = new ArrayList<ConditionGroupBean>();
        // 转化为JSONArray
        JSONArray jsonArr = JSONArray.fromObject(expStr);
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArr.get(i);
            String condition = (String) jsonObj.get("conditions");
            String value = null;
            if (WorkFlowConstants.A_BOUND_CONDITON.equals(propertyCode)) {
                value = jsonObj.getString("boundId");
            } else if (WorkFlowConstants.A_ROUTE_FORM.equals(propertyCode) || WorkFlowConstants.P_ROUTE_FORM.equals(propertyCode)) {
                value = jsonObj.getString("formId");
            } else if (WorkFlowConstants.A_ROUTE_USER.equals(propertyCode)) {
                value = jsonObj.getString("userId");
            } else if (WorkFlowConstants.A_ROUTE_POSTION.equals(propertyCode)) {
                value = jsonObj.getString("postId");
            }

            // num,左括号 ,左表达式 ,运算符，表达式类型，有表达式，右括号，联接符 #
            //  0    1      2        3       4         5       6      7
            ConditionGroupBean cgb = new ConditionGroupBean();
            // 添加条件组信息
            cgb.setParentType(parentType);
            cgb.setParentId(parentId);
            cgb.setPropertyCode(propertyCode);
            UUID groupId = UUID.randomUUID();
            cgb.setGroupId(groupId.toString());
            cgb.setConditionValue(value);
            cgb.setCreateBy(userId);
            cgb.setCreateDt(new Date());

            ArrayList<ConditionBean> conditionBeanList = new ArrayList<ConditionBean>();
            // 解析条件str 封装为List
            String[] strArr = condition.split("#");
            for (String s : strArr) {
                String[] row = s.split(",", 9);
                ConditionBean cb = new ConditionBean();
                cb.setConditionId(UUID.randomUUID().toString());
                cb.setCreateBy(userId);
                cb.setCreateDt(new Date());
                cb.setGroupId(groupId.toString());
                // num,左括号 ,左表达式 ,运算符，表达式类型，有表达式，右括号，联接符 #
                //  0    1      2        3       4         5       6      7
                cb.setConditionNum(row[0]);
                cb.setLeftBracket(row[1]);
                cb.setLeftExp(row[2]);
                cb.setOperators(row[3]);
                cb.setExpType(row[4]);
                cb.setRightExp(row[5]);
                cb.setRightBracket(row[6]);
                cb.setJoinOperators(row[7]);
                conditionBeanList.add(cb);
            }
            cgb.setConditionList(conditionBeanList);
            groupList.add(cgb);
        }
        return groupList;
    }

    ;

    /*
     * (非 Javadoc)
     * <p>Title: judgeByBoundExpression</p>
     * <p>Description: 判断条件表达式值的真假 </p>
     * @param cgb					条件组
     * @param processInstanceBean
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ExpressionService#judgeByBoundExpression(com.papla.cloud.wf.modal.ConditionGroupBean, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public boolean judgeByBoundExpression(ConditionGroupBean cgb, ProcessInstanceBean processInstanceBean) throws Exception {
        // 实体属性
        ArrayList<InstanceEntityAttrBean> al = (ArrayList<InstanceEntityAttrBean>) processInstanceBean.getAttrList();
        StringBuffer sqlstr = new StringBuffer();
        ArrayList<ConditionBean> cbList = (ArrayList<ConditionBean>) cgb.getConditionList();

        // 流程实体业务实体内置属性数据类型
        HashMap<String, String> attrDataType = processInstanceDAO.getInsEntityAttrsDataType(processInstanceBean.getInstanceId());

        // 执行条件拼接
        for (int i = 0; i < cbList.size(); i++) {
            ConditionBean cb = cbList.get(i);

            //替换左表达式
            String lexp = cb.getLeftExp();
            if (lexp != null && !"".equals(lexp)) {
                for (InstanceEntityAttrBean iea : al) {
                    if (lexp.toUpperCase().equals(iea.getAttrCode().toUpperCase())) {
                        String value = processEngineCommonService.getInsEntityAttrValue(lexp, processInstanceBean);
                        cb.setLeftExpValue(value);
                        break;
                    }
                }
            }

            //替换右表达式
            String rexp = cb.getRightExp();
            // 表达式类型 【attr    属性值，fixed    固定值】
            //当表达式为固定值时，取表达式内容作为值
            if ("fixed".equals(cb.getExpType())) {
                cb.setRightExpValue(cb.getRightExp());
            } else if (rexp != null && !"".equals(rexp)) {
                for (InstanceEntityAttrBean iea : al) {
                    if (rexp.equals(iea.getAttrCode())) {
                        String type = iea.getAttrCategory();
                        String value = "";
                        if (type.equals("static")) {
                            value = iea.getAttrResult();
                        } else if (type.equals("dynamic")) {
                            value = processEngineCommonService.getInsEntityAttrValue(rexp, processInstanceBean);
                        }
                        cb.setRightExpValue(value);
                        break;
                    }
                }
            }
            String cbSqlString = cb.getSqlStr(attrDataType);
            sqlstr.append(cbSqlString);
        }
        if ("".equals(sqlstr.toString())) {
            return true;
        }
        Integer count = expressionServiceDAO.CheckSqlExpression("select count(1) count from wb_dual where " + sqlstr.toString());
        return count == 0 ? false : true;
    };

    /*
     * (非 Javadoc)
     * <p>Title: getRouteValue</p>
     * <p>
     * Description:  获取路由条件值
     * 		路由表单：json数组的形式
     *  	[{"formId":"1233","conditions":"同绑定条件的"},{"formId":"1233","conditions":"同绑定条件的"}]
     * </p>
     * @param parentType
     * @param parentId
     * @param propertyCode
     * @param processInstanceBean
     * @return
     * @throws Exception
     * @see com.papla.cloud.wf.common.api.ExpressionService#getRouteValue(java.lang.String, java.lang.String, java.lang.String, com.papla.cloud.wf.modal.ProcessInstanceBean)
     */
    public ArrayList<String> getRouteValue(String parentType, String parentId,
                                           String propertyCode, ProcessInstanceBean processInstanceBean) throws Exception {
        // 实体属性
        ArrayList<String> truesList = new ArrayList<String>();
        ArrayList<ConditionGroupBean> conditionGroupList = null;

        if ("P".equals(parentType)) {
            // 流程级别的表达式MAP
            HashMap<String, List<ConditionGroupBean>> hm = processInstanceBean.getProExpMap();
            if (hm != null) {
                conditionGroupList = (ArrayList<ConditionGroupBean>) hm.get(propertyCode);
            } else {
                conditionGroupList = (ArrayList<ConditionGroupBean>) paraseExpressionToDB(parentType, parentId, propertyCode, processInstanceBean.getPropertyMap().get(propertyCode).toString());
            }
        } else if ("A".equals(parentType)) {
            // 取得实例节点信息
            ArrayList<ActivityBean> actList = (ArrayList<ActivityBean>) processInstanceBean.getActivityBeanList();

            for (ActivityBean ab : actList) {
                if (parentId.equals(ab.getId())) {
                    HashMap<String, List<ConditionGroupBean>> hm = ab.getActExpMap();
                    conditionGroupList = (ArrayList<ConditionGroupBean>) hm.get(propertyCode);
                    break;
                }
            }
        }
        if (conditionGroupList == null) {
            return truesList;
        }
        for (ConditionGroupBean c : conditionGroupList) {
            boolean flag = judgeByBoundExpression(c, processInstanceBean);
            if (flag) {
                truesList.add(c.getConditionValue());
            }
        }
        return truesList;
    }

    ;

}
