package com.papla.cloud.workflow.engine.common.util;


/**
 * 工作流系统常量类
 **/
public class WorkFlowConstants {


    /* ===========================================================
     * 1.节点类型常量定义
     * ===========================================================
     */

    /**
     * 开始节点常量值
     **/
    public static final String START_NODE = "Start";

    /**
     * 结束节点常量值
     **/
    public static final String END_NODE = "End";

    /**
     * 功能节点常量值
     **/
    public static final String FUNCTION_NODE = "Function";

    /**
     * 判断节点常量值
     **/
    public static final String SWITCH_NODE = "Switch";

    /**
     * 通知节点常量值
     **/
    public static final String NOTICE_NODE = "Notice";

    /**
     * 会签节点常量值
     **/
    public static final String COUNTERSIGN_NODE = "Countersign";

    /**
     * 抄送节点常量值
     **/
    public static final String RESPONSE_NODE = "Response";



    /* ===========================================================
     * 2.会签规则常量定义
     * ===========================================================
     */

    /**
     * 会签规则, 一票通过
     **/
    public static final String JOIN_ONE_PASS = "onePass";

    /**
     * 会签规则, 一票否决
     **/
    public static final String JOIN_ONE_REFUSE = "oneRefuse";

    /**
     * 会签规则, 比例通过
     **/
    public static final String JOIN_PERCENT_PASS = "percentPass";

    /**
     * 会签规则, 比例驳回
     **/
    public static final String JOIN_PERCENT_REFUSE = "percentRefuse";

    /**
     * 会签规则，抢单
     **/
    public static final String JOIN_FIRST_PASS = "firstPass";


    /* ===========================================================
     * 3.业务实体内置属性定义
     * ===========================================================
     */

    /**
     * 内置实体属性 - 标题（流程摘要）
     */
    public static final String E_PROCESS_TITLE = "e_process_title";

    /**
     * 内置实体属性 - 实例编码
     */
    public static final String E_INSTANCE_CODE = "e_instance_code";

    /**
     * 内置实体属性 - 提交人
     */
    public static final String E_SUBMITTER = "e_submitter";

    /**
     * 内置实体属性 - 提交时间
     */
    public static final String E_SUBMIT_DATE = "e_submit_date";
    /**
     * 内置实体属性 - 当前登录用户ID
     */
    public static final String E_CURRENT_APPROVER = "e_current_approver";

    /**
     * 内置实体属性 - 当前系统时间(格式为：yyyy-MM-dd HH:mm:ss)
     */
    public static final String E_CURRENT_SYSDATE = "e_current_sysdate";

    /**
     * 内置实体属性 - 当前流程节点编码
     */
    public static final String E_CURRENT_NODE = "e_current_node";

    /**
     * 内置实体属性 - 业务编码
     */
    public static final String E_BUSINESS_CODE = "e_business_code";

    /**
     * 内置实体属性 - 业务名称
     */
    public static final String E_BUSINESS_NAME = "e_business_name";

    /**
     * 内置实体属性 - 业务ID
     */
    public static final String E_BUSINESS_ID = "e_business_id";

    /**
     * 内置实体属性 - 组织
     */
    public static final String E_ORG = "e_org";

    /**
     * 内置实体属性 - 部门
     */
    public static final String E_DEPT = "e_dept";

    /**
     * 内置实体属性 - 当前业务状态
     */
    public static final String E_CURRENT_BIZ_STATUS = "e_current_biz_status";

    /**
     * 内置实体属性 - 业务状态分类编码
     */
    public static final String E_CURRENT_BIZ_STATUS_CAT = "e_current_biz_status_cat";

    /**
     * 内置实体属性 - 业务状态分类描述
     */
    public static final String E_CURRENT_BIZ_STATUS_DESC = "e_current_biz_status_desc";

    /**
     * 内置实体属性 - 提交人姓名
     */
    public static final String E_SUBMITTER_NAME = "e_submitter_name";

    /**
     * 内置实体属性 - 流程名称
     */
    public static final String E_PROCESS_NAME = "e_process_name";

    /**
     * 内置实体属性 - 提交人待办审批意见
     */
    public static final String E_SUBMITTER_COMMENT = "e_submitter_comment";

    /**
     * 内置实体属性 - 当前节点审批人ID和名称
     */
    public static final String E_CURRENT_NODE_APPROVER = "e_current_node_approver";

