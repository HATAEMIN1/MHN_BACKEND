package com.project.mhnbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.project.mhnbackend.chart.repository", "com.project.mhnbackend.chatBoard.repository", "com.project.mhnbackend.freeBoard.repository", "com.project.mhnbackend.hospital.repository", "com.project.mhnbackend.member.repository", "com.project.mhnbackend.payment.repository", "com.project.mhnbackend.pet.repository", "com.project.mhnbackend.subscription.repository",})
@EnableMongoRepositories(basePackages = "com.project.mhnbackend.chatBoard.mongo.repository")
@EntityScan(basePackages = {"com.project.mhnbackend.chart.domain", "com.project.mhnbackend.chatBoard.domain", "com.project.mhnbackend.freeBoard.domain", "com.project.mhnbackend.hospital.domain", "com.project.mhnbackend.member.domain", "com.project.mhnbackend.payment.domain", "com.project.mhnbackend.pet.domain", "com.project.mhnbackend.subscription.domain"})
public class
MhnBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MhnBackendApplication.class, args);
	}

}
