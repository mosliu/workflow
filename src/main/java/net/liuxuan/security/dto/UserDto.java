package net.liuxuan.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.liuxuan.db.entity.RoleInfo;
import net.liuxuan.db.entity.UserDetailsEntity;
import net.liuxuan.db.entity.UserGroup;
import net.liuxuan.db.entity.UserInfo;
import net.liuxuan.security.SecurityUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
//@JsonIgnoreProperties(ignoreUnknown = true)
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
     * 密码
     */
    @JsonIgnore
    private String password;
    /**
     * 性别
     */
    private Integer gender;


    private Integer status;
    /**
     * 部门Id
     */
    private Integer deptId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 角色名称，列表
     */
    private Set<RoleInfo> roles;

    /**
     * 生日
     */
//    private LocalDate birthday;
    private Date birthday;

    /**
     * 按钮
     */
    private List<ButtonDto> buttons;

    /**
     * 菜单
     */
    private List<MenuDto> menus;


    public UserInfo genUserInfo() {
        UserInfo ui = new UserInfo();
        ui.setName(this.getUsername()).setPassword(this.getPassword()).setIsLock(0).setActive(this.getStatus()).setId(this.getUid());
        return ui;
    }

    public void updateUserInfo(UserInfo ui) {
        if (ui == null) return;
        if (this.getUsername() != null) ui.setName(this.getUsername());
        if (isNotBlank(this.getPassword())) ui.setPassword(SecurityUtils.encryptPassword(this.getPassword()));
        if (this.getStatus() != null) ui.setActive(this.getStatus());
    }

    public UserDetailsEntity genUserDetailsEntity() {
        UserDetailsEntity detailsEntity = new UserDetailsEntity();
        detailsEntity.setAvatar(this.getAvatar()).setBirthday(this.getBirthday()).setEmail(this.getEmail())
                .setGender(this.getGender()).setNickname(this.getNickname()).setUid(this.getUid());
        return detailsEntity;
    }

    public void updateUserDetailsEntity(UserDetailsEntity details) {
        if (details == null) return;
        if (this.getAvatar() != null) details.setAvatar(this.getAvatar());
        if (this.getAvatar() != null) details.setAvatar(this.getAvatar());
        if (this.getBirthday() != null) details.setBirthday(this.getBirthday());
        if (this.getEmail() != null) details.setEmail(this.getEmail());
        if (this.getGender() != null) details.setGender(this.getGender());
        if (this.getNickname() != null) details.setNickname(this.getNickname());
//        if (this.getUid() != null) details.setUid(this.getUid());
    }

    public static UserDto fromUserInfoAndDetails(UserInfo ui, UserDetailsEntity details) {
        if (ui == null && details == null) return null;
        UserDto dto = new UserDto();
        if (ui != null) {
            dto.setUid(ui.getId())
                    .setUsername(ui.getName())
                    .setPassword(ui.getPassword())
                    .setStatus(ui.getActive())
            ;
            Set<RoleInfo> roleInfos = ui.getRoleInfos();
            if (roleInfos != null && !roleInfos.isEmpty()) {
                dto.setRoles(roleInfos);
//                Set<String> collect = roleInfos.stream().map(e -> e.getName()).collect(Collectors.toSet());
//                dto.setRoles(collect);
            }
            Set<UserGroup> userGroups = ui.getUserGroups();
            if (userGroups != null && !userGroups.isEmpty()) {
                UserGroup userGroup = userGroups.stream().findAny().orElse(null);
                if(userGroup!=null){
                    dto.setDeptId(userGroup.getId());
                    dto.setDepartmentName(userGroup.getName());
                }
            }


        }
        if (details != null) {
            dto
                    .setAvatar(details.getAvatar())
                    .setBirthday(details.getBirthday())
                    .setEmail(details.getEmail())
                    .setGender(details.getGender())
                    .setNickname(details.getNickname());

        }
        return dto;

    }
}
