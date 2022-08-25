package com.papla.cloud.workflow.engine.deploy.service.impl;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papla.cloud.workflow.engine.common.api.G6Service;
import com.papla.cloud.workflow.engine.deploy.dao.ProcessDeployDAO;
import com.papla.cloud.workflow.engine.deploy.service.ProcessDeployService;
import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;
import com.papla.cloud.workflow.engine.modal.ProcessDesignBean;
import com.papla.cloud.workflow.util.XipUtil;

/**
 * @author zhangfj
 * @ClassName: ProcessDeployServiceImpl
 * @Description: 流程部署后台数据处理服务接口
 * @date 2021-08-03
 */
@Service("processDeployServiceImpl")
public class ProcessDeployServiceImpl implements ProcessDeployService {

    private Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    private G6Service g6Service;

    @Resource
    private ProcessDeployDAO processDeployDAO;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public String deployProcess(String processId) throws Exception {
        try {

            ProcessDesignBean processDesignBean = processDeployDAO.getProcessDesignByProcId(processId);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> G6Process = objectMapper.readValue(processDesignBean.getProcessJson(), Map.class);

            ProcessDeployBean processDeployBean = g6Service.parseG6(processDesignBean.getProcessId(), UUID.randomUUID().toString(), G6Process);
            processDeployBean.setOrgId(processDesignBean.getOrgId());
            processDeployBean.setAppId(processDesignBean.getAppId());
            processDeployBean.setProcessId(processDesignBean.getProcessId());
            processDeployBean.setEntityId(processDesignBean.getEntityId());
            processDeployBean.setProcessCode(processDesignBean.getProcessCode());
            processDeployBean.setProcessName(processDesignBean.getProcessName());
            processDeployBean.setProcessCategory(processDesignBean.getProcessCategory());
            processDeployBean.setProcessDesc(processDesignBean.getProcessCode());

            processDeployDAO.deployProcess(processDeployBean);

            return XipUtil.getMessage("XIP_WF_DAO_0001", null);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, String> enableProcess(String processId) throws Exception {
        return null;
    }

    @Override
    public Map<String, String> disableProcess(String processId) throws Exception {
        return null;
    }

    @Override
    public ProcessDeployBean getProcessDeployById(String deployId) throws Exception {
        return processDeployDAO.getProcessDeployById(deployId);
    }

}
