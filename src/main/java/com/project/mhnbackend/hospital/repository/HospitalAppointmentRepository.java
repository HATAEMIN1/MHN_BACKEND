package com.project.mhnbackend.hospital.repository;

import com.project.mhnbackend.hospital.domain.HospitalAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HospitalAppointmentRepository extends JpaRepository<HospitalAppointment,Long> {
	Optional<HospitalAppointment> findByHospitalIdAndAppointmentDateTime(Long hospitalId, LocalDateTime appointmentDateTime);
	
	List<HospitalAppointment> findByHospitalId(Long hospitalId);
}
