package net.liuxuan.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "datacompare")
public class Datacompare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dateStr")
    private String dateStr;

    @Column(name = "source_type")
    private Integer sourceType;

    @Column(name = "count")
    private Integer count;

    @Column(name = "data_from")
    private String dataFrom;

}
