package com.papla.cloud.oauth.code;

import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

/**
 * @author zhangfj
 * 验证码生成器
 */
public class LoginCodeGenerator {

    /**
     * 获取验证码生产类
     *
     * @return /
     */
    public static Captcha getCaptcha() {
        LoginCode loginCode = new LoginCode();
        loginCode.setCodeType(LoginCodeEnum.arithmetic);
        return switchCaptcha(loginCode);
    }

    /**
     * 依据配置信息生产验证码
     * @param loginCode 验证码配置信息
     * @return /
     */
    private static Captcha switchCaptcha(LoginCode loginCode) {
        Captcha captcha;
        synchronized (LoginCodeGenerator.class) {
            switch (loginCode.getCodeType()) {
                case arithmetic:
                    captcha = new ArithmeticCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case chinese:
                    captcha = new ChineseCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case chinese_gif:
                    captcha = new ChineseGifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case gif:
                    captcha = new GifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case spec:
                    captcha = new SpecCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                default:
                    throw new RuntimeException("验证码配置信息错误！正确配置查看 LoginCodeEnum ");
            }
        }
        if (StringUtils.isNotBlank(loginCode.getFontName())) {
            captcha.setFont(new Font(loginCode.getFontName(), Font.PLAIN, loginCode.getFontSize()));
        }
        return captcha;
    }

}
