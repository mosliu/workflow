package net.liuxuan.security.service;

import net.liuxuan.db.entity.UserInfo;
import net.liuxuan.security.dto.UserDto;
import net.liuxuan.security.jwt.JwtToken;
import net.liuxuan.security.jwt.JwtUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/
public interface LoginService {
    UserInfo findByUsername(String username) throws UsernameNotFoundException;

    UserDto findUserInfo();

    JwtToken login(String username, String password);

//    String refreshToken(String oldToken);

    JwtToken register(String username, String password) throws AuthenticationException;

    JwtToken refresh();

    JwtUser validateUser(String loginId) throws AuthenticationException;

    void logout();
}
