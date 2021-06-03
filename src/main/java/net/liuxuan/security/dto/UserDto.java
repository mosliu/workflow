package net.liuxuan.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-02
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserDto {
    private Integer uid;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 角色名称，列表
     */
    private Set<String> roles;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 按钮
     */
    private List<ButtonDto> buttons;

    /**
     * 菜单
     */
    private List<MenuDto> menus;
}
