package net.liuxuan.security.service;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.utils.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import static net.liuxuan.constants.SecurityConstants.CAPTCHA_CODE_KEY;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-02
 **/
@Slf4j
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    @Lazy
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public BufferedImage randomImageVerifyCode(String codeKey) {
        VerifyCodeUtils.ImageVerifyCode image = VerifyCodeUtils.getImage();
        // 将验证码的 codeKey 和 codeText , 保存在 redis 中，有效时间为 10 分钟
        redisTemplate.opsForValue().set(CAPTCHA_CODE_KEY + codeKey, image.getCodeText().toUpperCase(), 10, TimeUnit.MINUTES);
        return image.getImage();
    }

    @Override
    public void deleteImageVerifyCode(String codeKey) {
        redisTemplate.delete(CAPTCHA_CODE_KEY + codeKey);
    }

    @Override
    public boolean checkVerifyCode(String codeKey, String userCodeText) {
        // 获取服务器的 CodeText
        String serverCodeText = redisTemplate.opsForValue().get(CAPTCHA_CODE_KEY + codeKey);
        // 将 serverCodeText 和 user.codeText 都转换成小写，然后比较
        if (serverCodeText != null && serverCodeText.equals(userCodeText.toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }
}
