package com.aboutdk.note.controller;

import com.aboutdk.note.POJO.VO.ResponseVO;
import com.aboutdk.note.client.SnowflakeIdClient;
import com.aboutdk.note.security.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/snowflake")
@Slf4j
public class IdController {

   @Autowired
   SnowflakeIdClient snowflakeIdClient;

   @Autowired
   private TokenService tokenService;

   @GetMapping("/id")
   public ResponseVO<String> generateSnowflakeId(@RequestHeader HttpHeaders headers) {
      String curUser = tokenService.getUsername(headers.getFirst("token"));

      String snowflakeId = snowflakeIdClient.getSnowflakeId(curUser, "addNote");

      return ResponseVO.success(snowflakeId);
   }
}
