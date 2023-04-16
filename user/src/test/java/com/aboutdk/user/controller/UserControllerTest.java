package com.aboutdk.user.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class UserControllerTest {

   @BeforeEach
   void setUp() {
   }

   @AfterEach
   void tearDown() {
   }


   @Test
   void register() {
      RestTemplate restTemplate = new RestTemplate();
      String hi = restTemplate.getForObject("http://localhost:8081/api/register/test", String.class);
      System.out.println(hi);
   }

}