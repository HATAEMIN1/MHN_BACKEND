package com.project.mhnbackend.pet.dto.response;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.project.mhnbackend.pet.domain.PetImage;
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
	
//	@Builder.Default
//	private List<String> uploadFileNames = new ArrayList<> ();
//
//	public void setUploadFileNames(List<String> uploadFileNames) {
//		this.uploadFileNames = uploadFileNames;
//	}
@Builder.Default
private List<PetImage> petImage = new ArrayList<>();  // petImage로 유지

}
