package com.papla.cloud.workflow.engine.modal;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

/**
 * 根据实体编码启动时，发送ajax请求发送的参数
 *
 * @author zhangfj
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class CreateByEntityParams {

    // 实体属性HashMap
    private HashMap entityAttJson;
    // 实体属性String
    private String entityAttJsonString;
    // 实体编码
    private String entityCode;
    // 实例编码
    private String instanceCode;
    // 已选中的用户
    private String[] userIds;
    // ws action 演示环境中调用
    private String workflowWS;
    //登陆名称
    private String loginName;

}
