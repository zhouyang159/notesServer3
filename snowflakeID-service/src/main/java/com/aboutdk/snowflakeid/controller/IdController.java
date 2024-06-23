package com.aboutdk.snowflakeid.controller;

import com.aboutdk.snowflakeid.service.impl.SnowflakeIDServiceImpl;
import com.aboutdk.snowflakeid.util.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/snowflake")
@Slf4j
public class IdController {

   @Autowired
   private Snowflake snowflake;

   @Autowired
   private SnowflakeIDServiceImpl snowflakeIDService;

   @GetMapping("/id")
   public ResponseEntity<Object> generateSnowflakeId(
           @RequestHeader(value = "username", defaultValue = "_") String username,
           @RequestHeader(value = "action") String action) throws Exception {

      if ("addNote".equals(action) || "register".equals(action)) {
         // continue;
      } else {
         return new ResponseEntity<>("header action must be addNote or register", HttpStatus.BAD_REQUEST);
      }

      long id = snowflake.nextId();
      snowflakeIDService.addID(id, username, action);

      return new ResponseEntity<>(id, HttpStatus.OK);
   }

   @PostMapping("/parse/{id}")
   public long[] parseSnowflakeId(@PathVariable long id) {
      long[] parse = snowflake.parse(id);
      return parse;
   }
}
