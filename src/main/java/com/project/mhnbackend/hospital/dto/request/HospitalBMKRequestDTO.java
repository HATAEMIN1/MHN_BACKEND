package com.project.mhnbackend.hospital.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalBMKRequestDTO {
//	private Long id;
		private Long memberId;
	private Long hospitalId;
}
