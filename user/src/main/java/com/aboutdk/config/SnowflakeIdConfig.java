package com.aboutdk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aboutdk.util.Snowflake;

@Configuration
public class SnowflakeIdConfig {

   @Bean
   Snowflake snowflake() {
      Snowflake snowflake = new Snowflake(27);
      return snowflake;
   }
}
