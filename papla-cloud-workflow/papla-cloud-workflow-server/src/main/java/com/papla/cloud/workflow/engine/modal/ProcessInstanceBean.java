package com.papla.cloud.workflow.engine.modal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.papla.cloud.workflow.engine.run.engine.core.activity.AbstractActivityHandler;
import com.papla.cloud.workflow.engine.run.engine.core.instance.ProcessInstanceHandler;

import lombok.Getter;
import lombok.Setter;


/**
 * @author zhangfj
 * @ClassName: ProcessInstanceBean
 * @Description: 流程实例Bean
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class ProcessInstanceBean extends ProcessDeployBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 实例ID
     **/
    protected String instanceId;

    /**
     * 实例编码
     **/
    protected String instanceCode;

    /**
     * 实例标题
     **/
    protected String instanceTitle;

    /**
     * 实体状态
     **/
    protected String instanceState;

    /**
     * 开始时间
     **/
    protected Date beginDate;

    /**
     * 技术时间
     **/
    protected Date endDate;

    /**
     * 业务ID
     **/
    protected String businessId;

    /**
     * 业务编码
     **/
    protected String businessCode;

    /**
     * 业务名称
     **/
    protected String businessName;

    /**
     * 流程实例实体属性信息
     **/
    protected List<InstanceEntityAttrBean> attrList;

    /**
     * 流程实例属性值对
     * KEY为属性编码，Value为属性值
     **/
    protected Map<String, String> propertyMap;

    /**
     * 流程实例节点状态
     **/
    protected List<ActivityStateBean> actStateList;

    /**
     * 流程实例连线状态
     **/
    protected List<TransStateBean> transStateList;

    /**
     * 所属本流程实例的待办信息
     **/
    protected List<WorkItemBean> workItemList;

    /**
     * 流程实例属性中所涉及表达式信息
     * KEY值为属性编码(propertyCode)，Value为存储了ConditionGroupBean的List数组
     **/
    protected HashMap<String, List<ConditionGroupBean>> proExpMap;

    /**
     * 流程对应的应用信息
     */
    protected ApplicationBean appBean;

    /**
     * 业务实体信息
     */
    protected EntityBean entityBean;

    /**
     * 业务状态信息
     */
    protected List<BizStatusEnumBean> bizStatusList;

    /**
     * 是否为测试流程
     */
    protected boolean isTest;

    // ========================完成待办使用参数=======================

    /**
     * 当前审批操作 , 即按钮的名称.
     * <p>
     * 如：同意、驳回、退回、征询、委派(引擎调度时使用)等
     */
    protected String currentResult;

    /**
     * 审批时所选线的Id
     */
    protected String chooseMoveOutLineId;

    /**
     * 审批时所选线的code
     */
    protected String lineCode;

    /**
     * 连线类型: Y-是, N-否
     */
    protected String lineCategory;

    /**
     * 审批意见
     */
    protected String approverComment;

    /**
     * 当前待办ID
     */
    protected String currentTaskId;

    /**
     * 回退至节点ID
     */
    protected String backToActivityId;

    /**
     * 发起回退处理的节点ID
     */
    protected String backFromActivityId;

    /**
     * 手动指定的执行人
     */
    protected List<ActivityExecutorBean> choosedUserList;

    /**
     * 是否为超时待办
     */
    protected String isTimeout;

    /**
     * 当前业务状态
     */
    protected String currentBizState;

    /**
     * 判断流程是否被撤回
     */
    protected boolean isReject;

    /**
     * 是否查询归档信息
     */
    protected boolean isQueryArch;

    // ========================引擎调度使用参数=======================

    /**
     * 流程实例处理类
     */
    protected ProcessInstanceHandler processInstanceHandler;

    /**
     * 当前处理节点
     */
    protected AbstractActivityHandler currentActivityHandler;

    /**
     * 当前用户ID
     */
    protected String currentUserId;

    /**
     * 当前处理时间
     */
    protected Date currentDate;

    /**
     * 执行路径(引擎调度时使用)
     */
    protected List<ActivityBean> processExecPath;

    /**
     * 被执行过的路径(引擎调度时使用)
     */
    protected List<ActivityBean> oldProcessExcePath;

    /**
     * 开始节点绑定的表单信息 , 创建实例后返回值使用
     */
    //protected String startActForm ;

    /**
     * 开始节点绑定的表单URL
     */
    //protected String startActFormUrl ;

    /**
     * 是否自动失效规避策略:Y-失效，N-不失效
     */
    protected String autoDisabledAvoidStrategy;

    /**
     * 流程是否存在环路：Y-存在，N-不存在
     */
    protected boolean isExistsLoop;

}
