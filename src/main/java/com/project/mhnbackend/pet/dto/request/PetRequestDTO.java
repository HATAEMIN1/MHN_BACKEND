package com.project.mhnbackend.pet.dto.request;


import lombok.Data;

@Data
public class PetRequestDTO {
	private String name;
	private String kind;
	private int age;


}