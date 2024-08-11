package com.project.mhnbackend.hospital.dto.response;

import com.project.mhnbackend.hospital.domain.Hospital;
import com.project.mhnbackend.hospital.domain.HospitalAppointment;
import com.project.mhnbackend.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalAppointmentResponseDTO {
	
	private LocalDateTime appointmentDateTime;
	private Member member;
	private Hospital hospital;
	private HospitalAppointment.AppointmentStatus status;
}
