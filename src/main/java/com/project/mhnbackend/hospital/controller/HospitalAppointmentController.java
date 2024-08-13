package com.project.mhnbackend.hospital.controller;


import com.project.mhnbackend.hospital.domain.HospitalAppointment;
import com.project.mhnbackend.hospital.dto.request.HospitalAppointmentRequestDTO;
import com.project.mhnbackend.hospital.dto.response.HospitalAppointmentResponseDTO;
import com.project.mhnbackend.hospital.service.HospitalAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HospitalAppointmentController {
	private final HospitalAppointmentService hospitalAppointmentService;
	
	
	// 유저 진료예약 post
	@PostMapping("/hospital/appointment")
	public HospitalAppointment createAppointment(
			@RequestBody HospitalAppointmentRequestDTO hospitalAppointmentRequestDTO
	) {
		return hospitalAppointmentService.createAppointment(hospitalAppointmentRequestDTO);
	}
	
	// 의사 진료예약신청 리스트 get
	@GetMapping("/hospital/appointment")
	public List<HospitalAppointmentResponseDTO> getAppointments(
			@RequestParam("hospitalId") Long hospitalId      // axios경로에서 쓸 param임,,, 프론트 url이랑은 다름
//			@RequestParam("memberId") Long memberId
	){
		return hospitalAppointmentService.getAppointments(hospitalId);
	}
}
