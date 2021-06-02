package net.liuxuan.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "UserGroup_RoleInfo")
public class UsergroupRoleinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "groupId")
    private Integer groupId;

    @Column(name = "roleId")
    private Integer roleId;

}
