package net.liuxuan.security.jwt;

import lombok.Builder;
import lombok.Data;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-05-31
 **/
@Data
@Builder
public class JwtToken {
    /**
     * @description: 请求头的值
     */
    private String header;

    /**
     * @description: token 值
     */
    private String value;

    /**
     * @description: token 前缀
     */
    private String prefix;

    /**
     * @description: 过期时间（毫秒，这里默认20分钟）
     */
    private Long expiration;

}
