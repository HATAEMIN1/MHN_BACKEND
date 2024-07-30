package com.project.mhnbackend.pet.dto.request;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetRequestDTO {
	private Long memberId;
	private String name;
	private String kind;
//	private int age;
@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate age;
	
	@Builder.Default
	private List<MultipartFile> files = new ArrayList<> ();
	
	@Builder.Default
	private List<String> uploadFileNames = new ArrayList<>();
}