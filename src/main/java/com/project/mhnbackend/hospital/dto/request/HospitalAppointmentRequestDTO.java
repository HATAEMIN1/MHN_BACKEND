package com.project.mhnbackend.hospital.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalAppointmentRequestDTO {
	private LocalDateTime appointmentDateTime;
	private Long memberId;
	private Long hospitalId;
}
