package net.liuxuan.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class JwtUser implements UserDetails {

    /**
     * 用户唯一ID
     */
    private Integer uid;

    /**
     * 根据 redis 中，关联的id
     */
    private String loginId;

    /**
     * 用户登录时，使用的用户名
     */
    private String username;

    /**
     * 用户登录时，使用的密码
     */
    private String password;

    /**
     * 用户状态， [ 0.禁用 1.正常 2.被删除 ]
     */
    private Integer status;

    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 1 正常 0 禁用
     * @return
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return status == 1;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
