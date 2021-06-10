package net.liuxuan.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"menus", "roleInfos", "functions"})
@ToString(exclude = {"menus", "roleInfos", "functions"})
@Table(name = "Privilege")
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @JsonIgnoreProperties(value = "privileges")
    @ManyToMany(mappedBy = "privileges")  //配置多表关系
    private Set<Menu> menus = new HashSet<>();

    @JsonIgnoreProperties(value = "privileges")
    @ManyToMany(mappedBy = "privileges")  //配置多表关系
    private Set<Function> functions = new HashSet<>();

    //两个一样的，可能会出问题。
    @JsonIgnoreProperties(value = "privileges")
    @ManyToMany(mappedBy = "privileges")  //配置多表关系
    private Set<RoleInfo> roleInfos = new HashSet<>();

}
