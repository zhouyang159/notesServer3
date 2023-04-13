package com.aboutdk.note.config;

import com.aboutdk.note.util.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeIdConfig {

   @Bean
   Snowflake snowflake() {
      Snowflake snowflake = new Snowflake(27);
      return snowflake;
   }
}
