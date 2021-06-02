package net.liuxuan.security.jwt;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.db.entity.UserInfo;
import net.liuxuan.db.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/

@Slf4j
@Service("jwtUserDetailsService")
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (isBlank(username)) {
            throw new UsernameNotFoundException(String.format("'%s'.传入的用户名错误", username));
        }
        // 根据用户名获取数据库的用户信息
        UserInfo userInfo = userInfoService.fetchUserByUserName(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException(String.format("'%s'.这个用户不存在", username));
        } else {
            // 根据数据库中的用户信息，构建JwtUser对象
//            List<SimpleGrantedAuthority> collect = sysUser.getRoles().stream().map(SysRole::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            List<SimpleGrantedAuthority> collect = new ArrayList<>();
            JwtUser jwtUser = new JwtUser();
            jwtUser.setUsername(userInfo.getName()).setPassword(userInfo.getPassword()).setAuthorities(collect).setStatus(userInfo.getActive());

            return jwtUser;
        }
    }

}
