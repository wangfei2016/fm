package com.xff.servicesmep.controller;

import com.xff.servicesmep.bean.Voucher;
import com.xff.servicesmep.dao.VoucherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * VoucherController.
 *
 * @author wang_fei
 * @since 2022/4/6 12:30
 */
@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherMapper mapper;

    @GetMapping("/selectAll")
    public List<Voucher> getList() {
        return mapper.selectAll();
    }

}
