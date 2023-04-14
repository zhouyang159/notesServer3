package com.aboutdk.note.controller;

import com.aboutdk.note.POJO.VO.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/snowflake")
@Slf4j
public class IdController {

   @Value("${snowflake.baseUrl}")
   private String snowflakeBaseUrl;

   @GetMapping("/id")
   public ResponseVO<String> generateSnowflakeId() {
      RestTemplate restTemplate = new RestTemplate();
      String snowflakeID = restTemplate.getForObject(snowflakeBaseUrl + "/snowflake/id", String.class);

      return ResponseVO.success(snowflakeID);
   }
}
