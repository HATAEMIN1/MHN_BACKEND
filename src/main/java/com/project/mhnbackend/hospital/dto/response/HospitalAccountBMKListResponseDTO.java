package com.project.mhnbackend.hospital.dto.response;

import com.project.mhnbackend.hospital.domain.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalAccountBMKListResponseDTO {
	private Long id;
	private Long memberId;
	private Hospital hospital;
}
