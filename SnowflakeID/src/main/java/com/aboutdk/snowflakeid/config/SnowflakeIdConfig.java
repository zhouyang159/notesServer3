package com.aboutdk.snowflakeid.config;

import com.aboutdk.snowflakeid.util.Snowflake;
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
