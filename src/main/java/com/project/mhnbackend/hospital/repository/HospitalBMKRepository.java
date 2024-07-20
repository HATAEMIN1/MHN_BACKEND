package com.project.mhnbackend.hospital.repository;

import com.project.mhnbackend.hospital.domain.HospitalBMK;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalBMKRepository extends JpaRepository<HospitalBMK,Long> {
//	List<HospitalBMK> findByHospitalId(Long hospitalId);
}
