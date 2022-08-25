package com.papla.cloud.workflow.engine.deploy.dao;

import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;
import com.papla.cloud.workflow.engine.modal.ProcessDesignBean;

/**
 * @author zhangfj
 * @ClassName: ProcessDesignerDAO
 * @Description: 流程部署数据持久层操作
 * @date 2015年4月29日 下午12:49:56
 */
public interface ProcessDeployDAO {

    Map<String, Object> deployProcess(ProcessDeployBean processBean) throws Exception;

    ProcessDesignBean getProcessDesignByProcId(String processId) throws Exception;

    Map<String, Object> getProcessActsAndTranitionsById(String deployId) throws Exception;

    /**
     * 根据流程部署id获取流程部署信息
     * @param deployId
     * @return
     * @throws Exception
     */
     ProcessDeployBean getProcessDeployById(String deployId) throws Exception;

}
