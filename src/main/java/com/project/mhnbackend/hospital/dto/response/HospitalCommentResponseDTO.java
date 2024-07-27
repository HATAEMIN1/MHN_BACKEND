package com.project.mhnbackend.hospital.dto.response;


import com.project.mhnbackend.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalCommentResponseDTO {
	private Long id;
	private Member member;
	private Long hospitalId;
	private String comment;
	private LocalDateTime createdAt;
	private int rating;
}