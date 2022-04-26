/*
 * Copyright © 2021 https://www.cestc.cn/ All rights reserved.
 */
package com.xff.servicesmgl.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * PostgresFeignClient.
 *
 * @author wang_fei
 * @since 2022/4/26 19:00
 */
@Component
@FeignClient(value = "smep")
public interface PostgresFeignClient {

    @GetMapping({"/smep/voucher/selectAll"})
    List<Map<Object, Object>> getVoucherListForPg();

}
