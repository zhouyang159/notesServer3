package com.aboutdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayService {
    public static void main(String[] args) {
        SpringApplication.run(GatewayService.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
//                .route("note_service", r -> r
//                        .path("/api/note/**")  // 匹配路径 /api/note/**
//                        .uri("lb://NoteApplication"))
////                        .uri("http://119.23.74.175:8081"))
////                        .uri("http://localhost:8081"))
//
////              .route("user_service", r -> r
////                      .path("/api/register/**") // 匹配路径 /api/register/**
////                      .uri("lb://UserApplication")) // 同上，指向 UserApplication
//
//                .route("snowflakeId_service", r -> r
//                        .path("/snowflake/**") //
//                        .uri("lb://SnowflakeidApplication"))
////                        .uri("http://119.23.74.175:8082"))
////                        .uri("http://localhost:8082"))

                .route("pc",
                        r -> r.path("/pc")
                                .filters(f -> f.rewritePath("/pc", "/pc/index.html"))
                                .uri("http://localhost"))
                .route("register",
                        r -> r.path("/register")
                                .filters(f -> f.rewritePath("/register", "/register/index.html"))
                                .uri("http://localhost"))
                .build();
    }
}
