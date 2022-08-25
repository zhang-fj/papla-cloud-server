package com.papla.cloud.oauth.security.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.papla.cloud.common.core.constants.AuthConstants;
import com.papla.cloud.common.core.utils.PageUtil;
import com.papla.cloud.common.redis.utils.RedisUtils;
import com.papla.cloud.common.web.utils.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.papla.cloud.oauth.security.domain.OnlineUser;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangfj
 * @date 2019年10月26日21:56:27
 */
@Service
@Slf4j
public class OnlineUserService {

    private final RedisUtils redisUtils;

    public OnlineUserService(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 保存在线用户信息
     * @param request    /
     */
    public void save(String key,String userid,String username,String nickname,Long expires, HttpServletRequest request) {
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        String address = StringUtils.getCityInfo(ip);
        OnlineUser OnlineUser = null;
        try {
            OnlineUser = new OnlineUser(key,userid,username,nickname,browser,ip,address,new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        redisUtils.set( AuthConstants.ONLINE_KEY + key, OnlineUser, expires);
    }

    /**
     * 查询全部数据
     *
     * @param filter   /
     * @param pageable /
     * @return /
     */
    public Map<String, Object> getAll(String filter, Pageable pageable) {
        List<OnlineUser> OnlineUsers = getAll(filter);
        return PageUtil.toPage(
                PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), OnlineUsers),
                OnlineUsers.size()
        );
    }

    /**
     * 查询全部数据，不分页
     *
     * @param filter /
     * @return /
     */
    public List<OnlineUser> getAll(String filter) {
        List<String> keys = redisUtils.scan(AuthConstants.ONLINE_KEY + "*");
        Collections.reverse(keys);
        List<OnlineUser> OnlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUser OnlineUser = (OnlineUser) redisUtils.get(key);
            if (StringUtils.isNotBlank(filter)) {
                if (OnlineUser.toString().contains(filter)) {
                    OnlineUsers.add(OnlineUser);
                }
            } else {
                OnlineUsers.add(OnlineUser);
            }
        }
        OnlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return OnlineUsers;
    }

    /**
     * 踢出用户
     * @param key /
     */
    public void kickOut(String key) {
        redisUtils.del(AuthConstants.ONLINE_KEY + key);
    }

    /**
     * 退出登录
     * @param key /
     */
    public void logout(String key) {
        redisUtils.del(AuthConstants.ONLINE_KEY + key);
    }

}
