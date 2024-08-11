package com.project.mhnbackend.hospital.repository;

import com.project.mhnbackend.hospital.domain.HospitalAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalAppointmentRepository extends JpaRepository<HospitalAppointment,Long> {
}
