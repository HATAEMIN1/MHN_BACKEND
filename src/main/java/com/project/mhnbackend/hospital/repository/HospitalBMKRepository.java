package com.project.mhnbackend.hospital.repository;

import com.project.mhnbackend.hospital.domain.HospitalBMK;
import com.project.mhnbackend.hospital.dto.response.HospitalAccountBMKListResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalBMKRepository extends JpaRepository<HospitalBMK,Long> {
	
	@Query("select bmk from HospitalBMK bmk  where bmk.hospital.id = :hospitalId and  bmk.member.id = :memberId")
	HospitalBMK findByHospitalBMK(@Param ("hospitalId") Long hospitalId, @Param ("memberId") Long memberId);
	
	
	// 새로 추가된 메서드: 특정 병원의 총 북마크 수를 조회
	@Query("SELECT COUNT(bmk) FROM HospitalBMK bmk WHERE bmk.hospital.id = :hospitalId")
	int countTotalBMKByHospital(@Param("hospitalId") Long hospitalId);
	
//	@Query("SELECT new com.project.mhnbackend.hospital.dto.response.HospitalAccountBMKListResponseDTO(bmk.id, bmk.member.id, bmk.hospital.id) FROM HospitalBMK bmk WHERE bmk.member.id = :memberId")
//	List<HospitalAccountBMKListResponseDTO> findAllByMemberId (@Param("memberId") Long memberId);
List<HospitalBMK> findByMemberId(Long memberId);

}
