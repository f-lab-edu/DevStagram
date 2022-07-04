package com.moondysmell.devstausers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope //서비스 개발시 모든 주소를 통해가야하기 때문에 controller로 옮겨줄 예정
public class DevstausersApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevstausersApplication.class, args);
	}

}
