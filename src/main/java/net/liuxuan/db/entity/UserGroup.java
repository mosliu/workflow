package net.liuxuan.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户组信息表
 */
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"userInfos","roleInfos"})
@ToString(exclude = {"userInfos","roleInfos"})
@Entity
@Table(name = "UserGroup")
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户组名，及部门名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 父部门
     */
    @Column(name = "parentId")
    private Integer parentId;
    /**
     * 部门排序
     */
    @Column(name = "level", columnDefinition = "TINYINT")
    private Integer level;
    /**
     * 部门描述
     */
    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(value = "userGroups")
    @ManyToMany(mappedBy = "userGroups")  //配置多表关系
    private Set<UserInfo> userInfos = new HashSet<>();

    @JsonIgnoreProperties(value = "userGroups")
    @JoinTable(name = "UserGroup_RoleInfo",
            joinColumns = {@JoinColumn(name = "groupId", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "roleId", referencedColumnName = "ID")})
    @ManyToMany(targetEntity = RoleInfo.class, cascade = CascadeType.ALL)
    private Set<RoleInfo> roleInfos = new HashSet<>();

}
