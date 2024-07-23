package com.project.mhnbackend.hospital.repository;

import com.project.mhnbackend.hospital.domain.HospitalComment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalCommentRepository extends JpaRepository<HospitalComment,Long> {
	// 병원 아이디를 통해 코멘트를 전부 겟
	//	List<HospitalComment> findByHospitalId(Long hospitalId);
	List<HospitalComment> findByHospitalId(Long hospitalId,  Sort sort);
	
	
	// 코멘트를 통해 별점의 평균 겟
	@Query("SELECT AVG(hc.rating) FROM HospitalComment hc WHERE hc.hospital.id = :hospitalId")
	Double getAVGRating(@Param("hospitalId") Long hospitalId);
	
}