package com.project.mhnbackend.pet.dto.response;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PetResponseDTO {
	private Long id;
	private String name;
	private String kind;
	private int age;

}
