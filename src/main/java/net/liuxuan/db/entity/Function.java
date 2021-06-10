package net.liuxuan.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "privileges")
@ToString(exclude = "privileges")
@Entity
@Table(name = "Function")
public class Function implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @JsonIgnoreProperties(value = "functions")
    @JoinTable(name = "Function_Privilege",
            joinColumns = {@JoinColumn(name = "functionId", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "privilegeId", referencedColumnName = "ID")})
    @ManyToMany(targetEntity = Privilege.class, cascade = CascadeType.ALL)
    private Set<Privilege> privileges = new HashSet<>();
}
