package net.liuxuan.security.controller;

import net.liuxuan.security.jwt.JwtToken;
import net.liuxuan.security.service.LoginService;
import net.liuxuan.security.service.VerifyCodeService;
import net.liuxuan.springconf.CommonResponseDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/

@RestController
public class AuthController {
    @Autowired
    LoginService loginService;

    @Autowired
    VerifyCodeService verifyCodeService;

    @PostMapping(value = "${jwt.route.login}")
    public CommonResponseDto login(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return CommonResponseDto.fail401("用户或者密码不能为空！");
//            return Result.error401("用户或者密码不能为空！", null);
        }
        JwtToken login = loginService.login(username, password);
        return CommonResponseDto.success("登录成功", login);
    }
//
//    @PostMapping(value = "${jwt.route.refresh}")
//    public CommonResponseDto refresh(@RequestHeader("${jwt.header}") String token) {
//        return CommonResponseDto.success("刷新token成功!", loginService.refreshToken(token));
//    }


    @PostMapping(value = "${jwt.route.register}")
    public CommonResponseDto register(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return CommonResponseDto.fail401("用户或者密码不能为空！");
//            return Result.error401("用户或者密码不能为空！", null);
        }
        JwtToken login = loginService.register(username, password);
        return CommonResponseDto.success("注册成功", login);
    }

    /**
     * 用户退出登录
     *
     * @return
     */
    @GetMapping(value = "${jwt.route.logout}")
    public CommonResponseDto logout() {
        loginService.logout();
        return CommonResponseDto.success();
    }


    /**
     * 刷新Token值
     * 只需要在请求头中附带token即可
     *
     * @return
     */
    @GetMapping(value = "${jwt.route.refresh}")
    public CommonResponseDto refresh() {
        JwtToken refresh = loginService.refresh();
        return CommonResponseDto.success("刷新token成功", refresh);
    }


    /**
     * 获取图形验证码, codeKey 前端传入一个随机生成的字符串
     */
    @GetMapping("/verify/code/{codeKey}")
    public void imageVerifyCode(@PathVariable String codeKey, HttpServletResponse response) throws IOException {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 获取 图片 验证码
        ImageIO.write(
                verifyCodeService.randomImageVerifyCode(codeKey),
                "JPEG",
                response.getOutputStream()
        );
    }


}
