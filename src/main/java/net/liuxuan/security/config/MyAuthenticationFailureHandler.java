package net.liuxuan.security.config;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.utils.json.GsonUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

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
@Configuration("myAuthenctiationFailureHandler")
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        log.info("登录失败");
        // 错误码设置，这里先注释掉。登陆失败由前端处理
//        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(JSONObject.toJSONString(exception.getLocalizedMessage()));
        response.getWriter().write(GsonUtils.toJson(exception.getLocalizedMessage()));

    }
}
