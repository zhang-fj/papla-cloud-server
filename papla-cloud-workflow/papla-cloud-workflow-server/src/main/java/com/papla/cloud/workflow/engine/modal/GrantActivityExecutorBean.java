package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:GrantActivityExecutorBean
 * Function: 授权节点执行人，节点按用户部门岗位进行授权
 *
 * @author zhangfj
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class GrantActivityExecutorBean {

    private String activityId;
    private String grantType;
    private String grantValue;

}
