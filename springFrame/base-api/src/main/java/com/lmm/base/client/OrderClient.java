package com.lmm.base.client;

import com.lmm.base.dto.TestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.lmm.base.OrderConstant;

@FeignClient(name = OrderConstant.SERVICE_NAME, path = "/v1/test", url = "${staffjoy.company-service-endpoint}")
public interface OrderClient {
    // Company Apis
    @PostMapping(path = "/test")
    TestResponse testList();

}
