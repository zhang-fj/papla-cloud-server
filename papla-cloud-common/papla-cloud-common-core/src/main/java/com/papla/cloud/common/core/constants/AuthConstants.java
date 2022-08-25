package com.papla.cloud.common.core.constants;

public interface AuthConstants {
	
	/*-------------------------------start 认证常量 start------------------------------------*/

    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";
    
    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "basic";

    /**
     * JWT令牌前缀
     */
    String AUTHORIZATION_PREFIX = "bearer ";

    /**
     * JWT载体key
     */
    String JWT_PAYLOAD_KEY = "payload";

    /**
     * JWT ID 唯一标识
     */
    String JWT_JTI = "jti";

    /**
     * JWT存储权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";
    
    /**
     * 角色前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";
    
    
    /*-------------------------------end 认证常量 end------------------------------------*/
    
    /*-------------------------------start PAYLOAD用户信息 start-------------------------*/
    
    /**
     * 客户端ID
     */
    String CLIENT_ID_KEY = "client_id";

    /**
     * 用户ID
     */
    String USER_ID_KEY = "user_id";

    /**
     * 用户名称
     */
    String USER_NAME_KEY = "user_name";
    
	/**
	 * 用户昵称
	 */
	String NICK_NAME_KEY = "nick_name";

    /**
     * 用户扩展信息
     */
    String USER_INFO_KEY = "user_info";
    
    /*-------------------------------end PAYLOAD用户信息 end-----------------------------*/
    
    /*-------------------------------start 授权redis配置 start-----------------------------*/
    
    /**
     * 登录验证码 redis key
     */
    String LOGIN_CODE_KEY = "login_code_key";
    
    /**
     * 在线用户redis key
     */
    String ONLINE_KEY = "online_key_";

    /**
     * 在线用户检查时间
     */
    Long ONLINE_DETECT = (30*60L);

    /**
     * 在线用户续期时间
     */
    long ONLINE_RENEW = (30*60L);
    
    /*-------------------------------end 授权redis配置 end-----------------------------*/
}
