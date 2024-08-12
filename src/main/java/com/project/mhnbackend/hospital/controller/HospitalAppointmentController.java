package com.project.mhnbackend.hospital.controller;


import com.project.mhnbackend.hospital.domain.HospitalAppointment;
import com.project.mhnbackend.hospital.dto.request.HospitalAppointmentRequestDTO;
import com.project.mhnbackend.hospital.service.HospitalAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HospitalAppointmentController {
	private final HospitalAppointmentService hospitalAppointmentService;
	
	
	@PostMapping("/hospital/appointment")
	public HospitalAppointment createAppointment(
			@RequestBody HospitalAppointmentRequestDTO hospitalAppointmentRequestDTO
	) {
		return hospitalAppointmentService.createAppointment(hospitalAppointmentRequestDTO);
	}
}
