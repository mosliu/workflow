package net.liuxuan.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-21
 **/
@Configuration("myAuthenticationProvider")
@Slf4j
public class MyAuthenticationProvider implements AuthenticationProvider {

//    @Autowired
//    private MyUserDetailService myUserDetailService;
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private  UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userName = authentication.getName();// 这个获取表单输入中的用户名
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//        String encodePassword = passwordEncoder.encode(password);
        if (!passwordEncoder.matches(password,userDetails.getPassword())) {
            throw new UsernameNotFoundException("用户名或者密码不正确");
        }

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), authorities);

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}