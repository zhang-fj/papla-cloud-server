package com.papla.cloud.workflow.engine.modal;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessDeployBean extends ProcessDesignBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected String deployId;

    protected String processStatus;

    /**
     * 路由表单、路由用户/路由岗位、绑定条件表达式解析结果集
     **/
    protected List<ConditionGroupBean> conditionGroupList;

    /**
     * 存储节点对象数组
     **/
    protected List<ActivityBean> activityBeanList;

    /**
     * 存储流程连线对象集合
     **/
    protected List<TransitionBean> transitionBeanList;

    /**
     * 存储流程属性
     */
    protected List<DeployPropertyBean> deployPropertyList;

}
