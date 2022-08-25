package com.papla.cloud.workflow.engine.designer.dao;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.*;

/**
 * @author zhangfj
 * @ClassName: ProcessDesignerDAO
 * @Description: 流程设计器数据持久层操作
 * @date 2015年4月29日 下午12:49:56
 */
public interface ProcessDesignerDAO {

    /**
     * @param processBean
     * @return HashMap<String, Object>    返回类型
     * @throws Exception
     * @Title: saveProcessConfig
     * @Description: 更新流程图配置信息
     */
     void updateProcDesign(ProcessDesignBean processBean) throws Exception;

    /**
     * @param processId
     * @return
     * @throws Exception
     */
     ProcessDesignBean getProcessDesignById(String processId) throws Exception;

    /**
     * 获取业务状态列表
     * @return
     * @throws Exception
     */
     List<BizStatusEnumBean> getProcessDesignStatus(String statusName, String entityId) throws Exception;

     List<EntityFormBean> getProcessDesignForms(String entityId, String clientType, String formDesc, String formId) throws Exception;

     List<EntityAttrBean> getProcessDesignEntityAttrs(String entityId) throws Exception;

     ApplicationBean getApplicationByProcId(String processId);


    /**
     * 获取配置岗位
     * @return
     * @throws Exception
     */
    List<Map<String, String>> getProcessDesignPosts(Map<String, Object> params);
}
