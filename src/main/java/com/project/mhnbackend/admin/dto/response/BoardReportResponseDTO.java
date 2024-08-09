package com.project.mhnbackend.admin.dto.response;


import com.project.mhnbackend.freeBoard.domain.FreeBoard;
import com.project.mhnbackend.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardReportResponseDTO {
	private Long memberId;
	private FreeBoard freeBoard;
}
