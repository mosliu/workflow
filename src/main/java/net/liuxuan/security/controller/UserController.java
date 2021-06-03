package net.liuxuan.security.controller;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.security.service.LoginService;
import net.liuxuan.springconf.CommonResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-02
 **/
@RestController
@Slf4j
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    private LoginService loginService;

//    @ApiOperation(value = "获取用户详细信息", notes = "获取用户详细信息")

    /**
     * 获取用户详细信息
     */
    @GetMapping("/info")
    public CommonResponseDto findUserInfo() {
        return CommonResponseDto.success(loginService.findUserInfo());
    }

}
