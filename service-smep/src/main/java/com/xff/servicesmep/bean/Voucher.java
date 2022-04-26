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
@Table(name = "fr_sxtj")
@Getter
@Setter
public class Voucher {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "xzqhbm")
    private String xzqhbm;

    @Column(name = "sxmc")
    private String sxmc;

    @Column(name = "sxlx")
    private String sxlx;

    @Column(name = "sxsl")
    private String sxsl;

    @Column(name = "pjblsc")
    private String pjblsc;

}
