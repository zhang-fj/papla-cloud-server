package com.papla.cloud.oauth.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 在线用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    /**
     * key
     */
    private String key;

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * IP
     */
    private String ip;

    /**
     * 地址
     */
    private String address;

    /**
     * 登录时间
     */
    private Date loginTime;

}
