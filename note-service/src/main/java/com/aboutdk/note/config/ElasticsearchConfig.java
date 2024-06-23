package com.aboutdk.note.config;

//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

//@Configuration
//public class ElasticsearchConfig {
//
//   @Value("${spring.elasticsearch.host}")
//   private String HOST;
//
//   @Value("${spring.elasticsearch.port}")
//   private int PORT;
//
//   private String PROTOCOL = "http";
//
//   @Value("${spring.elasticsearch.username}")
//   private String username;
//
//   @Value("${spring.elasticsearch.password}")
//   private String password;
//
//   @Bean
//   public RestHighLevelClient client() {
//      ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//              .connectedTo(HOST + ":" + PORT)
//              .withBasicAuth(username, password) // put your credentials
//              .build();
//
//      return RestClients.create(clientConfiguration).rest();
//   }
//
//   @Bean
//   public ElasticsearchOperations elasticsearchTemplate() {
//      return new ElasticsearchRestTemplate(client());
//   }
//}
