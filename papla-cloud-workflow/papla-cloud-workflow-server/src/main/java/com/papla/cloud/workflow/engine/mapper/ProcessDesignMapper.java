package com.papla.cloud.workflow.engine.mapper;

import com.papla.cloud.workflow.engine.modal.ProcessDesignBean;

import java.util.List;
import java.util.Map;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessDesignMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 流程设计器Mapper: Tab - [PAPLA_WF_PROC_DESIGN]
 * @date 2021年8月24日 下午3:28:08
 */
public interface ProcessDesignMapper {

    /**
     * @param processBean
     * @return void
     * @throws Exception
     * @Title updateProcessJson
     * @Description TODO 更新【流程设计】信息
     */
     void updateProcessJson(ProcessDesignBean processBean) throws Exception;

    /**
     * @param processId
     * @return ProcessDesignBean
     * @throws Exception
     * @Title getProcessDesignByProcessId
     * @Description TODO 根据【流程设计ID】获取【流程设计】信息
     */
    ProcessDesignBean getProcessDesignByProcessId(String processId) throws Exception;

    /**
     * @param params
     * @return ProcessDesignBean
     * @throws Exception
     * @Title getProcessDesignPosts
     * @Description TODO 获取流程配置岗位信息
     */
    List<Map<String, String>> getProcessDesignPosts(Map<String, Object> params);
}
