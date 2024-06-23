package com.aboutdk.note.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "SnowflakeidApplication")
public interface SnowflakeIdClient {

   @GetMapping("/snowflake/id")
   String getSnowflakeId(@RequestHeader("username") String username, @RequestHeader("action") String action);
}
