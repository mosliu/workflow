package net.liuxuan.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import net.liuxuan.security.dto.ButtonDto;
import net.liuxuan.security.dto.MenuDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 菜单
 */
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "privileges")
@ToString(exclude = "privileges")
@Entity
@Table(name = "Menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Name")
    private String name;
    /**
     * 资源图标
     */
    @Column(name = "icon")
    private String icon;
    /**
     * 类型，menu或者button
     */
    @Column(name = "type")
    private String type;
    /**
     * 权限描述
     */
    @Column(name = "description")
    private String description;

    @Column(name = "parentId")
    private Integer parentId;

    @Column(name = "url")
    private String url;

    /**
     * 配置用户到角色的多对多关系
     *      配置多对多的映射关系
     *          1.声明表关系的配置
     *              @ManyToMany(targetEntity = Role.class)  //多对多
     *                  targetEntity：代表对方的实体类字节码
     *          2.配置中间表（包含两个外键）
     *                @JoinTable
     *                  name : 中间表的名称
     *                  joinColumns：配置当前对象在中间表的外键
     *                      @JoinColumn的数组
     *                          name：外键名
     *                          referencedColumnName：参照的主表的主键名
     *                  inverseJoinColumns：配置对方对象在中间表的外键
     */
    @JsonIgnoreProperties(value = "menus")
    @JoinTable(name="Menu_Privilege",
            joinColumns={@JoinColumn(name="menuId", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="privilegeId", referencedColumnName="ID")})
    @ManyToMany(targetEntity = Privilege.class, cascade = CascadeType.ALL)
    private Set<Privilege> privileges = new HashSet<>();

    public ButtonDto toButtonDto() {
        ButtonDto dto = new ButtonDto();
        dto.setPid(this.getId());
        dto.setResources(this.getUrl());
        dto.setTitle(this.getName());
        return dto;
    }

    public MenuDto toMenuDto() {
        MenuDto dto = new MenuDto();
        dto.setPid(this.getId());
        dto.setResources(this.getUrl());
        dto.setTitle(this.getName());
        dto.setIcon(this.getIcon());
        dto.setParentId(this.getParentId());
        return dto;
    }
}
