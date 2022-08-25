package com.papla.cloud.workflow.engine.modal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 节点信息数据项
 *
 * @author zhangfj
 * @date 2020日 下午3:28:33
 **/
@Getter
@Setter
public class ActivityBean extends BaseBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 流程ID
     **/
    private String processId;

    /**
     * 部署ID
     */
    private String deployId;

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点编码
     */
    private String code;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点序号（由设计器产生）
     */
    private int num;

    /**
     * 节点类型
     */
    private String actType;

    /**
     * 存储定义时节点属性
     */
    private List<ActivityPropertyBean> actPropertyList;

    /**
     * 节点描述
     **/
    private String actDesc;

    /**
     * 存储运行时实例节点属性信息
     **/
    private List<ActivityPropertyBean> actPropertyBeanList;

    /**
     * 存储运行时实例节点属性值对
     * KEY为属性编码，Value为属性值
     **/
    private Map<String, String> propertyMap;

    /**
     * 存储运行时实例节点迁出线集合
     **/
    private List<TransitionBean> transitionList;

    /**
     * 存储流程定义时节点属性中所涉及表达式集合
     **/
    private List<ConditionGroupBean> conditionGroupList;

    /**
     * 存储流程实例节点属性中所涉及表达式信息
     * KEY值为属性编码(propertyCode)，Value为存储了ConditionGroupBean的List数组
     **/
    private HashMap<String, List<ConditionGroupBean>> actExpMap;

    /**
     * 节点属性枚举值信息
     */
    private List<PVEnumBean> actPropEnumList;

    /**
     * 节点属性枚举值信息
     * KEY值为属性编码(propertyCode)，Value为存储了PVEnumBean的List数组
     **/
    private HashMap<String, List<PVEnumBean>> actPropEnumMap;

    // ====================运行时参数===============================

    /**
     * 存储运行时实例节点迁出线对象
     */
    private TransitionBean moveInTransition;

    /**
     * 当前选择迁出线的编码
     */
    private String chooseLineCode;

    /**
     * 是否为最后一个审批人
     */
    private boolean isLastApprover;

    /**
     * 是否执行会签驳回处理
     */
    private boolean isDoReject;


    // ====================计算节点执行人及路径参数=======================

    /**
     * 存储运行时节点执行人列表  add by gongbinglai 2014.07.04
     */
    private List<ActivityExecutorBean> activityExecutorList;

    /**
     * 存储运行时节点配置执行人列表  add by gongbinglai 2014.07.04
     */
    private List<ActivityExecutorBean> activityConfigExecutorList;

    /**
     * 存储节点是否被系统规避  add by gongbinglai 2014.07.04
     */
    private boolean isAvoidFlag;

    /**
     * 流程实例id
     */
    private String instanceId;

    /**
     * 流程实例编码
     */
    private String instanceCode;

    /**
     * 绑定条件
     **/
    private Object boundCondition;

    /**
     * 流程是否存在环路：Y-存在，N-不存在
     */
    private boolean isExistsLoop;

    /**
     * 节点最新状态
     */
    private ActivityStateBean lastActivityStateBean;

    /**
     * 当前动态队列行对象
     */
    private DynaNodeQueueBean queueBean;

    /**
     * 打开状态的审批节点
     */
    private List<String> excludeOpenNodes;

    /**
     * 动态审批队列列表
     */
    private List<DynaNodeQueueBean> queues;

    /**
     * 节点是否为再次运行
     */
    private boolean againRunning;

}
