package com.aboutdk.note.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "OrderApplication")
public interface OrderClient {

    @GetMapping("/order/hi")
    String hi();
}
