package com.project.mhnbackend.admin.dto.response;

import com.project.mhnbackend.doctor.domain.Doctor;
import com.project.mhnbackend.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponseForMemberDTO {
	private Long id;
	private Member member;
//	private Doctor doctor;
}
