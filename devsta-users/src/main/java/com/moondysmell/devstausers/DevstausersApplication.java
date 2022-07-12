package com.moondysmell.devstausers;

import com.moondysmell.devstausers.domain.document.DevUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;


@SpringBootApplication
public class DevstausersApplication {

//	@Autowired
//	private MongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DevstausersApplication.class, args);
	}

//	@Bean
//	public ApplicationRunner applicationRunner(){
//		return args -> {
//			DevUser devUser = new DevUser();
//			devUser.setId("5");
//			devUser.setName("Hannah");
//			devUser.setNickname("smell");
//			devUser.setPassword("1234");
//			devUser.setEmail("dlgiddk10@gmail.com");
//
//			mongoTemplate.insert(devUser);
//		};
//	}

}
