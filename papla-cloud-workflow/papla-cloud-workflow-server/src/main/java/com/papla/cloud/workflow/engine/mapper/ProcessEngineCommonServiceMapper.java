package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.WorkItemBean;


public interface ProcessEngineCommonServiceMapper {

    /**
     * @param map
     * @return int    返回类型
     * @Title: isLastApprover
     * @Description: 判断是否为当前节点的最后一个审批人
     */
    public int isLastApprover(Map<String, String> params) throws Exception;

    /**
     * @param map
     * @return List<WorkItemBean>    返回类型
     * @Title: getTasks
     * @Description: 查询实例下节点待办信息
     */
    public List<WorkItemBean> getTasks(Map<String, String> params) throws Exception;

}
