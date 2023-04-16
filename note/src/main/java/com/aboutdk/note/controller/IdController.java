package com.aboutdk.note.controller;

import com.aboutdk.note.POJO.VO.ResponseVO;
import com.aboutdk.note.client.SnowflakeIdClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/snowflake")
@Slf4j
public class IdController {

   @Autowired
   SnowflakeIdClient snowflakeIdClient;

   @GetMapping("/id")
   public ResponseVO<String> generateSnowflakeId() {
      String snowflakeId = snowflakeIdClient.getSnowflakeId();



      return ResponseVO.success(snowflakeId);
   }
}
