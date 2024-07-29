package com.project.mhnbackend.doctor.dto.request;

import com.project.mhnbackend.hospital.domain.Hospital;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSignUpRequestDTO {
	private String email;
	private String password;
	private Long hospitalId;
}
