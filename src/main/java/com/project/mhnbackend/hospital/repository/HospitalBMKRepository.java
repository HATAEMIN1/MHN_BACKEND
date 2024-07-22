package com.project.mhnbackend.hospital.repository;

import com.project.mhnbackend.hospital.domain.HospitalBMK;
import com.project.mhnbackend.hospital.dto.response.HospitalBMKResponseDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalBMKRepository extends JpaRepository<HospitalBMK,Long> {
	
	@Query("select bmk from HospitalBMK bmk  where bmk.hospital.id = :hospitalId and  bmk.member.id = :memberId")
	HospitalBMK findByHospitalBMK(@Param ("hospitalId") Long hospitalId, @Param ("memberId") Long memberId);
	
	

}