    public static final String E_CURRENT_NODE_APPROVER_ID = "e_current_node_approver_id";


    /* ===========================================================
     * 4.流程属性定义
     * ===========================================================
     */

    /**
     * 流程属性-流程名称
     */
    public static final String P_NAME = "p_name";

    /**
     * 流程属性-流程编码
     */
    public static final String P_CODE = "p_code";

    /**
     * 流程属性-流程描述
     */
    public static final String P_DESC = "p_desc";

    /**
     * 流程属性-流程实体
     */
    public static final String P_ENTITY_ID = "p_entity_id";

    /**
     * 流程属性-流程分类
     */
    public static final String P_CATEGORY = "p_category";

    /**
     * 流程属性-流程组织
     */
    public static final String P_ORG_ID = "p_org_id";

    /**
     * 流程属性-流程绑定类型 : S-静态、R-路由、V-变量
     */
    public static final String P_FORM_TYPE = "p_form_type";

    /**
     * 流程属性-静态表单编码
     */
    public static final String P_STATIC_FORM = "p_static_form";

    /**
     * 流程属性-变量表单编码
     */
    public static final String P_VAR_FORM = "p_var_form";

    /**
     * 流程属性-路由表单编码
     */
    public static final String P_ROUTE_FORM = "p_route_form";

    /**
     * 流程属性-相邻节点编码
     */
    public static final String P_ADJACENT_NODE = "p_adjacent_node";

    /**
     * 流程属性-流程内部编码
     */
    public static final String P_INTERNAL_FLOW = "p_internal_flow";

    /**
     * 流程属性-提交人编码
     */
    public static final String P_AVOID_SUBMITTER = "p_avoid_submitter";

    /**
     * 流程属性-提交人变量编码
     */
    public static final String P_SUBMITTER_VAR = "p_submitter_var";

    /**
     * 流程属性-绑定条件编码
     */
    public static final String P_BOUND_CONDITION = "p_bound_condition";

    /**
     * 流程属性-撤回函数类型编码 : PROC-存储过程，WEB-WEB服务
     */
    public static final String P_REVOKE_TYPE = "p_revoke_type";

    /**
     * 流程属性-撤回函数编码
     */
    public static final String P_REVOKE_FUNC = "p_revoke_func";

    /**
     * 流程属性-撤回时业务状态信息
     */
    public static final String P_BIZ_STATUS = "p_biz_status";

    /**
     * 流程属性-平板端表单类型
     */
    public static final String P_PAD_FORM_TYPE = "p_pad_form_type";

    /**
     * 流程属性-平板端表单-静态表单
     */
    public static final String P_PAD_STATIC_FORM = "p_pad_static_form";

    /**
     * 流程属性-平板端表单-变量表单
     */
    public static final String P_PAD_VAR_FORM = "p_pad_var_form";

    /**
     * 流程属性-平板端表单-路由表单
     */
    public static final String P_PAD_ROUTE_FORM = "p_pad_route_form";

    /**
     * 流程属性-手机端表单类型
     */
    public static final String P_M_FORM_TYPE = "p_m_form_type";

    /**
     * 流程属性-手机端表单-静态表单
     */
    public static final String P_M_STATIC_FORM = "p_m_static_form";

    /**
     * 流程属性-手机端表单-变量表单
     */
    public static final String P_M_VAR_FORM = "p_m_var_form";

    /**
     * 流程属性-手机端表单-路由表单
     */
    public static final String P_M_ROUTE_FORM = "p_m_route_form";

    /* ===========================================================
     * 5.节点属性定义
     * ===========================================================
     */

    /**
     * 节点属性 - 编码
     */
    public static final String A_CODE = "a_code";

    /**
     * 节点属性 - 描述
     */
    public static final String A_DESC = "a_desc";

    /**
     * 节点属性-程序类型：PROC-存储过程，WEB-WEB服务
     */
    public static final String A_FUNC_TYPE = "a_func_type";

    /**
     * 节点属性-函数值
     */
    public static final String A_FUNC_NAME = "a_func_name";

    /**
     * 节点属性-开始节点关联表单
     */
    public static final String A_FORM = "a_form";

    /**
     * 节点属性-关联表单类型
     */
    public static final String A_FORM_TYPE = "a_form_type";

    /**
     * 节点属性-静态表单
     */
    public static final String A_STATIC_FORM = "a_static_form";

    /**
     * 节点属性-变量表单
     */
    public static final String A_VAR_FORM = "a_var_form";

