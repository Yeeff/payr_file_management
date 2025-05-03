package com.horizonx.file_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FileServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileServicesApplication.class, args);
	}

}
