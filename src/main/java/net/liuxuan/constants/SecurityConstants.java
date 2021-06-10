package net.liuxuan.constants;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 常亮
 * @date 2021-06-01
 **/
public class SecurityConstants {
    //TODO 修改。
    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 在线用户，令牌前缀
     */
    public static final String ONLINE_USER_KEY = "online_user:";

    /**
     * 强制下线，令牌前缀
     */
    public static final String FORCED_OFFLINE_KEY = "forced_offline_user:";
}