    /**
     * 节点属性 - 路由表单
     */
    public static final String A_ROUTE_FORM = "a_route_form";

    /**
     * 节点属性 - 绑定条件
     */
    public static final String A_BOUND_CONDITON = "a_bound_condition";

    /**
     * 节点属性 - 执行人类型 : SU-静态用户、SP-静态岗位、RU-路由用户、RP-路由岗位、GRANT-授权类型、VAR-变量类型、PROC-过程类型、SQL-SQL类型、WEB-WEB服务、JAVA方法
     */
    public static final String A_EXECUTOR_TYPE = "a_executor_type";

    /**
     * 节点属性 - 静态用户
     */
    public static final String A_STATIC_USER = "a_static_user";

    /**
     * 节点属性 - 静态岗位
     */
    public static final String A_STATIC_POSTION = "a_static_postion";

    /**
     * 节点属性 - 路由用户
     */
    public static final String A_ROUTE_USER = "a_route_user";

    /**
     * 节点属性 - 路由岗位
     */
    public static final String A_ROUTE_POSTION = "a_route_postion";

    /**
     * 节点规避类型  single-单人规避;  multiple-多人规避
     */
//	public static final String ACTIVITY_AVOID_TYPE = "single";
    public static final String ACTIVITY_AVOID_SINGLE = "single";
    public static final String ACTIVITY_AVOID_MULTIPE = "multiple";

    /**
     * 节点属性 - 是否按部门隔离
     */
    public static final String A_DEPT_FILTER = "a_dept_filter";

    /**
     * 节点属性 - 执行人变量
     */
    public static final String A_EXEC_VAR = "a_exec_var";

    /**
     * 节点属性 - 节点属性 - 存过过程
     */
    public static final String A_EXEC_PROC = "a_exec_proc";

    /**
     * WEB服务
     */
    public static final String A_EXEC_WEB = "a_exec_web";

    /**
     * 节点属性 - SQL类型-SQL语句
     */
    public static final String A_CONFIG_SQL = "a_config_sql";

    /**
     * 节点属性 - SQL类型-更多SQL
     */
    public static final String A_MORE_SQL = "a_more_sql";

    /**
     * 节点属性 - JAVA方法-方法名
     */
    public static final String A_MORE_JAVA = "a_more_java";

    /**
     * 节点属性 - 执行前事件
     */
    public static final String A_BEFORE_EVENT = "a_before_event";

    /**
     * 节点属性 - 执行前函数名
     */
    public static final String A_BEFORE_FUNC = "a_before_func";

    /**
     * 节点属性 - 执行后事件
     */
    public static final String A_AFTER_EVENT = "a_after_event";

    /**
     * 节点属性 - 执行后函数名
     */
    public static final String A_AFTER_FUNC = "a_after_func";

    /**
     * 节点属性 - 通知标题
     */
    public static final String A_TITLE = "a_title";

    /**
     * 节点属性 - 超时天数
     */
    public static final String A_TIMEOUT_DAYS = "a_timeout_days";

    /**
     * 节点属性 - 超时执行线
     */
    public static final String A_TIMEOUT_LINE_CODE = "a_timeout_line_code";

    /**
     * 节点属性 - 参与规避
     */
    public static final String A_IS_AVOID_FLAG = "a_is_avoid_flag";

    /**
     * 节点属性 - 手动选人
     */
    public static final String A_IS_ASSGIN_EXECUTOR = "a_is_assgin_executor";

    /**
     * 节点属性 - 征询
     */
    public static final String A_IS_CONSULT = "a_is_consult";

    /**
     * 节点属性 - 委派
     */
    public static final String A_IS_DELEGATE = "a_is_delegate";

    /**
     * 节点属性 - 允许回退
     */
    public static final String A_IS_ALLOW_BACK = "a_is_allow_back";

    /**
     * 节点属性 - 允许撤办
     */
    public static final String A_IS_ALLOW_REVOKE = "a_is_allow_revoke";

    /**
     * 节点属性 - 客户化待办
     */
    public static final String A_IS_CUSTOM_TASK = "a_is_custom_task";

    /**
     * 节点属性 - 是否提醒
     */
    public static final String A_IS_NOTICE = "a_is_notice";

    /**
     * 节点属性 - 会签规则
     */
    public static final String A_COUNTERSIGN_RULE = "a_countersign_rule";

