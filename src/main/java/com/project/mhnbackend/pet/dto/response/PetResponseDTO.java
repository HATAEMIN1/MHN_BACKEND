package com.project.mhnbackend.pet.dto.response;


import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PetResponseDTO {
	private Long id;
	private String name;
	private String kind;
//	private int age;
	private LocalDate age;
	private String petImage;

}
