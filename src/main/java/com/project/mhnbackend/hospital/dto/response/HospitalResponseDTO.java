package com.project.mhnbackend.hospital.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalResponseDTO {
	private Long id;
	
	//	private Long boardId;
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	private String phone;
}