    /**
     * 节点属性 - 会签比例
     */
    public static final String A_COUNTERSIGN_PERCENT = "a_countersign_percent";

    /**
     * 节点属性 - 回退后业务状态信息
     */
    public static final String A_BIZ_STATUS = "a_biz_status";

    /**
     * 节点属性 - 是否驳回
     */
    public static final String A_IS_REJECT = "a_is_reject";

    /**
     * 节点属性 - 驳回按钮名称
     */
    public static final String A_REJECT_CST_NAME = "a_reject_cst_name";

    /**
     * 节点属性 - 驳回后业务状态信息
     */
    public static final String A_BIZ_REJECT_STATUS = "a_biz_reject_status";

    /**
     * 节点属性 - 平板端表单类型
     */
    public static final String A_PAD_FORM_TYPE = "a_pad_form_type";

    /**
     * 节点属性 - 平板端表单 - 静态表单
     */
    public static final String A_PAD_STATIC_FORM = "a_pad_static_form";

    /**
     * 节点属性 - 平板端表单 - 变量表单
     */
    public static final String A_PAD_VAR_FORM = "a_pad_var_form";

    /**
     * 节点属性 - 平板端表单 - 路由表单
     */
    public static final String A_PAD_ROUTE_FORM = "a_pad_route_form";

    /**
     * 节点属性 - 手机端表单类型
     */
    public static final String A_M_FORM_TYPE = "a_m_form_type";

    /**
     * 节点属性 - 手机端表单 - 静态表单
     */
    public static final String A_M_STATIC_FORM = "a_m_static_form";

    /**
     * 节点属性 - 手机端表单 - 变量表单
     */
    public static final String A_M_VAR_FORM = "a_m_var_form";

    /**
     * 节点属性 - 手机端表单 - 路由表单
     */
    public static final String A_M_ROUTE_FORM = "a_m_route_form";

    /**
     * 节点属性 - 是否转发
     */
    public static final String A_IS_FORWARD = "a_is_forward";

    /**
     * 节点属性 - 是否动态节点
     */
    public static final String A_IS_DYNAMIC_NODE = "a_is_dynamic_node";

    /**
     * 节点属性 - 动态节点 - 函数类型 程序类型：根据WF_FUNC_TYPE_VS值集获取
     */
    public static final String A_DYNA_FUNC_TYPE = "a_dyna_func_type";

    /**
     * 节点属性 - 动态节点 - 函数名
     */
    public static final String A_DYNA_FUNC_NAME = "a_dyna_func_name";

    /* =============================================================================
     * 7.流程实例节点状态
     * started-开始，running-运行，completed-完成， canceled-撤销，terminate-中断
     * =============================================================================
     */

    /**
     * 节点启动状态
     */
    public static final String NODE_START_STATE = "started";

    /**
     * 节点运行状态
     */
    public static final String NODE_RUNNING_STATE = "running";

    /**
     * 节点完成状态
     */
    public static final String NODE_COMPLETED_STATE = "completed";

    /**
     * 节点撤消状态
     */
    public static final String NODE_CANCELED_STATE = "canceled";

    /**
     * 节点中止状态
     */
    public static final String NODE_TERMINATE_STATE = "terminate";


    /* =============================================================================
     * 8.表单类型分类
     * 表单类型：S-静态、R-路由、V-变量
     * =============================================================================
     */

    /**
     * 静态表单
     */
    public static final String FORM_TYPE_S = "S";

    /**
     * 路由表单
     */
    public static final String FORM_TYPE_R = "R";

    /**
     * 变量表单
     */
    public static final String FORM_TYPE_V = "V";


    /* =============================================================================
     * 9.执行人类型
     * 执行人类型：
     * 	SU-静态用户、SP-静态岗位、RU-路由用户、RP-路由岗位、GRANT-授权类型、
     * 	VAR-变量类型、PROC-过程类型、SQL-SQL类型、WEB-WEB服务、JAVA-Java方法
     * =============================================================================
     */

    /**
     * 静态用户
     */
    public static final String EXECUTOR_TYPE_SU = "SU";
    /**
     * 静态岗位
     */
    public static final String EXECUTOR_TYPE_SP = "SP";
    /**
     * 路由用户
     */
    public static final String EXECUTOR_TYPE_RU = "RU";
    /**
     * 路由岗位
     */
    public static final String EXECUTOR_TYPE_RP = "RP";
    /**
     * 授权类型
     */
    public static final String EXECUTOR_TYPE_GRANT = "GRANT";
    /**
     * 变量类型
     */
    public static final String EXECUTOR_TYPE_VAR = "VAR";
    /**
     * 过程类型
     */
    public static final String EXECUTOR_TYPE_PROC = "PROC";
    /**
     * SQL类型
     */
    public static final String EXECUTOR_TYPE_SQL = "SQL";
    /**
     * WEB服务
     */
    public static final String EXECUTOR_TYPE_WEB = "WEB";

