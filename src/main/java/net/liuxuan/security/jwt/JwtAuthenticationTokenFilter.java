package net.liuxuan.security.jwt;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.security.service.LoginService;
import net.liuxuan.springconf.CommonResponseDto;
import net.liuxuan.springconf.json.JsonUtils;
import net.liuxuan.springconf.web.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    LoginService loginService;

    /**
     * 获取当前登录用户的信息
     * JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     *
     * @param request  请求
     * @param response 回应
     * @param chain    filter 处理链
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 从这里开始获取 request 中的 jwt token
        // 获取 Request 中的请求头为 “ Authorization ” 的 token 值
        log.info("Request 请求：{}",request.getRequestURI());
        String tokenHeader = jwtTokenUtil.getTokenHeader();
        tokenHeader = isBlank(tokenHeader)?"Authorization":tokenHeader;
        String bearerToken = request.getHeader(tokenHeader);
        if (org.springframework.util.StringUtils.hasText(bearerToken)) {
            String tokenValue = jwtTokenUtil.getTokenValue(bearerToken);
            if (org.springframework.util.StringUtils.hasText(tokenValue)) {
                // 根据 username 去 redis 中查询 user 数据，足够信任token的情况下，可以省略这一步
                JwtUser userDetails = null;
                try {
                    String loginId = jwtTokenUtil.getIdFromToken(tokenValue);
                    userDetails = loginService.validateUser(loginId);
                } catch (AuthenticationException ex) {
                    SecurityContextHolder.clearContext();
                    ServletUtils.renderString(response, JsonUtils.objectToJson(
                            CommonResponseDto.fail403(ex.getMessage())
                    ));
                    return;
                } catch (Exception ex) {
                    SecurityContextHolder.clearContext();
                    ServletUtils.renderString(response, JsonUtils.objectToJson(
                            CommonResponseDto.fail401(ex.getMessage())
                    ));
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将用户信息，设置到 SecurityContext 中，可以在任何地方 使用 下面语句获取 获取 当前用户登录信息
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
