package com.project.mhnbackend.hospital.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class HospitalBMK {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@ManyToOne
//	@JoinColumn(name="member_id")
//	private Member member;
	
	@ManyToOne
	@JoinColumn(name="hospital_id")
	private Hospital hospital;
	
	private LocalDateTime createdAt;
}
