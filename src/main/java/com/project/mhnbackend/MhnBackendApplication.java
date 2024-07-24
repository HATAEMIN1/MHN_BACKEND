package com.project.mhnbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class
MhnBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MhnBackendApplication.class, args);
	}

}
