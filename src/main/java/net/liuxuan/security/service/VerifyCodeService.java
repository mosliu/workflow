package net.liuxuan.security.service;

import java.awt.image.BufferedImage;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 图形验证码
 * @date 2021-06-02
 **/
public interface VerifyCodeService {
    BufferedImage randomImageVerifyCode(String codeKey);

    void deleteImageVerifyCode(String codeKey);

    boolean checkVerifyCode(String codeKey, String userCodeText);
}
