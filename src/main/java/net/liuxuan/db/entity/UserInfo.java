package net.liuxuan.db.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "UserInfo")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "name")
    private String name;

    /**
     * 是否启用 1启用 0 禁用
     */
    @Column(name = "active")
    private Integer active;
    /**
     * 锁定 1锁定 0非锁定
     */
    @Column(name = "islock")
    private Integer isLock;
    /**
     * 过期时间  空为不过期
     */
    @Column(name = "expireTime")
    private Date expireTime;
    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

}
