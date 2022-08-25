package com.papla.cloud.admin.auth.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.papla.cloud.common.mybatis.domain.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Title:                User
 * @Description: TODO   用戶管理管理
 * @author
 * @date                2021-09-14
 * @version            V1.0
 */
@Getter
@Setter
public class User extends Entity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 362013224742003121L;

    /**
     * 员工ID
     */
    private String empId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 员工编码
     */
    private String empCode;

    /**
     * 员工姓名
     */
    private String empName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户类型：S-系统内置用户；U-自定义用户；注册用户-R
     */
    private String userType;

    /**
     * 固定电话
     */
    private String fixedPhone;

    /**
     * 手机号码
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 是否重置密码：Y-是；N-否
     */
    private String hasResetPwd;

    /**
     * 修改密码的时间
     */
    private Date pwdResetTime;

    /**
     * 状态：Y-启用；N-禁用
     */
    private String enabled;

    @JsonIgnore
    public String getPassword(){
        return this.password;
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

}