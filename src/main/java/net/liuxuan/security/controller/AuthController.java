package net.liuxuan.security.controller;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.db.service.ChinaAreaService;
import net.liuxuan.db.service.MenuService;
import net.liuxuan.db.service.PrivilegeService;
import net.liuxuan.security.jwt.JwtToken;
import net.liuxuan.security.service.LoginService;
import net.liuxuan.security.service.VerifyCodeService;
import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.utils.TreeNode;
import net.liuxuan.utils.TreeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/

@RestController
@Slf4j
public class AuthController {
    @Autowired
    LoginService loginService;

    @Autowired
    VerifyCodeService verifyCodeService;

    @PostMapping(value = "${jwt.route.login}")
    public CommonResponseDto login(@RequestBody Map<String, String> map) {

        if (false && !verifyCodeService.checkVerifyCode(map.get("codeKey"), map.get("codeText"))) {
            return CommonResponseDto.failCommon("验证码错误！");
        }

        String username = map.get("username");
        String password = map.get("password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return CommonResponseDto.fail401("用户或者密码不能为空！");
//            return Result.error401("用户或者密码不能为空！", null);
        }
        try {
            JwtToken login = loginService.login(username, password);
            // 登录成功后，就从 redis 中删除验证码
            verifyCodeService.deleteImageVerifyCode(map.get("codeKey"));
            return CommonResponseDto.success("登录成功", login);
        } catch (LockedException ex) {
            log.error(ex.getMessage());
            return CommonResponseDto.failCommon("此用户被锁定！暂时无法登录，请联系管理员！");
        } catch (AuthenticationException ex) {
            log.error(ex.getMessage());
            return CommonResponseDto.failCommon("用户名或密码错误");
        }

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

    @Autowired
    MenuService menuService;

    @Autowired
    PrivilegeService privilegeService;


    @Autowired
    private ChinaAreaService chinaAreaService;

    @GetMapping(value = "/auth/1")
    public CommonResponseDto test1() {
        List<TreeNode> collect = chinaAreaService.findAll()
                .stream()
                .distinct()
                .map(res -> {
                    return new TreeNode().setId(res.getAreaCode()).setParentId(res.getParentCode()).setSource(res).setName(res.getName());
                })
                .collect(Collectors.toList());
        List<TreeNode> root = TreeUtils.findRoot(collect);
        return CommonResponseDto.success(root);
//        Menu menu = menuService.findAll().get(2);
//        Privilege newP = new Privilege();
//        newP.setName("Ca");
//
//        menu.getPrivileges().add(newP);
//        Privilege privilege = privilegeService.findAll().get(0);
//        menu.getPrivileges().add(privilege);
//        Menu save = menuService.save(menu);
//
//        Set<Menu> menus = privilege.getMenus();
//        HashMap rtnMap = new HashMap();
//        rtnMap.put("menu",save);
//        rtnMap.put("menus",menus);
//        rtnMap.put("priv",privilege);
//
//
//        return CommonResponseDto.success("成功", rtnMap);
    }


}