    /**
     * JAVA方法
     */
    public static final String EXECUTOR_TYPE_JAVA = "JAVA";


    /* =============================================================================
     * 10.函数类型
     * 函数类型：PROC-存储过程，WEB-WEB服务
     * =============================================================================
     */

    /**
     * 存储过程
     */
    public static final String FUNC_TYPE_PROC = "PROC";

    /**
     * WEB服务
     */
    public static final String FUNC_TYPE_WEB = "WEB";

    /**
     * JAVA方法
     */
    public static final String FUNC_TYPE_JAVA = "JAVA";

    /* =============================================================================
     * 11.待办状态
     * 待办状态：open ： 打开 ； hung-up :挂起  ； closed : 关闭
     * =============================================================================
     */

    /**
     * 打开
     */
    public static final String TASK_STATE_OPEN = "open";

    /**
     * 挂起
     */
    public static final String TASK_STATE_HUNGUP = "hung-up";

    /**
     * 关闭
     */
    public static final String TASK_STATE_CLOSED = "closed";

    /**
     * 运行中
     */
    public static final String TASK_STATE_RUNNING = "running";

    /* =============================================================================
     * 12.流程实例状态
     * started-开始，running-运行，completed-完成， canceled-撤销，terminate-中断
     * =============================================================================
     */

    /**
     * 实例启动状态
     */
    public static final String INS_START_STATE = "started";

    /**
     * 实例运行状态
     */
    public static final String INS_RUNNING_STATE = "running";

    /**
     * 实例完成状态
     */
    public static final String INS_COMPLETED_STATE = "completed";

    /**
     * 实例消状态
     */
    public static final String INS_CANCELED_STATE = "canceled";

    /**
     * 实例中止状态
     */
    public static final String INS_TERMINATE_STATE = "terminate";


    /* =============================================================================
     * 13.关闭原因
     * normal		-	正常关闭
     * recall		-	撤回关闭
     * goback		- 	退回关闭，
     * timeout		-	超时关闭
     * revoke		-	撤消征询关闭
     * avoid		-	规避关闭
     * countersign	-	会签关闭
     * reject		-	驳回关闭
     * =============================================================================
     */

    public static final String TASK_CLOSE_NORMAL = "normal";

    public static final String TASK_CLOSE_RECALL = "recall";

    public static final String TASK_CLOSE_GOBACK = "goback";

    public static final String TASK_CLOSE_TIMEOUT = "timeout";

    public static final String TASK_CLOSE_REVOKE = "revoke";

    public static final String TASK_CLOSE_AVOID = "avoid";

    public static final String TASK_CLOSE_SING = "countersign";

    public static final String TASK_CLOSE_REJECT = "reject";

    /* =============================================================================
     * 14.待办类别
     * normal - 正常待办，consult - 征询待办，copyto - 抄送待办，avoid - 规避待办, start-提交人待办
     * =============================================================================
     */

    public static final String TASK_CREATE_NORMAL = "normal";

    public static final String TASK_CREATE_CONSULT = "consult";

    public static final String TASK_CREATE_COPYTO = "copyto";

    public static final String TASK_CREATE_AVOID = "avoid";

    public static final String TASK_CREATE_START = "start";

    /* =============================================================================
     * 15.征询、委派和回退按钮
     * btnConsult - 征询  ,
     * btnDelegate - 委派  ,
     * btnBack - 回退
     * btnCloseConsult - 征询反馈
     * btnRevokeConsult - 撤消征询
     * btnSubmit-提交流程,
     * btnCancel-撤销流程,
     * btnCopyTo-关闭抄送待办
     * btnReject-驳回
     * btnSignReject-会签驳回(如果会签节点只存在一根迁出线,则默认有一根线迁入到开始节点)
     * =============================================================================
     */
    public static final String TASK_BTN_CONSULT = "btnConsult";

    public static final String TASK_BTN_DELEGATE = "btnDelegate";

    public static final String TASK_BTN_BACK = "btnBack";

