//package com.moondysmell.devstausers.service;
//
//import com.moondysmell.devstausers.domain.document.DevUser;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import java.util.List;
//
//@SpringJUnitConfig(TestConfig.class)
//@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
//@EnableAutoConfiguration
//@DataMongoTest
//public class MongoTest {
//    @Autowired
//    @Qualifier("testMongoTemplate")
//    private MongoTemplate mongoTemplate;
//
//    @Test
//    @DisplayName("mongoTemplate Custom 빈 등록")
//    public void findOneTest() {
//        Query query = new Query(Criteria.where("email").is("momo@gmail.com"));
//        List<DevUser> target = mongoTemplate.find(query, DevUser.class);
//        Assertions.assertEquals(1, target.size());
//        Assertions.assertEquals("김모모", target.get(0).getName());
//    }
//}
