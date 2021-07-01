package net.liuxuan.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户信息表
 */
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"userGroups", "roleInfos"})
@ToString(exclude = {"userGroups", "roleInfos"})
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
    @JsonIgnore
    private String password;

    @JsonIgnoreProperties(value = "userInfos")
    @JoinTable(name = "UserGroup_UserInfo",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "groupId", referencedColumnName = "ID")})
    @ManyToMany(targetEntity = UserGroup.class, cascade = CascadeType.ALL)
    private Set<UserGroup> userGroups = new HashSet<>();

    @JsonIgnoreProperties(value = "userInfos")
    @JoinTable(name = "UserInfo_RoleInfo",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "roleId", referencedColumnName = "ID")})
    @ManyToMany(targetEntity = RoleInfo.class, cascade = CascadeType.ALL)
    private Set<RoleInfo> roleInfos = new HashSet<>();

//    @OneToOne(cascade=CascadeType.ALL)//People是关系的维护端，当删除 people，会级联删除 address
//    @JoinColumn(name = "uid", referencedColumnName = "uid")//people中的address_id字段参考address表中的id字段
//    private UserDetailsEntity userDetailsEntity;//地址

}
