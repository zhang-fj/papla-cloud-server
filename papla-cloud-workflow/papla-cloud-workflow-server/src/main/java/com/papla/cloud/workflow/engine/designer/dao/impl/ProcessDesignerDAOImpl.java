package com.papla.cloud.workflow.engine.designer.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.papla.cloud.workflow.engine.mapper.*;
import com.papla.cloud.workflow.engine.modal.*;
import org.springframework.stereotype.Repository;

import com.papla.cloud.workflow.engine.designer.dao.ProcessDesignerDAO;

/**
 * 功能：流程设计器数据持久层操作
 * 作者：zhangfj
 * 时间：2014年6月20日
 * 版本：2.0
 */
@Repository("processDesignerDAO")
public class ProcessDesignerDAOImpl implements ProcessDesignerDAO {

    @Resource
    private ProcessDesignMapper processDesignMapper;

    @Resource
    private EntityBizStatusEnumMapper entityBizStatusEnumMapper;

    @Resource
    private EntityFormMapper entityFormMapper;

    @Resource
    private EntityProtertyMapper entityProtertyMapper;

    @Resource
    private ApplicationMapper applicationMapper;

    @Override
    public void updateProcDesign(ProcessDesignBean processBean) throws Exception {
        processDesignMapper.updateProcessJson(processBean);
    }

    @Override
    public List<BizStatusEnumBean> getProcessDesignStatus(String statusName, String entityId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("statusName", statusName);
        return entityBizStatusEnumMapper.getEntityBizStatusList(params);
    }

    @Override
    public List<EntityAttrBean> getProcessDesignEntityAttrs(String entityId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        return entityProtertyMapper.getEntityAttrList(params);
    }

    @Override
    public List<EntityFormBean> getProcessDesignForms(String entityId, String clientType, String formDesc, String formId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("clientType", clientType);
        params.put("formDesc", formDesc);
        params.put("formId", formId);
        return entityFormMapper.getEntityFormList(params);
    }

    @Override
    public ProcessDesignBean getProcessDesignById(String processId) throws Exception {
        return processDesignMapper.getProcessDesignByProcessId(processId);
    }

    @Override
    public ApplicationBean getApplicationByProcId(String processId){
        ApplicationBean applicationBean = applicationMapper.getApplicationByProcId(processId);
        if(applicationBean != null){
            applicationBean.setParams(applicationMapper.getApplicationParams(applicationBean.getAppId()));
        }
        return applicationBean;
    }

    @Override
    public List<Map<String, String>> getProcessDesignPosts(Map<String, Object> params) {
        return processDesignMapper.getProcessDesignPosts(params);
    }

}
