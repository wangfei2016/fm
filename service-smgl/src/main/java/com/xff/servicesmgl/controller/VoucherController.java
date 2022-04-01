package com.xff.servicesmgl.controller;

import com.xff.basecore.masterslave.DataSourceKey;
import com.xff.servicesmgl.bean.Voucher;
import com.xff.servicesmgl.dao.VoucherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * VoucherController.
 *
 * @author wang_fei
 * @since 2022/4/1 16:17
 */
@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherMapper mapper;

    @PostMapping("/addSt")
    public int addSt(@RequestParam String stName, @RequestParam String stClass) {
        Voucher st = new Voucher();
        st.setId(UUID.randomUUID().toString());
        st.setStNo("1010000");
        st.setStYear("1");
        st.setStName(stName);
        st.setStClass(stClass);
        return mapper.insert(st);
    }

    @GetMapping("/getListByCondition")
    public List<Voucher> getList() {
        return mapper.selectAll();
    }

    @Value("${server.port}")
    private String configInfo;

    @GetMapping("/test")
    public String test() {
        return this.configInfo;
    }

}
