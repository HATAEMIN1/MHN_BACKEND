package com.project.mhnbackend.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberEditResponseDTO {
	private String email;
	private String nickName;
	private String profileImageUrl;
	private String tel;
	private String name;
}
