package com.xff.servicesmep.bean;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Voucher.
 *
 * @author wang_fei
 * @since 2022/4/6 12:31
 */
@Table(name = "tb_student")
@Getter
@Setter
public class Voucher {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "st_no")
    private String stNo;

    @Column(name = "st_year")
    private String stYear;

    @Column(name = "st_class")
    private String stClass;

    @Column(name = "st_name")
    private String stName;

    @Column(name = "st_sex")
    private String stSex;

}
