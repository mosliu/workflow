package net.liuxuan.security.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-29
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResetPasswordDto {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空!")
    private Integer uid;

    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空!")
    @Length(min = 5, message = "新密码长度要大于5位!")
    private String newPwd;
}
