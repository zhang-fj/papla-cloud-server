package com.papla.cloud.oauth.security.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 用户实体
 */
@Setter
@Getter
public class OAuthUser {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户扩展信息
     */
    private Map<String,Object> userinfo;

    /**
     * 是否禁用
     */
    private Boolean enabled;

    /**
     * 用户角色
     */
    private List<String> roles;

}
