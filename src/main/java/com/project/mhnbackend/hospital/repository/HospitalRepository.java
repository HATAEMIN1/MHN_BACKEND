package com.project.mhnbackend.hospital.repository;

import com.project.mhnbackend.hospital.domain.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
	
	
	//=== gpt버전 3km이내 반경 ===
	@Query("SELECT h FROM Hospital h WHERE h.latitude BETWEEN :minLat AND :maxLat AND h.longitude BETWEEN :minLon AND :maxLon")
	List<Hospital> findHospitalsInArea(@Param("minLat") double minLat,
	                                   @Param("maxLat") double maxLat,
	                                   @Param("minLon") double minLon,
	                                   @Param("maxLon") double maxLon);

}