    public static final String TASK_BTN_CONSULT_CLOSE = "btnCloseConsult";

    public static final String TASK_BTN_CONSULT_REVOKE = "btnRevokeConsult";

    public static final String TASK_BTN_SUBMIT = "btnSubmit";

    public static final String TASK_BTN_CANCEL = "btnCancel";

    public static final String TASK_BTN_COPYTO = "btnCopyTo";

    public static final String TASK_BTN_REJECT = "btnReject";

    public static final String TASK_BTN_SIGN_REJECT = "btnSignReject";

    public static final String TASK_BTN_FORWARD = "btnForward";

    public static final String TASK_BTN_LOOP_AGREE = "btnLoopAgree";

    /* =============================================================================
     * 16.连线状态
     * 连线状态：Y-已流经，N-未流经
     * =============================================================================
     */
    public static final String TRANS_STATE_PASS = "Y";

    public static final String TRANS_STATE_NOTPASS = "N";

    /* =============================================================================
     * 17.征询待办处理方式
     * reponse-征询反馈, revoke-撤销征询
     * =============================================================================
     */
    public static final String CONSULT_PROCESS_RESPONSE = "reponse";

    public static final String CONSULT_PROCESS_REVOKE = "revoke";

    /* =============================================================================
     * 18.待办是否有效(计算规避时使用）
     * Y-有效, N-无效
     * =============================================================================
     */
    public static final String TASK_EFFECTIVE_Y = "Y";

    public static final String TASK_EFFECTIVE_N = "N";

    /* =============================================================================
     * 19.业务状态分类码
     * A-起草，R-撤回、C-运行中、D-驳回、E-结束
     * =============================================================================
     */
    public static final String BIZ_STATUS_A = "A";

    public static final String BIZ_STATUS_R = "R";

    public static final String BIZ_STATUS_C = "C";

    public static final String BIZ_STATUS_D = "D";

    public static final String BIZ_STATUS_E = "E";


    /* ===========================================================
     * 20.业务实体内置属性取值范围
     *  varForm - 变量 表单
     *  varExcutor - 节点执行人
     *  varTitle - 通知标题
     *  varCondition - 绑定条件
     *  varCommitUser - 提交人变量
     * ===========================================================
     */
    public static final String INNER_VAR_FORM = "varForm";

    public static final String INNER_VAR_EXECUTOR = "varExcutor";

    public static final String INNER_VAR_TITLE = "varTitle";

    public static final String INNER_VAR_CONDITION = "varCondition";

    public static final String INNER_VAR_COMMIT_USER = "varCommitUser";


    /* =============================================================================
     * 21.业务实体属性
     * static-静态,dynamic-动态
     * =============================================================================
     */
    public static final String ATTR_CATEGORY_STATIC = "static";

    public static final String ATTR_CATEGORY_DYNAMIC = "dynamic";


    /* =============================================================================
     * 22.关联表单类型
     * PC-电脑端表单, PAD-平板端表单, Mobile-手机端表单
     * =============================================================================
     */
    public static final String CLIENT_TYPE_PC = "PC";

    public static final String CLIENT_TYPE_PAD = "PAD";

    public static final String CLIENT_TYPE_MOBILE = "Mobile";


    /* =============================================================================
     * 23.流程处理类型
     * process-流程定义期, instance-流程运行期
     * =============================================================================
     */
    public static final String PROCESS_HANDLER_TYPE = "process";

    public static final String INSTANCE_HANDLER_TYPE = "instance";


    /* =============================================================================
     * 24.待办提醒模式
     * detail-即时提醒, total-汇总提醒
     * =============================================================================
     */
    public static final String NOTICE_MODE_DETAIL = "detail";

    public static final String NOTICE_MODE_TOTAL = "total";


    /* =============================================================================
     * 25.流程实例所在时期
     * running-运行期, arched-归档期
     * =============================================================================
     */
    public static final String INSTANCE_RUNNING_PEIROD = "running";

    public static final String INSTANCE_ARCHED_PEIROD = "arched";


    /* =============================================================================
     * 26. 流程应用参数
     *
     * =============================================================================
     */
     public static final String APP_POST_URL = "APP_POST_URL";

     public static final String APP_USER_URL = "APP_USER_URL";

     public static final String APP_ORG_URL = "APP_ORG_URL";

     public static final String ACT_EXECUTOR_URL = "ACT_EXECUTOR_URL";
}
