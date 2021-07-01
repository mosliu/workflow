package net.liuxuan.db.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * [ 通用 ]中国行政地区表
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "china_area")
public class ChinaArea implements Serializable {


    private static final long serialVersionUID = 5598764540540525531L;

    /**
     * 行政代码 [ 唯一 ]
     */
    @Id
    @Column(name = "area_code", nullable = false)
    private Long areaCode;

    /**
     * 层级
     */
    @Column(name = "level", nullable = false,columnDefinition = "TINYINT")
    private Integer level;

    /**
     * 父级行政代码
     */
    @Column(name = "parent_code", nullable = false)
    private Long parentCode;

    /**
     * 邮政编码
     */
    @Column(name = "zip_code", nullable = false)
    private Integer zipCode;

    /**
     * 区号
     */
    @Column(name = "city_code", nullable = false,columnDefinition = "CHAR")
    private String cityCode;

    /**
     * 名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 简称
     */
    @Column(name = "short_name", nullable = false)
    private String shortName;

    /**
     * 组合名
     */
    @Column(name = "merger_name", nullable = false)
    private String mergerName;

    /**
     * 拼音
     */
    @Column(name = "pinyin", nullable = false)
    private String pinyin;

    /**
     * 经度
     */
    @Column(name = "lng", nullable = false)
    private BigDecimal lng;

    /**
     * 纬度
     */
    @Column(name = "lat", nullable = false)
    private BigDecimal lat;

}
