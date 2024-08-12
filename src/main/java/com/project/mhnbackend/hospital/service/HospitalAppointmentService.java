package com.project.mhnbackend.hospital.service;

import com.project.mhnbackend.hospital.domain.Hospital;
import com.project.mhnbackend.hospital.domain.HospitalAppointment;
import com.project.mhnbackend.hospital.dto.request.HospitalAppointmentRequestDTO;
import com.project.mhnbackend.hospital.repository.HospitalAppointmentRepository;
import com.project.mhnbackend.hospital.repository.HospitalRepository;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class HospitalAppointmentService {
		private final HospitalAppointmentRepository hospitalAppointmentRepository;
		private final MemberRepository memberRepository;
		private final HospitalRepository hospitalRepository;
	
	// 병원 진료예약 포스트
	public HospitalAppointment createAppointment(HospitalAppointmentRequestDTO hospitalAppointmentRequestDTO) {
		Hospital hospital = hospitalRepository.findById(hospitalAppointmentRequestDTO.getHospitalId())
				.orElseThrow(() -> new RuntimeException("Hospital not found"));
		Member member = memberRepository.findById(hospitalAppointmentRequestDTO.getMemberId())
				.orElseThrow(() -> new RuntimeException("Member not found"));
		
		// 중복 예약 체크
		boolean isTimeSlotAvailable = checkTimeSlotAvailability(
				hospital.getId(),
				hospitalAppointmentRequestDTO.getAppointmentDateTime()
		);
		
		if (!isTimeSlotAvailable) {
			throw new RuntimeException("이미 예약된 시간입니다.");
		}
		
		HospitalAppointment hospitalAppointment = HospitalAppointment.builder()
				.appointmentDateTime(hospitalAppointmentRequestDTO.getAppointmentDateTime())
				.member(member)
				.hospital(hospital)
				.createdAt(LocalDateTime.now())
				.build();
		
		return hospitalAppointmentRepository.save(hospitalAppointment);
	}
	
	// 중복 예약 체크 메서드
	private boolean checkTimeSlotAvailability(Long hospitalId, LocalDateTime dateTime) {
		return hospitalAppointmentRepository.findByHospitalIdAndAppointmentDateTime(hospitalId, dateTime).isEmpty();
	}
}
