package com.papla.cloud.workflow.engine.mapper;


import com.papla.cloud.workflow.engine.modal.ApplicationBean;
import com.papla.cloud.workflow.engine.modal.ApplicationParamBean;

import java.util.List;

public interface ApplicationMapper {

    /**
     * @param processId
     * @return ApplicationBean    返回类型
     * @Title: getApplicationByProcessId
     * @Description: 根据流程ID, 查找应用信息
     */
    ApplicationBean getApplicationByProcId(String processId);

    /**
     * @param appId
     * @return List<ApplicationParamBean>
     * @Description 根据应用Id，获取应用参数列表
     */
    List<ApplicationParamBean> getApplicationParams(String appId);

}
