package com.papla.cloud.workflow.engine.monitor;

import java.util.Map;

public interface ProcessMonitorService {

    Map<String, Object> getProcessJsonByInstanceCode(String instanceCode) throws Exception;

    Map<String, Object> getProcessJsonByTaskId(String taskId) throws Exception;

}
