package com.project.mhnbackend.hospital.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalBMKCountResponseDTO {
	private Long id;
	private Long hospitalId;
	// 수정본
	private int totalBMKCount;
}
