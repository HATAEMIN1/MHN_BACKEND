package com.project.mhnbackend.pet.dto.request;


import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PetRequestDTO {
	private String name;
	private String kind;
//	private int age;
	private LocalDate age;
	private MultipartFile petImage; 
}