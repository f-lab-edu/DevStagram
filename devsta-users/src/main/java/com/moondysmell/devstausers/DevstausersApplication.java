package com.moondysmell.devstausers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DevstausersApplication {

	@RequestMapping("/user")
	String home() {
		return "Hello World!";
	}

	public static void main(String[] args) {
		SpringApplication.run(DevstausersApplication.class, args);
	}

}
