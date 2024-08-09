package com.project.mhnbackend.admin.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class BoardReportRequestDTO {
	private Long memberId;
	private Long freeBoardId;
}
