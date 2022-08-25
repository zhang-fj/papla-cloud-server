package com.papla.cloud.workflow.engine.modal;

import java.util.Date;

import com.papla.cloud.workflow.engine.run.engine.core.task.WorkItemStateHandler;

import lombok.Getter;
import lombok.Setter;


/**
 * @author zhangfj
 * @ClassName: WorkItemBean
 * @Description: 工作项信息（待办）
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class WorkItemBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    /**
     * 待办ID
     **/
    private String taskId;

    /**
     * 流程部署ID
     **/
    private String deployId;

    /**
     * 流程ID
     **/
    private String processId;

    /**
     * 流程编码
     **/
    private String processCode;

    /**
     * 流程版本
     **/
    private Integer processVersion;

    /**
     * 实例ID
     **/
    private String instanceId;

    /**
     * 实例编码
     **/
    private String instanceCode;

    /**
     * 节点ID
     **/
    private String actId;

    /**
     * 节点编码
     **/
    private String actCode;

    /**
     * 节点名称
     **/
    private String actName;

    /**
     * 待办标题
     **/
    private String taskTitle;

    /**
     * 待办发起人
     **/
    private String fromUser;

    /**
     * 待办所有者
     **/
    private String assignUser;

    /**
     * 待办执行人
     **/
    private String executeUser;

    /**
     * 待办状态
     **/
    private String taskState;

    /**
     * 来源的待办任务ID，被征询待办时不为空
     **/
    private String fromTaskId;

    /**
     * 开始日期
     **/
    private Date beginDate;

    /**
     * 结束日期
     **/
    private Date endDate;

    /**
     * 处理结果
     **/
    private String result;

    /**
     * 审批意见
     **/
    private String approveCommnet;

    /**
     * 描述
     **/
    private String description;

    /**
     * 关闭原因：normal - 正常关闭，recall - 撤回关闭，goback - 退回关闭，timeout-超时关闭，copyto-抄送关闭，avoid-规避关闭
     **/
    private String closeCause;

    /**
     * 待办类别：normal - 正常待办，consult - 征询待办，copyto - 抄送待办，avoid - 规避待办
     **/
    private String createType;

    /**
     * 待办是否失效: Y-有效,N-失效
     */
    private String taskEffective;


    private WorkItemStateHandler workItemStateHandler;

    /**
     * 征询或委派的批注信息
     */
    private String comment;

    /**
     * 待办关闭线编码:Y-同意线,N-不同意线
     */
    private String closedLineType;
    // 是否为客户化待办
    private String isCustomTask;
    // 是否为超时待办
    private String isTimeoutTask;
    // 数据库类型
    private String dbType;

    /* ====================================================
     *
     *	查询时使用的参数
     *
     * ====================================================
     */


    /**
     * 发起人名称
     */
    private String fromUserName;

    /**
     * 待办所有者名称
     */
    private String assignUserName;

    /**
     * 待办执行人名称
     */
    private String executeUserName;

    /**
     * 业务数据Id
     */
    private String businessId;

    /**
     * 业务数据编码
     */
    private String businessCode;

    /**
     * 业务数据名称
     */
    private String businessName;

    /**
     * 实体ID
     */
    private String entityId;

    /**
     * 实体编码
     */
    private String entityCode;

    /**
     * 实体名称
     */
    private String entityName;

    private String authSrc;    // 授权来源
    private String authSrcId;    // 授权来源ID
    private String taskOwner;    // 待办所有者

}
