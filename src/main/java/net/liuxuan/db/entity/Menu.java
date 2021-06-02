package net.liuxuan.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 菜单
 */
@Data
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

    @Column(name = "parentId")
    private Integer parentId;

    @Column(name = "url")
    private String url;

}
