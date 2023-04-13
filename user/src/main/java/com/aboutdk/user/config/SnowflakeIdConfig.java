package com.aboutdk.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aboutdk.user.util.Snowflake;

@Configuration
public class SnowflakeIdConfig {

   @Bean
   Snowflake snowflake() {
      Snowflake snowflake = new Snowflake(27);
      return snowflake;
   }
}
