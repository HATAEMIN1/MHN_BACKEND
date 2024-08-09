package com.project.mhnbackend.admin.dto.response;

import com.project.mhnbackend.doctor.domain.Doctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponseForDoctorDTO {
	private Long id;
	private Doctor doctor;
}


