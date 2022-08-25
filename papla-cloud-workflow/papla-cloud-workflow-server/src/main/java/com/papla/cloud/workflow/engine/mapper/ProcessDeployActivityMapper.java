package com.papla.cloud.workflow.engine.mapper;

import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.modal.ActivityBean;

/**
 * @author zhangfj
 * @version V1.0
 * @ClassName: ProcessDeployActivityMapper.java
 * @Package com.papla.cloud.wf.mapper
 * @Description: 流程节点处理Mapper: tab - [PAPLA_WF_DEPLOY_ACTS]
 * @date 2021年8月24日 下午4:22:12
 */
public interface ProcessDeployActivityMapper {

    /**
     * @param activityBeanList
     * @return void
     * @throws Exception
     * @Title saveActivity
     * @Description TODO 批量保存【流程部署】节点信息
     */
    public void saveActivity(List<ActivityBean> activityBeanList) throws Exception;

    /**
     * @param deployId
     * @return List<ActivityBean>
     * @throws Exception
     * @Title getDeployActivityBeans
     * @Description TODO 根据【流程部署Id】获取【流程节点】列表
     */
    public List<ActivityBean> getDeployActivityBeans(String deployId) throws Exception;

    /**
     * @param instanceId
     * @return List<ActivityBean>
     * @throws Exception
     * @Title getInstanceActivityBeans
     * @Description TODO 根据【流程实例ID】获取【流程节点】列表
     */
    public List<ActivityBean> getInstanceActivityBeans(String instanceId) throws Exception;

    /**
     * @param instanceId
     * @return ActivityBean
     * @throws Exception
     * @Title getStartActivityBean
     * @Description TODO 根据【流程实例ID】获取开始【流程节点】信息
     */
    public ActivityBean getStartActivityBean(String instanceId) throws Exception;

    /**
     * @param params
     * @return ActivityBean
     * @throws Exception
     * @Title getInstanceActivityBean
     * @Description TODO 根据【流程实例ID】和【流程节点ID】获取单个【流程节点】信息
     */
    public ActivityBean getInstanceActivityBean(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return String
     * @throws Exception
     * @Title getInstanceActivityCategory
     * @Description TODO 根据【流程实例ID】和【流程节点ID】获取单个【流程节点类型】
     */
    public String getInstanceActivityCategory(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return String
     * @throws Exception
     * @Title isBoundForm
     * @Description TODO 判断【流程节点】是否绑定【业务表单】
     */
    public String isBoundForm(Map<String, String> params) throws Exception;

    /**
     * @param params
     * @return ActivityBean
     * @throws Exception
     * @Title getBaseActivityBean
     * @Description TODO 根据【任务ID】和【流程实例ID】获取运行期任务对应的【流程节点】信息
     */
    public ActivityBean getBaseActivityBean(Map<String, Object> params) throws Exception;

    /**
     * @param params
     * @return ActivityBean
     * @throws Exception
     * @Title getArchBaseActivityBean
     * @Description TODO 根据【任务ID】和【流程实例ID】获取已归档任务对应的【流程节点】信息
     */
    public ActivityBean getArchBaseActivityBean(Map<String, Object> params) throws Exception;

}
