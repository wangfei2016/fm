package com.xff.servicesmgl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xff.servicesmgl.bean.Voucher;
import com.xff.servicesmgl.dao.VoucherMapper;
import com.xff.servicesmgl.feign.PostgresFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
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
    public PageInfo<Voucher> getList() {
        PageHelper.startPage(1, 10);
        return new PageInfo<>(mapper.queryListByCondition());
    }

    @Value("${spring.datasource.hikari.master.jdbc-url}")
    private String configInfo;

    @GetMapping("/test")
    public String test() {
        return this.configInfo;
    }

    @PostMapping("/copyTab")
    public void copyTab(@RequestParam String tbName, @RequestParam String targetTbName) {
        mapper.copyTab(tbName, targetTbName);
    }

    @Autowired
    private PostgresFeignClient postgresFeignClient;

    @GetMapping("/getVoucherListForPg")
    public List<Map<Object, Object>> getVoucherListForPg() {
        return postgresFeignClient.getVoucherListForPg();
    }

}
