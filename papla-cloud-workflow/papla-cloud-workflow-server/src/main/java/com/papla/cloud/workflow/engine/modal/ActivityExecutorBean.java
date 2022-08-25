package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;


/**
 * 节点执行人信息
 **/
@Getter
@Setter
public class ActivityExecutorBean extends BaseBean {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private String actName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 员工ID
     */
    private String empId;

    /**
     * 员工编号
     */
    private String empCode;

    /**
     * 员工姓名
     */
    private String empName;
    /**
     * 组织id
     */
    private String orgId;
    /**
     * 组织name
     */
    private String orgName;

    /**
     * 判断节点执行人是否相等
     * (non-Javadoc)
     *
     * @see Object#equals(Object)
     */

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ActivityExecutorBean) || obj == null) return false;
        ActivityExecutorBean bean = (ActivityExecutorBean) obj;

        if (this.getUserId().equals(bean.getUserId())) return true;

        return false;

    }


}
