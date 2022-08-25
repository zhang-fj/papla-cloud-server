package com.papla.cloud.workflow.engine.deploy.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.papla.cloud.workflow.engine.deploy.dao.ProcessDeployDAO;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActPVEnumMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActivityMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployActivityPropertyMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployConditionGroupMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployConditionMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployPropertyMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDeployTransitionMapper;
import com.papla.cloud.workflow.engine.mapper.ProcessDesignMapper;
import com.papla.cloud.workflow.engine.modal.ActivityBean;
import com.papla.cloud.workflow.engine.modal.ProcessDeployBean;
import com.papla.cloud.workflow.engine.modal.ProcessDesignBean;
import com.papla.cloud.workflow.engine.modal.TransitionBean;

/**
 * 功能：流程部署数据持久层操作
 * 作者：zhangfj
 * 时间：2021-08-03
 * 版本：2.0
 */
@Repository("processDeployDAO")
public class ProcessDeployDAOImpl implements ProcessDeployDAO {

    @Resource
    private ProcessDeployMapper processDeployMapper;

    @Resource
    private ProcessDeployPropertyMapper processDeployPropertyMapper;

    @Resource
    private ProcessDesignMapper processDesignMapper;

    @Resource
    private ProcessDeployActivityMapper processDeployActivityMapper;

    @Resource
    private ProcessDeployActivityPropertyMapper processDeployActivityPropertyMapper;

    @Resource
    private ProcessDeployActPVEnumMapper processDeployActPVEnumMapper;

    @Resource
    private ProcessDeployTransitionMapper processDeployTransitionMapper;

    @Resource
    private ProcessDeployConditionGroupMapper processDeployConditionGroupMapper;

    @Resource
    private ProcessDeployConditionMapper processDeployConditionMapper;

    @Override
    public ProcessDesignBean getProcessDesignByProcId(String processId) throws Exception {
        return processDesignMapper.getProcessDesignByProcessId(processId);
    }

    ;

    /**
     * 部署流程配置信息
     * 1、获取【流程部署】最大版本
     * 2、将其他【流程部署】设置为【历史流程】
     * 3、保存新的【流程部署】信息
     * 4、保存新的【流程部署】属性
     * 5、保存新的【流程部署】条件组(包括条件组包含的条件)
     * 6、保存新的【流程部署】节点
     * 7、保存新的【流程部署】节点属性
     * 8、保存新的【流程部署】节点条件组(包括条件组包含的条件)
     * 9、保存新的【流程部署】线
     * @param processDeployBean
     **/
    public Map<String, Object> deployProcess(ProcessDeployBean processDeployBean) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        try {

            //新的节点id List
            List<String> newActIds = new ArrayList<String>();

            //1、获取【流程部署】最大版本
            Integer maxVersion = processDeployMapper.getMaxVersionByProcessId(processDeployBean.getProcessId());
            processDeployBean.setProcessVersion((maxVersion == null ? 1 : maxVersion) + 1);

            //2、将其他【流程部署】设置为【历史流程】
            params.put("processId", processDeployBean.getProcessId());
            params.put("processStatus", "H");
            processDeployMapper.updateProcessDeployStatus(params);

            //3、保存新的【流程部署】信息
            //设置【流程部署】状态为部署状态
            processDeployBean.setProcessStatus("D");
            processDeployMapper.saveProcessDeploy(processDeployBean);


            //4、保存新的【流程部署】属性
            if (processDeployBean.getDeployPropertyList().size() > 0) {
                processDeployPropertyMapper.saveDeployProperty(processDeployBean.getDeployPropertyList());
            }

            //5、保存新的【流程部署】条件组(包括条件组包含的条件)
            if (processDeployBean.getConditionGroupList().size() > 0) {
                processDeployConditionGroupMapper.saveConditionGroup(processDeployBean.getConditionGroupList());
                for (int i = 0; i < processDeployBean.getConditionGroupList().size(); i++) {
                    if (processDeployBean.getConditionGroupList().get(i).getConditionList().size() > 0) {
                        processDeployConditionMapper.saveCondtionBean(processDeployBean.getConditionGroupList().get(i).getConditionList());
                    }
                }
            }

            //6、保存新的【流程部署】节点
            if (processDeployBean.getActivityBeanList().size() > 0) {
                processDeployActivityMapper.saveActivity(processDeployBean.getActivityBeanList());
            }

            for (int i = 0; i < processDeployBean.getActivityBeanList().size(); i++) {
                //7、保存新的【流程部署】节点属性
                if (processDeployBean.getActivityBeanList().get(i).getActPropertyList().size() > 0) {
                    processDeployActivityPropertyMapper.saveActivityProperty(processDeployBean.getActivityBeanList().get(i).getActPropertyList());
                }

                //8、保存新的【流程部署】节点条件组(包括条件组包含的条件)
                if (processDeployBean.getActivityBeanList().get(i).getConditionGroupList().size() > 0) {
                    processDeployConditionGroupMapper.saveConditionGroup(processDeployBean.getActivityBeanList().get(i).getConditionGroupList());
                    for (int j = 0; j < processDeployBean.getActivityBeanList().get(i).getConditionGroupList().size(); j++) {
                        if (processDeployBean.getActivityBeanList().get(i).getConditionGroupList().get(j).getConditionList().size() > 0) {
                            processDeployConditionMapper.saveCondtionBean(processDeployBean.getActivityBeanList().get(i).getConditionGroupList().get(j).getConditionList());
                        }
                    }
                }

                if (processDeployBean.getActivityBeanList().get(i).getActPropEnumList().size() > 0) {
                    processDeployActPVEnumMapper.saveActivityPvEnum(processDeployBean.getActivityBeanList().get(i).getActPropEnumList());
                }

                newActIds.add(processDeployBean.getActivityBeanList().get(i).getId());
            }

            //9、保存新的【流程部署】线
            if (processDeployBean.getTransitionBeanList().size() > 0) {
                processDeployTransitionMapper.saveTransitions(processDeployBean.getTransitionBeanList());
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> getProcessActsAndTranitionsById(String deployId) throws Exception {

        // 节点信息
        List<ActivityBean> acts = processDeployActivityMapper.getDeployActivityBeans(deployId);
        // 连线信息
        List<TransitionBean> transitions = processDeployTransitionMapper.getTransitionsByDeployId(deployId);

        // 返回参数
        Map<String, Object> map = new HashMap<String, Object>();
        if (acts != null && acts.size() > 0) {
            map.put("acts", acts);
        }
        if (transitions != null && transitions.size() > 0) {
            map.put("trans", transitions);
        }

        return map;
    }

    @Override
    public ProcessDeployBean getProcessDeployById(String deployId) throws Exception {
        return processDeployMapper.getProcessDeployById(deployId);
    }

}
