package com.project.mhnbackend.hospital.repository;
import com.project.mhnbackend.hospital.domain.HospitalBMK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalBMKCountRepository extends JpaRepository <HospitalBMK,Long>  {
	
		// 새로 추가된 메서드: 특정 병원의 총 북마크 수를 조회
	@Query("SELECT COUNT(bmk) FROM HospitalBMK bmk WHERE bmk.hospital.id = :hospitalId")
	int countTotalBMKByHospital(@Param("hospitalId") Long hospitalId);
}
