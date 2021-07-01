package net.liuxuan.security.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-28
 **/
@Data
public class UserRelatedRoleDto {
    @NotNull(message = "用户ID不能为空!")
    private Integer uid;

    @NotNull(message = "角色ID列表,不能为空!")
    @Size(min = 1, message = "用户角色最少赋值一个")
    private Set<Integer> roles;

}
