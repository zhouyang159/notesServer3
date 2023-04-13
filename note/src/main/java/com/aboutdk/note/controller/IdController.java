package com.aboutdk.note.controller;

import com.aboutdk.note.service.impl.SnowflakeIDServiceImpl;
import com.aboutdk.note.util.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
   public long generateSnowflakeId() {
      long id = snowflake.nextId();
      snowflakeIDService.addID(id);

      return id;
   }

   @PostMapping("/parse/{id}")
   public long[] parseSnowflakeId(@PathVariable long id) {
      long[] parse = snowflake.parse(id);
      return parse;
   }
}
