package com.aboutdk.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "SnowflakeidApplication")
public interface SnowflakeIdClient {

   @GetMapping("/snowflake/id")
   String getSnowflakeId(@RequestHeader String action);
}
