package net.liuxuan.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Function_Privilege")
public class FunctionPrivilege implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "functionId")
    private Integer functionId;

    @Column(name = "privilegeId")
    private Integer privilegeId;

}
