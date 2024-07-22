package com.project.mhnbackend.hospital.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalRequestDTO {
	private Long id;
	
	//	private Long boardId;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	private String phone;
}