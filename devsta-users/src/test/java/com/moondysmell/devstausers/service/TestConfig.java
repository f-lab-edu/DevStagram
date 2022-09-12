//package com.moondysmell.devstausers.service;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.test.context.ActiveProfiles;
//
//import javax.validation.Valid;
//
//@Configuration(proxyBeanMethods = false)
//class TestConfig {
//
//  @Value("${spring.data.mongodb.uri}")
//  String mongoDBUrl = "";
//
//  @Bean("testMongoTemplate") // testMongoTemplate Bean 설정 및 선언하기
//  public MongoTemplate mongoTemplateConfig() {
//    MongoClient mongoClient = MongoClients.create(mongoDBUrl);
//    return new MongoTemplate(mongoClient, "devstagram");
//  }
//}