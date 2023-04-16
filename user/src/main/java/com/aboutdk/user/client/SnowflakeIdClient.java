package com.aboutdk.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "SnowflakeidApplication")
public interface SnowflakeIdClient {

   @GetMapping("/snowflake/id")
   String getSnowflakeId();
}
