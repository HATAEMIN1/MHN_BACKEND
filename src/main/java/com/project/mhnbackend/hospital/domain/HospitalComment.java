package com.project.mhnbackend.hospital.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class HospitalComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="hospital_id")
	private Hospital hospital;
	
//	@ManyToOne                      // 나중에 회원가입 로그인 하고나서 주석풀고 다시 확인해봐야함!!
//	@JoinColumn(name="member_id")
//	private Member member;
	
	private String content;
	private LocalDateTime createdAt;
	private int rating;
	
}
