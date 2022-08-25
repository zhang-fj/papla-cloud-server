package com.papla.cloud.workflow.engine.designer.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.papla.cloud.common.mybatis.model.TabPage;
import com.papla.cloud.common.mybatis.page.PageUtils;
import com.papla.cloud.workflow.engine.modal.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papla.cloud.workflow.engine.common.util.WorkFlowConstants;
import com.papla.cloud.workflow.engine.designer.dao.ProcessDesignerDAO;
import com.papla.cloud.workflow.engine.designer.service.ProcessDesignerService;
import com.papla.cloud.workflow.util.XipUtil;

/**
 * @author zhangfj
 * @ClassName: ProcessDesignerServiceImpl
 * @Description: 流程设计器处理服务接口实现类
 * @date 2021-08-03
 */
@Service("processDesignerServiceImpl")
public class ProcessDesignerServiceImpl implements ProcessDesignerService {

    private Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    private ProcessDesignerDAO processDesignerDAO;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public String saveProcessConfig(String processId, Map<String, Object> G6Process) throws Exception {
        try {

            ObjectMapper objectMapper = new ObjectMapper();

            ProcessDesignBean processBean = new ProcessDesignBean();
            processBean.setProcessId(processId);
            processBean.setProcessJson(objectMapper.writeValueAsString(G6Process));

            processDesignerDAO.updateProcDesign(processBean);

            return XipUtil.getMessage("XIP_WF_DAO_0001", null);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public String getProcessJsonById(String processId) throws Exception {
        ProcessDesignBean bean = processDesignerDAO.getProcessDesignById(processId);
        String json =  bean.getProcessJson();
        if(StringUtils.isAllBlank(json)){
            json="{" +
                    "\"p_entity_id\":" +"\""+bean.getEntityId()+"\","+
                    "\"p_category\":" +"\""+bean.getProcessCategory()+"\","+
                    "\"p_org_id\":" +"\""+bean.getOrgId()+"\","+
                    "\"p_code\":" +"\""+bean.getProcessCode()+"\","+
                    "\"p_name\":" +"\""+bean.getProcessName()+"\""+
                  "}";
        }
        return json;
    }

    @Override
    public List<BizStatusEnumBean> getProcessDesignStatus(String statusName, String entityId) throws Exception {
        return processDesignerDAO.getProcessDesignStatus(statusName, entityId);
    }

    @Override
    public List<Map<String, Object>> getProcessDesignResourcesByType(String entityId, String type) {

        List<Map<String, Object>> attrs = new ArrayList<Map<String, Object>>();

        try {
            List<EntityAttrBean> entityAttrBeans = processDesignerDAO.getProcessDesignEntityAttrs(entityId);
            if (entityAttrBeans != null && entityAttrBeans.size() > 0) {
                for (int i = 0; i < entityAttrBeans.size(); i++) {
                    Map<String, Object> attr = new HashMap<String, Object>();
                    attr.put("attrId", entityAttrBeans.get(i).getId());
                    attr.put("attrCode", entityAttrBeans.get(i).getAttrCode());
                    attr.put("attrName", entityAttrBeans.get(i).getAttrName());
                    attr.put("attrType", entityAttrBeans.get(i).getAttrCategory());
                    attr.put("funcType", entityAttrBeans.get(i).getFuncType());
                    attr.put("funcValue", entityAttrBeans.get(i).getFuncValue());
                    attrs.add(attr);
                }
            }

            //添加系统内置的实体属性
            if (type.equals(WorkFlowConstants.INNER_VAR_FORM) || type.equals(WorkFlowConstants.INNER_VAR_COMMIT_USER)) {
                // 变量 表单 、 提交人变量

            } else if (type.equals(WorkFlowConstants.INNER_VAR_EXECUTOR)) { // 节点执行人
                attrs.addAll(this.getSysInnerEntityAttrByType(type));

            } else if (type.equals(WorkFlowConstants.INNER_VAR_TITLE)) {    // 通知标题
                attrs.addAll(this.getSysInnerEntityAttrByType(type));

            } else if (type.equals(WorkFlowConstants.INNER_VAR_CONDITION)) {    // 绑定条件
                attrs.addAll(this.getSysInnerEntityAttrByType(type));
            } else {
                // 扩展使用
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return attrs;
    }

    @Override
    public ApplicationBean getApplicationByProcId(String processId) {
        return processDesignerDAO.getApplicationByProcId(processId);
    }

    @Override
    public List<EntityFormBean> getProcessDesignForms(String entityId, String clientType, String formDesc, String formId) throws Exception {
        return processDesignerDAO.getProcessDesignForms(entityId, clientType, formDesc, formId);
    }

    ;

    /**
     * @param type
     * @return List<HashMap < String, Object>>    返回类型
     * @Title: getSysInnerEntityAttrByType
     * @Description: 根据属性分类，获取系统内置属性
     */
    public List<HashMap<String, Object>> getSysInnerEntityAttrByType(String type) {

        List<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();

        if (type.equals(WorkFlowConstants.INNER_VAR_TITLE)) {    // 通知标题
            for (int i = 0; i < 6; i++) {
                HashMap<String, Object> attr = new HashMap<String, Object>();
                attr.put("attrId", "");
                attr.put("attrType", "");
                attr.put("funcType", "");
                attr.put("funcValue", "");

                switch (i) {
                    case 0:
                        attr.put("attrCode", WorkFlowConstants.E_PROCESS_TITLE);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0007", null));
                        break;
                    case 1:
                        attr.put("attrCode", WorkFlowConstants.E_SUBMIT_DATE);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0010", null));
                        break;
                    case 2:
                        attr.put("attrCode", WorkFlowConstants.E_BUSINESS_CODE);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0012", null));
                        break;
                    case 3:
                        attr.put("attrCode", WorkFlowConstants.E_BUSINESS_NAME);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0013", null));
                        break;
                    case 4:
                        attr.put("attrCode", WorkFlowConstants.E_SUBMITTER_NAME);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0017", null));
                        break;
                    case 5:
                        attr.put("attrCode", WorkFlowConstants.E_PROCESS_NAME);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0018", null));
                        break;
                }

                results.add(attr);
            }

        } else if (type.equals(WorkFlowConstants.INNER_VAR_CONDITION)) {    // 绑定条件
            for (int i = 0; i < 7; i++) {
                HashMap<String, Object> attr = new HashMap<String, Object>();
                attr.put("attrId", "");
                attr.put("attrType", "");
                attr.put("funcType", "");
                attr.put("funcValue", "");

                switch (i) {
                    case 0:
                        attr.put("attrCode", WorkFlowConstants.E_PROCESS_TITLE);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0007", null));
                        break;
                    case 1:
                        attr.put("attrCode", WorkFlowConstants.E_SUBMITTER);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0009", null));
                        break;
                    case 2:
                        attr.put("attrCode", WorkFlowConstants.E_BUSINESS_CODE);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0012", null));
                        break;
                    case 3:
                        attr.put("attrCode", WorkFlowConstants.E_BUSINESS_NAME);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0013", null));
                        break;
                    case 4:
                        attr.put("attrCode", WorkFlowConstants.E_BUSINESS_ID);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0014", null));
                        break;
                    case 5:
                        attr.put("attrCode", WorkFlowConstants.E_ORG);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0015", null));
                        break;
                    case 6:
                        attr.put("attrCode", WorkFlowConstants.E_DEPT);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0016", null));
                        break;
                }

                results.add(attr);
            }
        } else if (type.equals(WorkFlowConstants.INNER_VAR_EXECUTOR)) { // 节点执行人
            for (int i = 0; i < 1; i++) {
                HashMap<String, Object> attr = new HashMap<String, Object>();
                attr.put("attrId", "");
                attr.put("attrType", "");
                attr.put("funcType", "");
                attr.put("funcValue", "");
                switch (i) {
                    case 0:
                        attr.put("attrCode", WorkFlowConstants.E_SUBMITTER);
                        attr.put("attrName", XipUtil.getMessage("XIP_WF_SERVICE_0009", null));
                        break;
                }
                results.add(attr);
            }
        }

        return results;
    }

    @Override
    public TabPage<Map<String, String>> getProcessDesignPosts(Map<String, Object> params) throws Exception {
        return PageUtils.selectForPage(params, new PageUtils.SelectCallBack<Map<String, String>>() {
            @Override
            public List<Map<String, String>> query(Map<String, Object> params) throws Exception {
                return processDesignerDAO.getProcessDesignPosts(params);
            }
        });
    }


}
