package com.project.mhnbackend.doctor.dto.response;


import com.project.mhnbackend.doctor.domain.Doctor;
import com.project.mhnbackend.hospital.domain.Hospital;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorLoginResponseDTO {
	private Long id;
	private Hospital hospital;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	@Enumerated(EnumType.STRING)
	private Doctor.DoctorStatus doctorStatus;
	private String accessToken;
	private String refreshToken;
}
