package com.moondysmell.devstaposts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class DevstapostsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevstapostsApplication.class, args);
	}

}
