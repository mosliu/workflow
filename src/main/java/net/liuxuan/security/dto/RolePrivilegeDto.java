package net.liuxuan.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 角色权限Dto
 * @date 2021-06-11
 **/
@Getter
@Setter
@ToString
public class RolePrivilegeDto implements java.io.Serializable {

    @NotNull(message = "角色ID不能为空!")
    private Integer rid;

    @NotNull(message = "权限ID列表,不能为空!")
    @Size(min = 1, message = "角色权限最少赋值一个")
    private Set<Integer> permissions;

}
