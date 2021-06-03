package net.liuxuan.security.dto;

import lombok.*;
import lombok.experimental.Accessors;


/**
 * All rights Reserved, Designed By www.xaaef.com
 * <p>
 * 用户登录成功后，返回的按钮权限
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 2.0
 * @date 2019/4/18 11:45
 * @copyright 2019 http://www.xaaef.com/ Inc. All rights reserved.
 */


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ButtonDto implements java.io.Serializable {

    private Integer pid;

    private String resources;

    private String title;


}
