package net.liuxuan.security.controller;

import net.liuxuan.security.jwt.JwtToken;
import net.liuxuan.security.service.LoginService;
import net.liuxuan.springconf.CommonResponseDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
