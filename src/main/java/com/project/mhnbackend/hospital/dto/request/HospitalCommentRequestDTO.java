package com.project.mhnbackend.hospital.dto.request;

import com.project.mhnbackend.hospital.domain.Hospital;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class HospitalCommentRequestDTO {
//	private Long memberId;
	private Long hospitalId;
	private String comment;
	private int rating;
}
