package net.liuxuan.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色表
 */
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"userInfos", "userGroups", "privileges"})
@ToString(exclude = {"userInfos", "userGroups", "privileges"})
@Entity
@Table(name = "RoleInfo")
public class RoleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名
     */
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(value = "roleInfos")
    @ManyToMany(mappedBy = "roleInfos")  //配置多表关系
    private Set<UserInfo> userInfos = new HashSet<>();

    //两个一样的，可能会出问题。
    @JsonIgnoreProperties(value = "roleInfos")
    @ManyToMany(mappedBy = "roleInfos")  //配置多表关系
    private Set<UserGroup> userGroups = new HashSet<>();


    @JsonIgnoreProperties(value = "roleInfos")
    @JoinTable(name = "RoleInfo_Privilege",
            joinColumns = {@JoinColumn(name = "roleId", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "privilegeId", referencedColumnName = "ID")})
    @ManyToMany(targetEntity = Privilege.class, cascade = CascadeType.ALL)
    private Set<Privilege> privileges = new HashSet<>();

}
