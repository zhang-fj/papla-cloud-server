package com.papla.cloud.workflow.engine.common.api;

import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;

import java.util.Map;

/**
 * @author linp
 * @ClassName: XMLService
 * @Description: 工作流系统G6处理服务接口
 * @date 2016年11月8日 上午9:25:21
 */
public interface G6Service {

    /**
     * @param processId
     * @param G6Process
     * @return
     * @throws Exception 设定文件
     * @Title: parseG6
     * @Description: 解析XML文件
     */
     ProcessDeployBean parseG6(String processId, String deployId, Map<String, Object> G6Process) throws Exception;

    /**
     * @param instanceId
     * @param xmlStr
     * @param isQueryArch
     * @return String    返回类型
     * @throws Exception
     * @Title: buildMonitorXml
     * @Description: 构建流程监控xml
     */
     Map<String, Object> buildMonitorG6(String instanceId, String xmlStr, boolean isQueryArch) throws Exception;
}
