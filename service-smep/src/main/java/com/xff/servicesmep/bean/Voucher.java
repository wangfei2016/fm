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
@Table(name = "voucher")
@Getter
@Setter
public class Voucher {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private int type;

    @Column(name = "add_amount")
    private Double addAmount;

}
