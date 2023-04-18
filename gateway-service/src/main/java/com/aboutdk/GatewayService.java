package com.aboutdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GatewayService {
   public static void main(String[] args) {
      SpringApplication.run(GatewayService.class, args);
   }

   @Bean
   public RouteLocator myRoutes(RouteLocatorBuilder builder) {

      return builder.routes()
              .route(p -> p
                      .path("/api/register/**")
                      .uri("http://localhost:8081"))
              .route(p -> p
                      .path("/api/note/**")
                      .uri("http://localhost:8080"))
              .build();
   }
}