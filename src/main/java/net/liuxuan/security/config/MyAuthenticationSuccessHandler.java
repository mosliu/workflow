package net.liuxuan.security.config;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.utils.json.GsonUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-21
 **/
@Configuration("myAuthenticationSuccessHandler")
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("登录成功");
        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(JSONObject.toJSONString(authentication));
        response.getWriter().write(GsonUtils.toJson(authentication, true));
    }

}