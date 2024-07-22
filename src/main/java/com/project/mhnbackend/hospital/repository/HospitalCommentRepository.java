package com.project.mhnbackend.hospital.repository;

import com.project.mhnbackend.hospital.domain.HospitalComment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalCommentRepository extends JpaRepository<HospitalComment,Long> {
//	List<HospitalComment> findByHospitalId(Long hospitalId);
	List<HospitalComment> findByHospitalId(Long hospitalId,  Sort sort);
}
