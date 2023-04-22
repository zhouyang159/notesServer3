package com.aboutdk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class GatewayService {
   public static void main(String[] args) {
      SpringApplication.run(GatewayService.class, args);
   }

   @Autowired
   private DiscoveryClient discoveryClient;

   @Bean
   public RouteLocator myRoutes(RouteLocatorBuilder builder) {

      List<String> services = this.discoveryClient.getServices();
      List<ServiceInstance> instances = new ArrayList<ServiceInstance>();
      services.forEach(serviceName -> {
         this.discoveryClient.getInstances(serviceName).forEach(instance ->{
            instances.add(instance);
         });
      });

      String noteServiceIp = "";
      String userServiceIp = "";
      for (int i = 0; i < instances.size(); i++) {
         ServiceInstance serviceInstance = instances.get(i);

         String instanceId = serviceInstance.getInstanceId();
         String[] arr = instanceId.split(":");

         if ("NoteApplication".equalsIgnoreCase(arr[1])) {
            noteServiceIp = "http://" + arr[0] + ":" + arr[2];
         }
         if ("UserApplication".equalsIgnoreCase(arr[1])) {
            userServiceIp = "http://" + arr[0] + ":" + arr[2];
         }
      }

      String finalNoteServiceIp = noteServiceIp;
      String finalUserServiceIp = userServiceIp;
      return builder.routes()
              .route(p -> p
                      .path("/api/note/**")
                      .uri(finalNoteServiceIp))
              .route(p -> p
                      .path("/api/register/**")
                      .uri(finalUserServiceIp))
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