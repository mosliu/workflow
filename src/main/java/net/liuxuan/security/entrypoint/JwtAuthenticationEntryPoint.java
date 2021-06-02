package net.liuxuan.security.entrypoint;

import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.springconf.json.JsonUtils;
import net.liuxuan.springconf.web.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        String msg = String.format("请求访问：%s，认证失败，无法访问系统资源", request.getRequestURI());
        CommonResponseDto responseDto = CommonResponseDto.fail401(msg);
        ServletUtils.renderString(response, JsonUtils.objectToJson(responseDto));
    }

}