package com.xff.servicesmgl.feign.fallback;

import com.xff.servicesmgl.feign.PostgresFeignClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * PostgresFeignFallback.
 *
 * @author wang_fei
 * @since 2022/4/26 23:59
 */
@Component
public class PostgresFeignHystrix implements PostgresFeignClient {

    @Override
    public List getVoucherListForPg() {
        return Arrays.asList("getVoucherListForPg:服务降级");
    }

    @Override
    public String getServerPortForSmep() {
        return "getServerPortForSmep:服务降级";
    }
}
