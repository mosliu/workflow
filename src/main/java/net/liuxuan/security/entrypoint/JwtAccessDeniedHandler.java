package net.liuxuan.security.entrypoint;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.springconf.json.JsonUtils;
import net.liuxuan.springconf.web.ServletUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权限访问资源控制器
 *
 * @author Wang Chen Chen
 * @date 2021/5/20 14:45
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException {
        String msg = String.format("请求访问：%s，权限不足，请联系管理员", request.getRequestURI());
        CommonResponseDto responseDto = CommonResponseDto.fail403(msg);
        ServletUtils.renderString(response, JsonUtils.objectToJson(responseDto));
    }

}