package com.project.mhnbackend.admin.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.mhnbackend.subscription.domain.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponseForSubMemberDTO {
	private Long id;
	
	private LocalDateTime createdAt;
	
	private Long memberId;
}
