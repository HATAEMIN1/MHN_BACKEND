package com.project.mhnbackend.hospital.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalCommentResponseDTO {
	//	private Long memberId;
	private Long hospitalId;
	private String comment;
	private int rating;
}
