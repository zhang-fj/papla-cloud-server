package com.papla.cloud.oauth.security.rest;

import cn.hutool.json.JSONObject;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.papla.cloud.common.core.constants.AuthConstants;
import com.papla.cloud.common.redis.utils.RedisUtils;
import com.papla.cloud.common.security.utils.JwtUtils;
import com.papla.cloud.oauth.code.LoginCodeEnum;
import com.papla.cloud.oauth.code.LoginCodeGenerator;
import com.papla.cloud.oauth.security.service.OnlineUserService;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "认证中心")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/oauth")
public class OAuthController {

    private final TokenEndpoint tokenEndpoint;
    private final KeyPair keyPair;
    private final OnlineUserService onlineUserService;
    private final RedisUtils redisUtils;

    @ApiOperation(value = "OAuth2认证", notes = "login")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "grant_type", defaultValue = "password", value = "授权模式", required = true),
        @ApiImplicitParam(name = "username", defaultValue = "admin", value = "登录用户名"),
        @ApiImplicitParam(name = "password", defaultValue = "123456", value = "登录密码"),
        @ApiImplicitParam(name = "uuid", value = "验证码标识"),
        @ApiImplicitParam(name = "code", value = "验证码代码")
    })
    @PostMapping("/token")
    public ResponseEntity<Object> postAccessToken(Principal principal, @RequestBody Map<String, String> parameters, HttpServletRequest request) throws HttpRequestMethodNotSupportedException, ParseException {


        // 检查验证码是否正确
        checkLoginCode((String)parameters.get("uuid"),(String)parameters.get("code"));

        // 登录验证
        OAuth2AccessToken token=tokenEndpoint.postAccessToken(principal, parameters).getBody();

        // 保存在线用户
        Map<String,Object> info = token.getAdditionalInformation();

        onlineUserService.save(
            (String)info.get(AuthConstants.JWT_JTI),
            (String)info.get(AuthConstants.USER_ID_KEY),
            (String)info.get(AuthConstants.USER_NAME_KEY),
            (String)info.get(AuthConstants.NICK_NAME_KEY),
            AuthConstants.ONLINE_DETECT,
            request
        );

        return ResponseEntity.ok(token);
    }

    /**
     * 验证码校验
     * @param uuid
     * @param loginCode
     */
    private void checkLoginCode(String uuid,String loginCode){
        // 查询验证码
        String code = (String) redisUtils.get(AuthConstants.LOGIN_CODE_KEY+"_"+uuid);
        // 清除验证码
        redisUtils.del(AuthConstants.LOGIN_CODE_KEY+"_"+uuid);
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(loginCode) || !loginCode.equalsIgnoreCase(code)) {
            throw new RuntimeException("验证码错误");
        }
    }

    @ApiOperation(value = "获取验证码", notes = "code")
    @GetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        // 获取运算的结果
        Captcha captcha = LoginCodeGenerator.getCaptcha();
        String uuid = UUID.randomUUID().toString();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(AuthConstants.LOGIN_CODE_KEY+"_"+uuid, captchaValue, 2L, TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @ApiOperation(value = "获取用户信息", notes = "info")
    @GetMapping(value = "/info")
    public ResponseEntity<Object> info() {
        return ResponseEntity.ok(JwtUtils.getUserInfo());
    }

    @ApiOperation(value = "注销", notes = "logout")
    @DeleteMapping("/logout")
    public ResponseEntity<Object> logout() {
        JSONObject payload = JwtUtils.getJwtPayload();
        String key = payload.getStr(AuthConstants.JWT_JTI); // JWT唯一标识
        onlineUserService.logout(key);
        return ResponseEntity.ok("注销成功");
    }

    @ApiOperation(value = "获取公钥信息", notes = "public-key")
    @GetMapping("/public-key")
    public Map<String, Object> getPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }
}
