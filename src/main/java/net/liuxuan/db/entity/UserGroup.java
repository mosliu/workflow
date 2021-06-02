package net.liuxuan.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户组信息表
 */
@Data
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

}
