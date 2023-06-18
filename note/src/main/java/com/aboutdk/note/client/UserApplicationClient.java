package com.aboutdk.note.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "UserApplication")
public interface UserApplicationClient {

   @PostMapping(value = "/api/register/user/{openid}")
   String registerWithOpenid(@PathVariable String openid);
}
